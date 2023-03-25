package com.lifengqiang.video.api;

import com.lifengqiang.video.async.Async;
import com.lifengqiang.video.data.PagerData;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.data.request.NewUserInfo;
import com.lifengqiang.video.data.result.CollectState;
import com.lifengqiang.video.data.result.FollowState;
import com.lifengqiang.video.data.result.ISFriend;
import com.lifengqiang.video.data.result.LikeState;
import com.lifengqiang.video.data.result.Remark;
import com.lifengqiang.video.data.result.Reply;
import com.lifengqiang.video.data.result.SearchValue;
import com.lifengqiang.video.data.result.User;
import com.lifengqiang.video.data.result.Works;

import java.io.File;
import java.util.List;

public class Api {
    public static Async.Builder<User> login(String username, String password) {
        return ExRequestBuilder.post("/user/login")
                .form()
                .field("username", username)
                .field("password", password)
                .<ExRequestBuilder>as()
                .async(User.class)
                .success(UserDetailsData::post);
    }

    public static Async.Builder<?> logout() {
        return ExRequestBuilder.post("/user/logout").async();
    }

    public static Async.Builder<?> register(String username, String password) {
        return ExRequestBuilder.post("/user/register")
                .form()
                .field("username", username)
                .field("password", password)
                .async();
    }

    public static Async.Builder<User> getUserDetails() {
        return ExRequestBuilder.get("/user/details")
                .async(User.class)
                .success(UserDetailsData::post);
    }

    public static Async.Builder<User> setUserInfo(NewUserInfo userInfo) {
        return ExRequestBuilder.put("/user/info")
                .raw()
                .json(userInfo)
                .<ExRequestBuilder>as()
                .async(User.class)
                .success(UserDetailsData::post);
    }

    public static Async.Builder<User> getUserDetails(int userId) {
        return ExRequestBuilder.get("/api/user/details/{id}")
                .path("id", userId)
                .<ExRequestBuilder>as()
                .async(User.class);
    }

    public static Async.Builder<?> setHead(File file) {
        return ExRequestBuilder.post("/user/head")
                .form()
                .field("file", file)
                .async();
    }

    public static Async.Builder<?> setPassword(String source, String password) {
        return ExRequestBuilder.post("/user/password")
                .form()
                .field("source", source)
                .field("password", password)
                .async();
    }

    public static Async.Builder<?> submitImages(String content, List<File> files) {
        return ExRequestBuilder.post("/user/works/images")
                .form()
                .field("content", content)
                .field("files", files)
                .<ExRequestBuilder>as()
                .async();
    }

    public static Async.Builder<?> submitVideo(String content, File file, File cover) {
        return ExRequestBuilder.post("/user/works/video")
                .form()
                .field("content", content)
                .field("file", file)
                .field("cover", cover)
                .<ExRequestBuilder>as()
                .async();
    }

    public static Async.Builder<?> resetRandomWorksIds() {
        return ExRequestBuilder.get("/works/recommend/random/ids/reset")
                .async();
    }

    public static Async.Builder<List<Integer>> getRandomWorksIds() {
        return ExRequestBuilder.get("/works/recommend/random/ids")
                .asyncList(Integer.class);
    }

    public static Async.Builder<List<Integer>> getFollowWorksIds() {
        return ExRequestBuilder.get("/user/works/id/follow/all/list")
                .asyncList(Integer.class);
    }

    public static Async.Builder<List<Works>> getFriendWorksList() {
        return ExRequestBuilder.get("/user/works/friend/all/list")
                .asyncList(Works.class);
    }

    public static Async.Builder<List<Works>> getUserWorksList(int userId) {
        return ExRequestBuilder.get("/api/works/byuserid")
                .param("userId", userId)
                .<ExRequestBuilder>as()
                .asyncList(Works.class);
    }

    public static Async.Builder<Works> getWorks(int worksId) {
        return ExRequestBuilder.get("/works/details/{id}")
                .path("id", worksId)
                .<ExRequestBuilder>as()
                .async(Works.class);
    }

    public static Async.Builder<LikeState> likeWorks(int worksId) {
        return ExRequestBuilder.post("/user/like/works/{worksId}")
                .path("worksId", worksId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    public static Async.Builder<LikeState> likeRemark(int remarkId) {
        return ExRequestBuilder.post("/user/like/remark/{remarkId}")
                .path("remarkId", remarkId)
                .<ExRequestBuilder>as()
                .async(LikeState.class);
    }

    public static Async.Builder<CollectState> collect(int worksId) {
        return ExRequestBuilder.post("/user/collect/{worksId}")
                .path("worksId", worksId)
                .<ExRequestBuilder>as()
                .async(CollectState.class);

    }

    public static Async.Builder<?> sendRemark(int worksId, String content) {
        return ExRequestBuilder.post("/user/remark")
                .form()
                .field("worksId", worksId)
                .field("content", content)
                .async();
    }

    public static Async.Builder<?> sendReply(int remarkId, String content, int replyId) {
        return ExRequestBuilder.post("/user/reply")
                .form()
                .field("remarkId", remarkId)
                .field("content", content)
                .field("replyId", replyId)
                .async();

    }

    /**
     * 不做分页了，直接获取1000条数据
     */
    public static Async.Builder<PagerData<Remark>> getRemarkList(int worksId) {
        return ExRequestBuilder.get("/user/remark/list/1")
                .param("worksId", worksId)
                .<ExRequestBuilder>as()
                .asyncPager(Remark.class);
    }

    public static Async.Builder<PagerData<Reply>> getReplyList(int remarkId) {
        return ExRequestBuilder.get("/user/reply/list/1")
                .param("remarkId", remarkId)
                .<ExRequestBuilder>as()
                .asyncPager(Reply.class);
    }

    public static Async.Builder<Remark> getRemark(int remarkId) {
        return ExRequestBuilder.get("/user/remark/{remarkId}")
                .path("remarkId", remarkId)
                .<ExRequestBuilder>as()
                .async(Remark.class);
    }

    public static Async.Builder<FollowState> isFollow(int targetId) {
        return ExRequestBuilder.get("/user/follow/{targetId}")
                .path("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(FollowState.class);
    }

    public static Async.Builder<FollowState> follow(int targetId) {
        return ExRequestBuilder.post("/user/follow/{targetId}")
                .path("targetId", targetId)
                .<ExRequestBuilder>as()
                .async(FollowState.class);
    }

    public static Async.Builder<List<SearchValue>> searchUsers(String w) {
        return ExRequestBuilder.get("/search/user/all/list")
                .param("w", w)
                .<ExRequestBuilder>as()
                .asyncList(SearchValue.class);
    }

    public static Async.Builder<List<Works>> searchWorks(String w) {
        return ExRequestBuilder.get("/search/works/all/list")
                .param("w", w)
                .<ExRequestBuilder>as()
                .asyncList(Works.class);
    }

    public static Async.Builder<ISFriend> isFriend(int targetId) {
        return ExRequestBuilder.get("/user/friend/{id}")
                .path("id", targetId)
                .<ExRequestBuilder>as()
                .async(ISFriend.class);
    }
}
