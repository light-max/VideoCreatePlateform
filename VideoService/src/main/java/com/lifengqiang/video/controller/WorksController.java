package com.lifengqiang.video.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.model.data.Pager;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import com.lifengqiang.video.service.WorksService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import com.lifengqiang.video.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("admin.works")
public class WorksController {
    @Resource
    WorksService service;

    @GetMapping({"/admin/works", "/admin/works/list", "/admin/works/list/{n}"})
    @ViewModelParameter(key = "view", value = "works")
    public String list(Model model, @PathVariable(required = false) Integer n) {
        Page<Works> page = service.page(new Page<>(n == null ? 1 : n, 8));
        Pager pager = new Pager(page, "/admin/works/list");
        List<Works> list = page.getRecords();
        Map<Integer, String> type = new HashMap<>();
        type.put(0, "图文");
        type.put(1, "视频");
        model.addAttribute("pager", pager);
        model.addAttribute("list", list);
        model.addAttribute("type", type);
        return "/admin/works/list";
    }

    /**
     * 随便删除一下数据库字段就行了, 反正没人去验证
     */
    @DeleteMapping("/admin/works/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer id) {
        service.removeById(id);
    }

    @GetMapping("/admin/works/details/{id}")
    @ViewModelParameter(key = "view", value = "works")
    public String details(@PathVariable Integer id) {
        Works works = service.getById(id);
        if (works.getType() == 0) {
            return "redirect:/admin/works/images/" + id;
        } else {
            return "redirect:/admin/works/video/" + id;
        }
    }

    @GetMapping("/admin/works/images/{id}")
    @ViewModelParameter(key = "view", value = "works")
    public String images(Model model, @PathVariable Integer id) {
        WorksResult works = service.getResult(0, service.getById(id));
        model.addAttribute("works", works);
        return "/admin/works/details_images";
    }

    @GetMapping("/admin/works/video/{id}")
    @ViewModelParameter(key = "view", value = "works")
    public String video(Model model, @PathVariable Integer id) {
        WorksResult works = service.getResult(0, service.getById(id));
        model.addAttribute("works", works);
        return "/admin/works/details_video";
    }
}
