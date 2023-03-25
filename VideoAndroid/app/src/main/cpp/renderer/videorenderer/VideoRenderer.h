//
// Created by lifengqiang on 2023/3/15.
//

#ifndef VIDEOSHARE_VIDEORENDERER_H
#define VIDEOSHARE_VIDEORENDERER_H

#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <GLES3/gl3.h>
#include "mylog.h"
#include "renderer/image/NativeFrameBuffer.h"
#include "renderer/shader/shader.h"
#include "pfr.h"

#define USE_RGBA

class VideoRenderer {
private:
    glm::vec2 screenSize = glm::vec2(0, 0);
    glm::vec2 videoSize = glm::vec2(0, 0);
    NativeFrameBuffer *buffer = nullptr;

    int program;
    int handle[3];
    unsigned int textureId[3];

    glm::mat4 mvp_matrix;
    glm::vec2 vertex[4] = {
            glm::vec2(-1, 1),
            glm::vec2(1, 1),
            glm::vec2(-1, -1),
            glm::vec2(1, -1),
    };
    glm::vec2 texture_coord[4] = {
            glm::vec2(0, 0),
            glm::vec2(1, 0),
            glm::vec2(0, 1),
            glm::vec2(1, 1),
    };
public:
    void onCreated();

    void onSizeChanged(int width, int height);

    void onDraw();

    void setNativeFrameBuffer(long handler) {
        this->buffer = (NativeFrameBuffer *) handler;
    }

    void updateMVPMatrix(int width, int height) {
        this->videoSize = glm::vec2(width, height);
        updateMVPMatrix();
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
        float height = videoSize.y / videoSize.x;
        vertex[0] = glm::vec2(-1, height);
        vertex[1] = glm::vec2(1, height);
        vertex[2] = glm::vec2(-1, -height);
        vertex[3] = glm::vec2(1, -height);
    }
};


#endif //VIDEOSHARE_VIDEORENDERER_H
