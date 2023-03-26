package com.lifengqiang.video.data.result;

public class Message {
    private int id;
    private String relationKey;
    private int sendId;
    private int receiveId;
    private String content;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", relationKey='" + relationKey + '\'' +
                ", sendId=" + sendId +
                ", receiveId=" + receiveId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
