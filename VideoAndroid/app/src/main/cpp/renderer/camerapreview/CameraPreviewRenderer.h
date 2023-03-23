//
// Created by lifengqiang on 2023/1/30.
//

#ifndef VIDEOSHARE_CAMERAPREVIEWRENDERER_H
#define VIDEOSHARE_CAMERAPREVIEWRENDERER_H

#include "comm/pfr.h"
#include <GLES3/gl3.h>
#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>
#include "renderer/frame/i420frame/GLI420Frame.h"
#include "renderer/shader/shader.h"
#include "interfaces/OnFrameRendererCallback.h"
#include "renderer/image/NativeImage.h"

#define PI 3.14159265358979323846

class CameraPreviewRenderer {
private:
    int program;
    int handle[3];
    glm::vec2 screenSize;
private:
    int fboProgram;
    int fboHandle[3];
    // yuv纹理
    unsigned int textureId[3];
    // fbo渲染后的rgba纹理
    unsigned int fboTextureId;
    unsigned int fboId;
private:
    float *vertex = nullptr;
    float texture_coord[8] = {
            0, 1,
            1, 1,
            0, 0,
            1, 0,
    };
    float *screen_vertex = nullptr;
    float screen_texture_coord[8]{
            0, 0,
            1, 0,
            0, 1,
            1, 1,
    };
    glm::mat4 mvp_matrix;
    glm::mat4 fbo_mvp_matrix;
    unsigned char *fboFrameBufferData = nullptr;
public:
    GLI420Frame gli420Frame;
    Mutex callback_mutex;
    OnFrameRendererCallback *callback = nullptr;

public:
    CameraPreviewRenderer() {
        vertex = new float[]{
                -1, 1,
                1, 1,
                -1, -1,
                1, -1
        };
        screen_vertex = vertex;
        fboFrameBufferData = new unsigned char[1080 * 1920 * 4];
    }

    ~CameraPreviewRenderer() {
        delete vertex;
        delete screen_vertex;
        delete fboFrameBufferData;
    }

    void onSurfaceCreated();

    void onSurfaceChanged(int width, int height);

    void onDraw();

    void setImageCanvas(int width, int height, int angle) {
        gli420Frame.setImageCanvas(width, height, angle);
        if (angle % 180) {
            float ratio = 1.0 * width / height;
            delete vertex;
            vertex = new float[]{
                    -ratio, 1,
                    ratio, 1,
                    -ratio, -1,
                    ratio, -1,
            };
            delete screen_vertex;
            screen_vertex = new float[]{
                    -1, ratio,
                    1, ratio,
                    -1, -ratio,
                    1, -ratio,
            };
        } else {
            float ratio = 1.0 * height / width;
            delete vertex;
            vertex = new float[]{
                    -1, ratio,
                    1, ratio,
                    -1, -ratio,
                    1, -ratio
            };
            delete screen_vertex;
            screen_vertex = new float[]{
                    -ratio, 1,
                    ratio, 1,
                    -ratio, -1,
                    ratio, -1,
            };
        }
    }

    void setFrameRendererCallback(OnFrameRendererCallback *callback) {
        callback_mutex.lock();
        if (this->callback != nullptr) {
            delete this->callback;
        }
        this->callback = callback;
        callback_mutex.unlock();
    }

    void updateFBOMVPMatrix() {
        float ratio = (float) gli420Frame.width / (float) gli420Frame.height;
        glm::mat4 projection = glm::ortho(-ratio, ratio, -1.f, 1.f, 2.f, 10.f);
        glm::mat4 view = glm::lookAt(
                glm::vec3(0.f, 0.f, 2.f),
                glm::vec3(0.f, 0.f, 0.f),
                glm::vec3(0.f, 1.f, 0.f)
        );
        auto angle = (float) ((float) gli420Frame.angle / 180.f * PI);
        glm::mat4 model = glm::mat4(1.f);
        if (gli420Frame.mirror == 0) {
            model = glm::rotate(model, angle, glm::vec3(0.f, 0.f, 1.f));
        } else {
            model = glm::rotate(model, angle, glm::vec3(0.f, 0.f, 1.f));
            model = glm::rotate(model, (float) PI, glm::vec3(1.f, 0.f, 0.f));
        }
        fbo_mvp_matrix = projection * view * model;
    }

    void updateMVPMatrix() {
        float ratio = (float) screenSize.y / (float) screenSize.x;
        glm::mat4 projection = glm::ortho(-1.f, 1.f, -ratio, ratio, 2.f, 10.f);
        glm::mat4 view = glm::lookAt(
                glm::vec3(0.f, 0.f, 2.f),
                glm::vec3(0.f, 0.f, 0.f),
                glm::vec3(0.f, 1.f, 0.f)
        );
        glm::mat4 model = glm::mat4(1.f);
        model = glm::translate(model, glm::vec3(0.f, 0.f, 0.f));
        mvp_matrix = projection * view * model;
    }

    void createFrameBufferObj() {
        // 创建一个 2D 纹理用于连接 FBO 的颜色附着
        glGenTextures(1, &fboTextureId);
        glBindTexture(GL_TEXTURE_2D, fboTextureId);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, GL_NONE);

        // 创建 FBO
        glGenFramebuffers(1, &fboId);
        // 绑定 FBO
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        // 绑定 FBO 纹理
        glBindTexture(GL_TEXTURE_2D, fboTextureId);
        // 将纹理连接到 FBO 附着
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTextureId,
                               0);
        // 分配内存大小
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1080, 1920, 0, GL_RGBA, GL_UNSIGNED_BYTE, nullptr);
        // 检查 FBO 的完整性状态
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            LOGE("FBOSample::CreateFrameBufferObj glCheckFramebufferStatus status != GL_FRAMEBUFFER_COMPLETE");
            return;
        }
        // 解绑纹理
        glBindTexture(GL_TEXTURE_2D, GL_NONE);
        // 解绑 FBO
        glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE);
    }

    void getRenderFrameFromBBO() {
        glReadPixels(0, 0, 1080, 1920, GL_RGBA, GL_UNSIGNED_BYTE, fboFrameBufferData);
        callback_mutex.lock();
        if (callback != nullptr) {
            callback->draw(fboFrameBufferData, 1080 * 1920 * 4);
        }
        callback_mutex.unlock();
    }
};


#endif //VIDEOSHARE_CAMERAPREVIEWRENDERER_H
