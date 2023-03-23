package com.lifengqiang.video.data.result;

public class Remark {
    private int id;
    private int userId;
    private int worksId;
    private String content;
    private long createTime;
    private String createDateTime;
    private String username;
    private String userNickname;
    private String userHead;
    private boolean me;
    private boolean like;
    private int likeCount;
    private int replyCount;

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWorksId() {
        return worksId;
    }

    public void setWorksId(int worksId) {
        this.worksId = worksId;
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

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    @Override
    public String toString() {
        return "Remark{" +
                "id=" + id +
                ", userId=" + userId +
                ", worksId=" + worksId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", createDateTime='" + createDateTime + '\'' +
                ", username='" + username + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userHead='" + userHead + '\'' +
                ", me=" + me +
                ", like=" + like +
                ", likeCount=" + likeCount +
                ", replyCount=" + replyCount +
                '}';
    }
}
