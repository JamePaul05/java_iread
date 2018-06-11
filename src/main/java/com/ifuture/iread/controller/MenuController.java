package com.ifuture.iread.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.service.IMenuService;
import com.ifuture.iread.service.IRoleService;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import com.ifuture.iread.util.DataTableUtil;
import com.ifuture.iread.util.Json;
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
 * Created by maofn on 2017/3/16.
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/showMenus")
    @ResponseBody
    public Json showMenus() {
        Json j = new Json();
        Set<Menu> menus = menuService.showMenus();
//        JSON.toJSONString(menus,SerializerFeature.DisableCircularReferenceDetect);
        j.setObj(menus);
        return j;
    }

    @RequestMapping("/list")
    public String list() {
        return "main.menu.list";
    }

    @RequestMapping("/fetchMenus")
    @ResponseBody
    public String fetchMenus(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = menuService.fetchMenus(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    /**
     *
     * 和springform的commandName绑定了参数，所以必须传一个menu到前台
     */
    @RequestMapping(value = "/create")
    public String create(Menu menu, Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "main.menu.create";
    }

    @RequestMapping(value = "/save")
    public String save(@Valid Menu menu, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            menuService.saveOrUpdate(menu);
        }
        if (!flag) {
            model.addAttribute("roles", roleService.findAll());
            if ("create".equalsIgnoreCase(type)) {
                return "main.menu.create";
            } else {
                return "main.menu.edit";
            }
        }
        return "redirect:/admin/menu/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = menuService.remove(ids);
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
        Menu menu = menuService.findOneById(id);
        List<Role> roles = menu.getRoles();
        String[] roleIDs = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleIDs[i] = roles.get(i).getId();
        }
        menu.setRoleIDS(roleIDs);
        model.addAttribute("menu", menu);
        model.addAttribute("roles", roleService.findAll());
        return "main.menu.edit";
    }

}
