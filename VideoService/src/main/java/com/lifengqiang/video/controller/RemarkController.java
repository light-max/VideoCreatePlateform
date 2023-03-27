package com.lifengqiang.video.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.mapper.RemarkMapper;
import com.lifengqiang.video.model.data.Pager;
import com.lifengqiang.video.model.entity.Remark;
import com.lifengqiang.video.service.RemarkService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import com.lifengqiang.video.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller("remark.admin")
public class RemarkController {
    @Resource
    RemarkService service;

    @Resource
    RemarkMapper mapper;

    @GetMapping({"/admin/remark", "/admin/remark/list", "/admin/remark/list/{n}"})
    @ViewModelParameter(key = "view", value = "remark")
    public String list(Model model, @PathVariable(required = false) Integer n, @RequestParam(required = false) Integer worksId) {
        Page<Remark> page;
        if (worksId == null) {
            page = mapper.selectPage(new Page<>(n == null ? 1 : n, 10), null);
        } else {
            LambdaQueryWrapper<Remark> wrapper = new QueryWrapper<Remark>()
                    .lambda()
                    .eq(Remark::getWorksId, worksId);
            page = mapper.selectPage(new Page<>(n == null ? 1 : n, 10), wrapper);
        }
        Pager pager = new Pager(page, "/admin/remark/list");
        if (worksId != null) {
            pager.setTailAppend("?worksId=" + worksId);
        }
        List<Remark> list = page.getRecords();
        model.addAttribute("pager", pager);
        model.addAttribute("list", list);
        model.addAttribute("w", worksId);
        return "/admin/remark/list";
    }

    /**
     * 随便删除一下算了
     */
    @DeleteMapping("/admin/remark/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteRemark(@PathVariable Integer id) {
        mapper.deleteById(id);
    }
}
