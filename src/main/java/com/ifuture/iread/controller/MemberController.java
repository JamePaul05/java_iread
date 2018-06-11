package com.ifuture.iread.controller;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.service.IMemberLevelService;
import com.ifuture.iread.service.IMemberService;
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
@RequestMapping("/admin/member")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IMemberLevelService memberLevelService;

    @RequestMapping("/list")
    public String list() {
        return "main.member.list";
    }

    @RequestMapping("/fetchMembers")
    @ResponseBody
    public String fetchCities(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = memberService.fetchMembers(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Member member, Model model) {
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("memberLevels", memberLevelService.findAll());
        return "main.member.create";
    }

    @RequestMapping(value = "/save")
    public String save(@Valid Member member, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            memberService.saveOrUpdate(member);
        }
        if (!flag) {
            model.addAttribute("cities", cityService.findAll());
            model.addAttribute("memberLevels", memberLevelService.findAll());
            if ("create".equalsIgnoreCase(type)) {
                return "main.member.create";
            } else {
                return "main.member.edit";
            }

        }
        return "redirect:/admin/member/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = memberService.remove(ids);
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
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("memberLevels", memberLevelService.findAll());
        Member member = memberService.findOneById(id);
        member.setCityID(member.getCity().getId());
        member.setMemberLevelID(member.getMemberLevel().getId());
        model.addAttribute("member", member);
        return "main.member.edit";
    }
    
    
}
