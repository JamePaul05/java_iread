package com.ifuture.iread.controller;

import com.ifuture.iread.entity.MemberLevel;
import com.ifuture.iread.service.IMemberLevelService;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import com.ifuture.iread.util.DataTableUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by maofn on 2017/3/23.
 */
@Controller
@RequestMapping("/admin/memberLevel")
public class MemberLevelController {

    @Autowired
    private IMemberLevelService memberLevelService;

    @RequestMapping("/list")
    public String list() {
        return "main.memberLevel.list";
    }

    @RequestMapping("/fetchMemberLevels")
    @ResponseBody
    public String fetchCities(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = memberLevelService.fetchMemberLevels(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(MemberLevel memberLevel, Model model) {
        return "main.memberLevel.create";
    }

    @RequestMapping(value = "/save")
    public String save(@Valid MemberLevel memberLevel, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            memberLevelService.saveOrUpdate(memberLevel);
        }
        if (!flag) {
            if ("create".equalsIgnoreCase(type)) {
                return "main.memberLevel.create";
            } else {
                return "main.memberLevel.edit";
            }

        }
        return "redirect:/admin/memberLevel/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = memberLevelService.remove(ids);
        json.put("flag", flag);
        if (flag) {
            json.put("msg", "删除成功");
        } else {
            json.put("msg", "删除失败");
        }
        return json.toString();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        MemberLevel memberLevel = memberLevelService.findOneById(id);
        model.addAttribute("memberLevel", memberLevel);
        return "main.memberLevel.edit";
    }
}
