package com.lifengqiang.video.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.model.data.Pager;
import com.lifengqiang.video.model.data.PagerData;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.Remark;
import com.lifengqiang.video.model.entity.Reply;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.RemarkResult;
import com.lifengqiang.video.model.result.ReplyResult;
import com.lifengqiang.video.service.RemarkService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class RemarkController {
    @Resource
    RemarkService service;

    @PostMapping("/user/remark")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void sendRemark(@SessionAttribute("user") User user, Integer worksId, String content) {
        service.addRemark(user.getId(), worksId, content);
    }

    @PostMapping("/user/reply")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void sendReply(@SessionAttribute("user") User user, Integer remarkId, String content,
                          @RequestParam(required = false, defaultValue = "0") Integer replyId) {
        service.addReply(user.getId(), remarkId, content, replyId);
    }

    /**
     * 因为时间关系，直接返回1000条数据，暂时不做分页
     */
    @GetMapping({"/user/remark/list/{n}", "/remark/list/{n}"})
    @ResponseBody
    public Result<PagerData> getRemarkList(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable(required = false) Integer n,
            @RequestParam Integer worksId
    ) {
        Page<Remark> page = service.getRemarkPage(worksId, n);
        Pager pager = new Pager(page, "/user/remark/list");
        List<Remark> list = page.getRecords();
        int currentUserId = user == null ? 0 : user.getId();
        List<RemarkResult> results = service.getRemarkResultList(currentUserId, list);
        return Result.pager(pager, results);
    }

    @GetMapping({"/user/reply/list/{n}", "/reply/list/{n}"})
    @ResponseBody
    public Result<PagerData> getReplyList(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable(required = false) Integer n,
            @RequestParam Integer remarkId
    ) {
        Page<Reply> page = service.getReplyPage(remarkId, n);
        Pager pager = new Pager(page, "/user/reply/list");
        List<Reply> list = page.getRecords();
        int currentUserId = user == null ? 0 : user.getId();
        List<ReplyResult> results = service.getReplyResultList(currentUserId, list);
        return Result.pager(pager, results);
    }

    @GetMapping({"/user/remark/{id}", "/remark/{id}"})
    @ResponseBody
    public Result<RemarkResult> getRemark(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer id
    ) {
        int currentUserId = user != null ? user.getId() : 0;
        Remark remark = service.getRemarkById(id);
        return Result.success(service.getRemarkResult(currentUserId, remark));
    }

}
