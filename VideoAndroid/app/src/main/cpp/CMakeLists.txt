project(AVProject)

cmake_minimum_required(VERSION 3.18.1)

include_directories(.)
include_directories(./comm)
include_directories(./ffmpeg/include)
include_directories(./libyuv)

add_library(renderer SHARED
        android/JniManager.cpp
        android/JNativeObject.cpp
        android/JCameraPreviewRenderer.cpp
        android/JAndroidImage.cpp
        android/JNativeImage.cpp
        android/JMediaRecordEncoder.cpp
        android/JFFMediaPlayer.cpp
        android/JVideoRenderer.cpp

        comm/NativeObjectManager.cpp
        comm/pfr.cpp

        renderer/camerapreview/CameraPreviewRenderer.cpp
        renderer/shader/shader.cpp
        renderer/image/AndroidImage.cpp
        renderer/image/NativeImage.cpp
        renderer/image/NativeFrameBuffer.cpp
        renderer/frame/i420frame/GLI420Frame.cpp
        renderer/videorenderer/VideoRenderer.cpp

        media/encoder/FFMediaEncoder.cpp
        media/player/FFMediaPlayer.cpp
        media/decoder/FFCodec.cpp
        media/decoder/FFCodecAudio.cpp
        media/decoder/FFCodecVideo.cpp
        media/decoder/MsgQueue.cpp
        )

#ff
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libavcodec.so)

add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libavformat.so)

add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libavutil.so)

add_library(postproc SHARED IMPORTED)
set_target_properties(postproc PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libpostproc.so)

add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libswresample.so)

add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libswscale.so)
#ff
#libyuv
add_library(yuv SHARED IMPORTED)
set_target_properties(yuv PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libyuv.so)
add_library(yuvutil SHARED IMPORTED)
set_target_properties(yuvutil PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libyuvutil.so)
#libyuv

target_link_libraries(renderer
        android
        log
        GLESv3

        avcodec
        avformat
        avutil
        postproc
        swresample
        swscale
        yuv
        yuvutil
        )