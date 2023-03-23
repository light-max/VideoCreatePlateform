//
// Created by lifengqiang on 2023/3/15.
//

#include "VideoRenderer.h"

void VideoRenderer::onCreated() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDepthMask(GL_FALSE);
#ifdef USE_RGBA
    program = shader::createProgram("framei420_vertex", "framei420_fragment_fbo");
    glGenTextures(1, textureId);
    glBindTexture(GL_TEXTURE_2D, textureId[0]);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glUseProgram(program);
    glUniform1i(glGetUniformLocation(program, "uTexture"), 0);
#endif
#ifdef USE_I420
    program = shader::createProgram("framei420_vertex", "framei420_fragment");
    glGenTextures(3, textureId);
    for (unsigned int i : textureId) {
        glBindTexture(GL_TEXTURE_2D, i);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }
    glUseProgram(program);
    glUniform1i(glGetUniformLocation(program, "yTexture"), 0);
    glUniform1i(glGetUniformLocation(program, "uTexture"), 1);
    glUniform1i(glGetUniformLocation(program, "vTexture"), 2);
#endif
    handle[0] = glGetAttribLocation(program, "vCoord");
    handle[1] = glGetAttribLocation(program, "tCoord");
    handle[2] = glGetUniformLocation(program, "uMatrix");
    glEnableVertexAttribArray(handle[0]);
    glEnableVertexAttribArray(handle[1]);
}

void VideoRenderer::onSizeChanged(int width, int height) {
    glViewport(0, 0, width, height);
    screenSize.x = width;
    screenSize.y = height;
    updateMVPMatrix();
}

void VideoRenderer::onDraw() {
    glClear(GL_COLOR_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    if (buffer != nullptr) {
        buffer->lock();
        if (buffer->hasData()) {
            if (buffer->width != (int) videoSize.x ||
                buffer->height != (int) videoSize.y) {
                buffer->unlock();
                return;
            }
            glUseProgram(program);
            glVertexAttribPointer(handle[0], 2, GL_FLOAT, false, 8, vertex);
            glVertexAttribPointer(handle[1], 2, GL_FLOAT, false, 8, texture_coord);
            glUniformMatrix4fv(handle[2], 1, false, &mvp_matrix[0][0]);
#ifdef USE_RGBA
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId[0]);
            glTexImage2D(GL_TEXTURE_2D, 0,
                         GL_RGBA, buffer->width, buffer->height,
                         0, GL_RGBA,
                         GL_UNSIGNED_BYTE, buffer->data);
#endif
#ifdef USE_I420
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId[0]);
            glTexImage2D(GL_TEXTURE_2D, 0,
                         GL_LUMINANCE, buffer->width, buffer->height,
                         0, GL_LUMINANCE,
                         GL_UNSIGNED_BYTE, buffer->getY());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, textureId[1]);
            glTexImage2D(GL_TEXTURE_2D, 0,
                         GL_LUMINANCE, buffer->width / 2, buffer->height / 2,
                         0, GL_LUMINANCE,
                         GL_UNSIGNED_BYTE, buffer->getU());
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, textureId[2]);
            glTexImage2D(GL_TEXTURE_2D, 0,
                         GL_LUMINANCE, buffer->width / 2, buffer->height / 2,
                         0, GL_LUMINANCE,
                         GL_UNSIGNED_BYTE, buffer->getV());
#endif
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        }else{
            LOGI("none");
        }
        buffer->unlock();
    }
}
