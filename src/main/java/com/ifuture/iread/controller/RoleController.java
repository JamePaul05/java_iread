package com.ifuture.iread.controller;

import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.service.IRoleService;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import com.ifuture.iread.util.DataTableUtil;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Created by maofn on 2017/3/21.
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/list")
    public String list() {
        return "main.role.list";
    }

    @RequestMapping("/fetchRoles")
    @ResponseBody
    public String fetchRoles(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = roleService.fetchRoles(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Role role, Model model) {
        return "main.role.create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save(@Valid Role role, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            roleService.saveOrUpdate(role);
        }
        if (!flag) {
            if ("create".equalsIgnoreCase(type)) {
                return "main.role.create";
            } else {
                return "main.role.edit";
            }
        }
        return "redirect:/admin/role/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = roleService.remove(ids);
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
        Role role = roleService.findOneById(id);
        model.addAttribute("role", role);
        return "main.role.edit";
    }
}
