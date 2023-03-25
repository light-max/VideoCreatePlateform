package com.lifengqiang.video.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.SearchValue;
import com.lifengqiang.video.model.result.WorksResult;
import com.lifengqiang.video.service.UserService;
import com.lifengqiang.video.service.WorksService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Resource
    UserService userService;

    @Resource
    WorksService worksService;

    @GetMapping("/search/user/all/list")
    @ResponseBody
    public Result<List<SearchValue>> searchUser(String w) {
        if (w.trim().isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        List<User> users = userService.list(new QueryWrapper<User>()
                .lambda()
                .like(User::getUsername, w)
                .or()
                .like(User::getNickname, w));
        List<SearchValue> values = new ArrayList<>();
        for (User user : users) {
            values.add(SearchValue.builder()
                    .id(user.getId())
                    .primary(user.getNickname())
                    .cover("/head/" + user.getId())
                    .build());
        }
        return Result.success(values);
    }

    @GetMapping("/search/works/all/list")
    @ResponseBody
    public Result<List<WorksResult>> searchWorks(String w) {
        if (w.trim().isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        List<Works> works = worksService.list(new QueryWrapper<Works>()
                .lambda()
                .like(Works::getContent, w));
        List<WorksResult> results = worksService.getResultList(0, works);
        return Result.success(results);
    }
}
