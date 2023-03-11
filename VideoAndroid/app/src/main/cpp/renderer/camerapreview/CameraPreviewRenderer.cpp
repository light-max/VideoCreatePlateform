//
// Created by lifengqiang on 2023/1/30.
//

#include "CameraPreviewRenderer.h"

void CameraPreviewRenderer::onSurfaceCreated() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDepthMask(GL_FALSE);

    createFrameBufferObj();

    program = shader::createProgram("framei420_vertex", "framei420_fragment_fbo");
    fboProgram = shader::createProgram("framei420_vertex", "framei420_fragment");
    fboHandle[0] = glGetAttribLocation(fboProgram, "vCoord");
    fboHandle[1] = glGetAttribLocation(fboProgram, "tCoord");
    fboHandle[2] = glGetUniformLocation(fboProgram, "uMatrix");
    handle[0] = glGetAttribLocation(program, "vCoord");
    handle[1] = glGetAttribLocation(program, "tCoord");
    handle[2] = glGetUniformLocation(program, "uMatrix");
    glEnableVertexAttribArray(fboHandle[0]);
    glEnableVertexAttribArray(fboHandle[1]);
    glEnableVertexAttribArray(handle[0]);
    glEnableVertexAttribArray(handle[1]);
    glUseProgram(fboProgram);
    glGenTextures(3, textureId);
    for (int i = 0; i < 3; ++i) {
        glBindTexture(GL_TEXTURE_2D, textureId[i]);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, GL_NONE);
    }
    glUniform1i(glGetUniformLocation(fboProgram, "yTexture"), 0);
    glUniform1i(glGetUniformLocation(fboProgram, "uTexture"), 1);
    glUniform1i(glGetUniformLocation(fboProgram, "vTexture"), 2);
    glUseProgram(program);
    glBindTexture(GL_TEXTURE_2D, fboTextureId);
    glUniform1i(glGetUniformLocation(program, "uTexture"), 0);
}

void CameraPreviewRenderer::onSurfaceChanged(int width, int height) {
    glViewport(0, 0, width, height);
    screenSize.x = width;
    screenSize.y = height;
}

void CameraPreviewRenderer::onDraw() {
    if (gli420Frame.data == nullptr)return;
    glClear(GL_COLOR_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glBindFramebuffer(GL_FRAMEBUFFER, fboId);
    glViewport(0, 0, 1080, 1920);
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(fboProgram);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, fboTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1080, 1920, 0, GL_RGBA, GL_UNSIGNED_BYTE, nullptr);
    glVertexAttribPointer(fboHandle[0], 2, GL_FLOAT, false, 8, vertex);
    glVertexAttribPointer(fboHandle[1], 2, GL_FLOAT, false, 8, texture_coord);
    updateFBOMVPMatrix();
    glUniformMatrix4fv(fboHandle[2], 1, false, &fbo_mvp_matrix[0][0]);
    //绘制yuv
    gli420Frame.lock();
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureId[0]);
    glTexImage2D(GL_TEXTURE_2D, 0,
                 GL_LUMINANCE, gli420Frame.width, gli420Frame.height,
                 0, GL_LUMINANCE,
                 GL_UNSIGNED_BYTE, gli420Frame.plane_y);
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, textureId[1]);
    glTexImage2D(GL_TEXTURE_2D, 0,
                 GL_LUMINANCE, gli420Frame.width / 2, gli420Frame.height / 2,
                 0, GL_LUMINANCE,
                 GL_UNSIGNED_BYTE, gli420Frame.plane_u);
    glActiveTexture(GL_TEXTURE2);
    glBindTexture(GL_TEXTURE_2D, textureId[2]);
    glTexImage2D(GL_TEXTURE_2D, 0,
                 GL_LUMINANCE, gli420Frame.width / 2, gli420Frame.height / 2,
                 0, GL_LUMINANCE,
                 GL_UNSIGNED_BYTE, gli420Frame.plane_v);
    gli420Frame.unlock();
    //绘制yuv
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    getRenderFrameFromBBO();
    glBindTexture(GL_TEXTURE_2D, GL_NONE);
    glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE);

    glClear(GL_COLOR_BUFFER_BIT);
    glViewport(0, 0, screenSize.x, screenSize.y);
    glUseProgram(program);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, fboTextureId);
    glVertexAttribPointer(handle[0], 2, GL_FLOAT, false, 8, screen_vertex);
    glVertexAttribPointer(handle[1], 2, GL_FLOAT, false, 8, screen_texture_coord);
    updateMVPMatrix();
    glUniformMatrix4fv(handle[2], 1, false, &mvp_matrix[0][0]);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}
