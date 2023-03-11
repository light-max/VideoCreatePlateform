//
// Created by lifengqiang on 2022/10/16.
//

#ifndef GLESGAME_SHADER_H
#define GLESGAME_SHADER_H

#include  "mylog.h"
#include <string.h>
#include <malloc.h>
#include <jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <GLES3/gl3.h>

extern AAssetManager *g_am;

namespace shader {
    void registerAssertManager(JNIEnv *env, jobject object);

    char *readShaderFile(char *name);

    void loadShader(int program, int type, char *code);

    /**
     * 创建渲染程序 并且加入顶点着色器和片元着色器，之后链接程序
     * @param v_name 顶点着色器文件名 不包含后缀名
     * @param f_name 片元着色器文件名 不好含后缀名
     * @return 着色器程序
     */
    int createProgram(char *v_name, char *f_name);
}

#endif //GLESGAME_SHADER_H
