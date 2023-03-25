package com.lifengqiang.video.data.result;

import java.util.List;

public class Works {
    private int id;
    private int userId;
    private int type;//0图片 1视频
    private String content;
    private long createTime;
    private String date;
    private String username;
    private String nickname;
    private String userhead;
    private String cover;
    private String video;
    private List<String> images;
    private boolean follwuser;
    private boolean like;
    private int likeCount;
    private int remarkCount;
    private boolean collect;
    private int collectCount;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isFollwuser() {
        return follwuser;
    }

    public void setFollwuser(boolean follwuser) {
        this.follwuser = follwuser;
    }

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

    public int getRemarkCount() {
        return remarkCount;
    }

    public void setRemarkCount(int remarkCount) {
        this.remarkCount = remarkCount;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    @Override
    public String toString() {
        return "Works{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", date='" + date + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userhead='" + userhead + '\'' +
                ", cover='" + cover + '\'' +
                ", video='" + video + '\'' +
                ", images=" + images +
                ", follwuser=" + follwuser +
                ", like=" + like +
                ", likeCount=" + likeCount +
                ", remarkCount=" + remarkCount +
                ", collect=" + collect +
                ", collectCount=" + collectCount +
                '}';
    }
}
