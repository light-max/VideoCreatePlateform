//
// Created by lifengqiang on 2022/8/22.
//

#ifndef VIDEOUSE_NATIVEOBJECTMANAGER_H
#define VIDEOUSE_NATIVEOBJECTMANAGER_H

#include <string>
#include <map>
#include "mylog.h"
#include <jni.h>

namespace android {
    /**
     * java NativeObject.mHandler存储的是Wrapper的地址
     * NativeObject对象的C对象存储在Wrapper的object指向的地址中
     */
    class BaseNativeObjectWrapper {
    public:
        void *object = nullptr;
    };

    template<typename T>
    class NativeObjectWrapper : public BaseNativeObjectWrapper {
    public:
        NativeObjectWrapper() {
            object = new T;
        }

        ~NativeObjectWrapper() {
            T *t = (T *) object;
            delete t;
//            LOGI("delete wrapper %ld", object);
        }

        T *get_object() {
            return (T *) object;
        }
    };

    class BaseNativeObjectWrapperFactory {
    public:
        virtual BaseNativeObjectWrapper *create_wrapper() = 0;

        virtual void release_wrapper(int64_t handler) = 0;
    };

    template<typename T>
    class NativeObjectWrapperFactory : public BaseNativeObjectWrapperFactory {
    public:
        NativeObjectWrapper<T> *create_wrapper() override {
            return new NativeObjectWrapper<T>();
        }

        void release_wrapper(int64_t handler) override {
            NativeObjectWrapper<T> *wrapper = (NativeObjectWrapper<T> *) handler;
            delete wrapper;
//            LOGI("delete %ld",wrapper);
        }
    };

    class NativeObjectManager {
    private:
        static NativeObjectManager *instance;
        std::map<std::string, BaseNativeObjectWrapperFactory *> m_class_map;
    public:
        static NativeObjectManager *get_instance();

        template<typename T>
        void register_class(const std::string &class_name) {
            m_class_map[class_name] = new NativeObjectWrapperFactory<T>();
        }

        BaseNativeObjectWrapperFactory *get_factory(const std::string &class_name) {
            return m_class_map[class_name];
        }

        template<typename T>
        static NativeObjectWrapper<T> *get_wrapper(JNIEnv *env, jobject obj) {
            jclass clazz = env->GetObjectClass(obj);
            jfieldID fieldId = env->GetFieldID(clazz, "mHandler", "J");
            jlong handler = env->GetLongField(obj, fieldId);
            return (NativeObjectWrapper<T> *) handler;
        }

        template<typename T>
        static T *get_object(JNIEnv *env, jobject obj) {
            NativeObjectWrapper<T> *wrapper = get_wrapper<T>(env, obj);
            return wrapper->get_object();
        }
    };

    namespace jni {
        template<typename T>
        class NativeObjectWrapper;

        template<typename T>
        NativeObjectWrapper<T> *get_wrapper(JNIEnv *env, jobject obj) {
            return NativeObjectManager::get_wrapper<T>(env, obj);
        }

        template<typename T>
        T *get_object(JNIEnv *env, jobject obj) {
            return NativeObjectManager::get_object<T>(env, obj);
        }
    }
}

#endif //VIDEOUSE_NATIVEOBJECTMANAGER_H
