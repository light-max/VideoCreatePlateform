//
// Created by lifengqiang on 2022/10/25.
//
#include "pfr.h"
#include "comm/mylog.h"

long getMicrosecond() {
    struct timeval tv{};
    gettimeofday(&tv, nullptr);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}

void printFrameRate() {
    static int frame = 0;
    static long start_time = getMicrosecond();
    static long current_time = getMicrosecond();
    static int second = 0;
    long time = getMicrosecond();
    frame++;
    if ((time - start_time) / 1000000 > (current_time - start_time) / 1000000) {
        current_time = time;
        second++;
        LOGI("gl %d %d", second, frame);
        frame = 0;
    }
}