class net {
}

net.ajax = function ({url, method, data, json, error, success, toast = false, reload = false}) {
    var settings = {
        "url": url,
        "method": method,
        "timeout": 0,
        "dataType": "json"
    };

    if (data != null) {
        var form = new FormData();
        Object.keys(data).forEach((key) => {
            form.append(key, data[key]);
        });
        settings["processData"] = false;
        settings["mimeType"] = "multipart/form-data";
        settings["contentType"] = false;
        settings["data"] = form;
    } else if (json != null) {
        settings["headers"] = {
            "Content-Type": "application/json"
        };
        if (typeof (json) == 'string') {
            settings["data"] = json;
        } else {
            settings["data"] = JSON.stringify(json);
        }
    }
    $.ajax(settings).done(function (result) {
        if (result.success) {
            if (success != null) {
                success(result.data);
            }
            if (reload) {
                window.location.reload();
            }
        } else {
            if (error != null) {
                error(result.message);
            }
            if (toast) {
                alert(result.message);
            }
        }
    });
}

net.get = function ({url, error, success, toast = false, reload = false}) {
    net.ajax({
        url: url,
        method: "get",
        error: error,
        success: success,
        toast: toast,
        reload: reload
    });
}

net.post = function ({url, data, json, error, success, toast = false, reload = false}) {
    net.ajax({
        url: url,
        method: "post",
        data: data,
        json: json,
        error: error,
        success: success,
        toast: toast,
        reload: reload
    });
}

net.put = function ({url, data, json, error, success, toast = false, reload = false}) {
    net.ajax({
        url: url,
        method: "put",
        data: data,
        json: json,
        error: error,
        success: success,
        toast: toast,
        reload: reload
    });
}

net.delete = function ({url, data, json, error, success, toast = false, reload = false}) {
    net.ajax({
        url: url,
        method: "delete",
        data: data,
        json: json,
        error: error,
        success: success,
        toast: toast,
        reload: reload
    });
}