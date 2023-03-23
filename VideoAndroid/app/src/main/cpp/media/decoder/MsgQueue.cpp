//
// Created by lifengqiang on 2022/9/12.
//

#include "MsgQueue.h"

namespace msg {
    Msg::~Msg() {
        // if (data != nullptr) free(data);
    }

    Msg::Msg() {
        type = msg::type::Goon;
        data = nullptr;
    }

    Msg::Msg(int type) {
        this->type = type;
        data = nullptr;
    }

    Msg::Msg(int type, void *data) {
        this->type = type;
        this->data = data;
    }

    int Msg::getType() const { return type; }

    void *Msg::getData() { return data; }

    MsgQueue::MsgQueue(int maxQueue) {
        count = maxQueue;
        count++;
        head = 0;
        end = 0;
        queue = new Msg[maxQueue];
    }

    bool MsgQueue::empty() const {
        return head == end;
    }

    void MsgQueue::push(const Msg &msg) {
        queue[end++] = msg;
        if (end == count) {
            end = 0;
        }
        if (head == end) {
            ++head;
        }
        if (head == count) {
            head = 0;
        }
    }

    int MsgQueue::size() const {
        if (empty()) {
            return 0;
        } else {
            if (end > head) {
                return end - head;
            } else {
                return count - head + end;
            }
        }
    }

    void MsgQueue::pop() {
        if (++head == count) {
            head = 0;
        }
    }

    Msg &MsgQueue::front() const {
        return queue[head];
    }

    Msg &MsgQueue::back() const {
        if (end == 0) {
            return queue[count - 1];
        }
        return queue[end - 1];
    }

    void MsgQueue::clear() {
        head = end = 0;
    }
}