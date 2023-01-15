//
// Created by lifengqiang on 2022/8/22.
//

#ifndef VIDEOUSE_MUTEX_H
#define VIDEOUSE_MUTEX_H

#include <pthread.h>

class Mutex {
private:
    pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
public:
    void lock() {
        pthread_mutex_lock(&mutex);
    }

    void unlock() {
        pthread_mutex_unlock(&mutex);
    }
};


#endif //VIDEOUSE_MUTEX_H
