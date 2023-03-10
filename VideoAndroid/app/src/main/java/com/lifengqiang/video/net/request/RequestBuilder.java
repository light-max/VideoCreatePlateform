package com.lifengqiang.video.net.request;

import androidx.annotation.Nullable;

import com.lifengqiang.video.async.Async;
import com.lifengqiang.video.async.AsyncTaskError;
import com.lifengqiang.video.net.DefaultOkHttpClient;
import com.lifengqiang.video.net.ResultError;
import com.lifengqiang.video.net.result.Result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestBuilder implements Serializable {
    private static final boolean debug = true;

    private String url;
    private String method;
    private boolean useStream = false;
    private RequestData data;

    private final Map<String, Object> log = debug ? new HashMap<>() : null;

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder method(@Method String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder useStream() {
        this.useStream = true;
        return this;
    }

    private RequestData getDataNotNull() {
        if (data == null) {
            data = new RequestData();
        }
        return data;
    }

    public RequestBuilder path(String key, @Nullable Object value) {
        getDataNotNull().setPath(key, value);
        return this;
    }

    public RequestBuilder param(String key, @Nullable Object value) {
        getDataNotNull().setParam(key, value);
        return this;
    }

    public RequestBuilder header(String key, String value) {
        getDataNotNull().setHeader(key, value);
        return this;
    }

    public RequestBuilder bodyType(@BodyType String bodyType) {
        getDataNotNull().setType(bodyType);
        return this;
    }

    public RequestBuilder form() {
        return bodyType(BodyType.FORM);
    }

    public RequestBuilder urlencoded() {
        return bodyType(BodyType.URLENCODED);
    }

    public RequestBuilder raw() {
        return bodyType(BodyType.RAW);
    }

    public RequestBuilder field(String key, Object value) {
        getDataNotNull().setField(key, value);
        return this;
    }

    public RequestBuilder json(Object object) {
        getDataNotNull().setJsonObject(object);
        return this;
    }

    private String buildUrl() {
        // ????????????
        StringBuilder pathBuilder = new StringBuilder();
        // ?????????????????????
        if (data != null && data.paths != null) {
            // ???????????????/?????????
            String[] split = url.split("/");
            // ?????????????????????????????????
            for (String s : split) {
                // ???????????????????????????????????????????????????????????????key??????data.path??????????????????????????????
                if (s.contains("{") && s.contains("}") && s.indexOf("{") < s.indexOf("}")) {
                    String key = s.substring(1, s.length() - 1);
                    Object value = data.paths.get(key);
                    // ??????value?????????
                    if (value != null) {
                        pathBuilder.append("/").append(value);
                    }
                } else if (!s.isEmpty()) {
                    // ????????????????????????
                    pathBuilder.append("/").append(s);
                }
            }
        } else {
            // ???????????????{}??????
            String replaceAll = url.replaceAll("\\{+[a-zA-Z0-9]+\\}", "");
            pathBuilder.append(replaceAll);
        }
        // ????????????
        if (data != null && data.params != null) {
            boolean first = true;
            for (Map.Entry<String, Object> entry : data.params.entrySet()) {
                if (entry.getValue() != null) {
                    if (first) {
                        pathBuilder.append("?");
                        first = false;
                    } else {
                        pathBuilder.append("&");
                    }
                    pathBuilder.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue());
                }
            }

        }
        return pathBuilder.toString();
    }

    private RequestBody buildBody() {
        if (Method.GET.equals(method)) {
            return null;
        } else {
            if (data != null) {
                if (BodyType.RAW.equals(data.type)) {
                    return RequestData.buildRaw(data);
                } else if (data.field != null) {
                    if (BodyType.FORM.equals(data.type)) {
                        return RequestData.buildDataForm(data);
                    } else if (BodyType.URLENCODED.equals(data.type)) {
                        return RequestData.buildUrlEncoded(data);
                    }
                }
            }
            return RequestData.buildEmptyBody();
        }
    }

    protected Request buildRequest() {
        String httpUrl = modifyBuildUrl(buildUrl());
        RequestBody requestBody = buildBody();
        return new Request.Builder()
                .url(httpUrl)
                .method(method, requestBody)
                .build();
    }

    private void setLogInfo(Request request) {
        log.put("url", request.url().toString());
        log.put("method", request.method());
        if (data != null) {
            log.put("bodyData", data.toString());
        }
    }

    protected String modifyBuildUrl(String url) {
        return url;
    }

    public Result execute() {
        try {
            Request request = buildRequest();
            if (debug) {
                setLogInfo(request);
            }
            Response response = DefaultOkHttpClient.getClient()
                    .newCall(request)
                    .execute();
            Result result = Result.response(response);
            if (!useStream) {
                result.parseContent();
            }
            return result;
        } catch (Exception e) {
            if (debug) {
                System.out.println(log);
            }
            return Result.exception(e);
        }
    }

    public Async.Builder<?> async() {
        return Async.<Result>builder().task(() -> {
            Result result = execute();
            ResultError error = result.error();
            if (error == null) {
                return result;
            }
            return new AsyncTaskError(error.getMessage(), error.getException());
        });
    }

    public <T extends RequestBuilder> T as() {
        return (T) this;
    }

    @Override
    public String toString() {
        return "RequestBuilder{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", useStream=" + useStream +
                ", data=" + data +
                ", log=" + log +
                '}';
    }
}
