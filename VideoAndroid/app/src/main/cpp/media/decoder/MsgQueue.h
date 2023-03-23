//
// Created by lifengqiang on 2022/9/12.
//

#ifndef VIDEOUSE_MSGQUEUE_H
#define VIDEOUSE_MSGQUEUE_H

#include "mylog.h"

namespace msg {
    namespace type {
        enum {
            NONE = 0,
            MediaChange,
            Goon,
            Decode,
            Pause,
            Release,
            Seek,
        };
    }

    class Msg {
    private:
        int type;
        void *data;
    public:
        ~Msg();

        Msg();

        Msg(int type);

        Msg(int type, void *data);

        int getType() const;

        void *getData();
    };

    class MsgQueue {
    private:
        Msg *queue;
        int count;
        // 队首元素下标
        int head;
        // 即将插入的元素下标
        int end;
    public:
        MsgQueue(int maxQueue);

        bool empty() const;

        void push(const Msg &msg);

        int size() const;

        void pop();

        Msg &front() const;

        Msg &back() const;

        void clear();

        /**msg**/
        void pushCommAction(int type) {
            if (empty()) {
                push(Msg(type));
            } else {
                if (back().getType() != type) {
                    push(Msg(type));
                }
            }
        }

        void setMediaPath(const char *path) {
//            clear();
            push(Msg(type::MediaChange, (void *) path));
        }

        void goon() { pushCommAction(type::Goon); }

        void decode() { pushCommAction(type::Decode); }

        void pause() { pushCommAction(type::Pause); }

        void release() {
            clear();
            pushCommAction(type::Release);
        }

        void seek(int progress) {
            for (int i = 0; i < count; ++i) {
                if (queue[i].getType() == type::NONE) {
                    queue[i] = Msg(type::NONE);
                }
            }
            int *v = new int;
            *v = progress;
            push(Msg(type::Seek, v));
        }

        Msg &pull_not_null() {
            if (empty()) {
                push(type::Pause);
            }
            return front();
        }
    };
}

#endif //VIDEOUSE_MSGQUEUE_H
