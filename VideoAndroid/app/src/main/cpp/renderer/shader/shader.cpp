//
// Created by lifengqiang on 2022/10/16.
//

#include "shader.h"

AAssetManager *g_am = nullptr;

void shader::registerAssertManager(JNIEnv *env, jobject object) {
    g_am = AAssetManager_fromJava(env, object);
}

char *shader::readShaderFile(char *name) {
    char *filename = (char *) malloc(strlen(name) + 6);
    strcpy(filename, name);
    strcat(filename, ".glsl");
    AAsset *asset = AAssetManager_open(g_am, filename, AASSET_MODE_UNKNOWN);
    free(filename);
    if (asset != nullptr) {
        int length = AAsset_getLength(asset);
        char *str = (char *) malloc(length + 1);
        str[length] = 0;
        AAsset_read(asset, str, length);
        AAsset_close(asset);
        return str;
    }
    return nullptr;
}

void shader::loadShader(int program, int type, char *code) {
    int shader = glCreateShader(type);
    glShaderSource(shader, 1, &code, nullptr);
    glCompileShader(shader);
    glAttachShader(program, shader);
}

int shader::createProgram(char *v_name, char *f_name) {
    int program = glCreateProgram();
    char *vCode = shader::readShaderFile(v_name);
    char *fCode = shader::readShaderFile(f_name);
    shader::loadShader(program, GL_VERTEX_SHADER, vCode);
    shader::loadShader(program, GL_FRAGMENT_SHADER, fCode);
    glLinkProgram(program);
    free(vCode);
    free(fCode);
    return program;
}
