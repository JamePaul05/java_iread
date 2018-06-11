package com.ifuture.iread.controller;

import com.ifuture.iread.entity.*;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.service.IRoleService;
import com.ifuture.iread.service.IShopService;
import com.ifuture.iread.service.IUserService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by maofn on 2017/3/17.
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/list")
    public String listUsers() {
        return "main.user.list";
    }

    @RequestMapping("fetchUsers")
    @ResponseBody
    public String fetchUsers(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = userService.fetchUsers(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create")
    public String create(User user, Model model) {
        User login = SecurityUtil.getUser(userRepository);
        List<Role> roles = login.getRoles();
        if (roles != null) {
            if (roles.size() == 1) {
                //如果是系统管理员
                if (roles.get(0).getCode().equals(Constants.ROLE_ADMIN)) {
                    model.addAttribute("roles", roleService.findAll());
                    model.addAttribute("cities", cityService.findAll());
                } else {
                    //设置roles集合（只有工作人员）
                    List<Role> rolesTemp = new ArrayList<>();
                    rolesTemp.add(roleService.findOneByCode(Constants.ROLE_STAFF));
                    model.addAttribute("roles", rolesTemp);
                    String[] roleIDs = new String[roles.size()];
                    for (int i = 0; i < roles.size(); i++) {
                        roleIDs[i] = rolesTemp.get(i).getId();
                    }
                    user.setRoleIDs(roleIDs);
                    //设置shops集合（仅限本人公益点）
                    List<Shop> shops = new ArrayList<>();
                    shops.add(login.getShop());
                    model.addAttribute("shops", shops);
                    user.setShopID(login.getShop().getId());
                }
                model.addAttribute("userType", roles.get(0).getCode());
            } else if (roles.size() == 2) {

            }
        }
        return "main.user.create";
    }

    @RequestMapping(value = "/save")
    public String save(@Valid User user, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if ("系统管理员".equalsIgnoreCase(user.getUserName())) {
            result.addError(new FieldError("userName", "不能更改管理员信息！", ""));
        }
        //只有是编辑界面的密码非空校验出错时可以继续保存
        if (result.hasErrors()) {
            //如果当前是编辑页面的save
            if ("edit".equalsIgnoreCase(type)) {
                //如果错误数量只有1
                if (result.getErrorCount() == 1) {
                    //如果是密码的非空校验
                    if (result.getFieldError("passWord") != null) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }
        }
        try {
            if (flag) {
                userService.saveOrUpdate(user);
            }
        } catch (Exception e) {
            if(e.getLocalizedMessage().contains(Constants.EXCEPTION_CONSTRAINTVIOLATIONEXCEPTION)){
                flag=false;
                result.addError(new FieldError("user","userName", "用户名不能重复"));
            }else{
                System.out.println(e.getLocalizedMessage());
            }
        }

        if (!flag) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("cities", cityService.findAll());
            if ("create".equalsIgnoreCase(type)) {
                return "main.user.create";
            } else {
                return "main.user.edit";
            }
        }
        return "redirect:/admin/user/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = userService.remove(ids);
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
        User user = userService.findOneById(id);
        List<Role> roles = user.getRoles();
        String[] roleIDs = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleIDs[i] = roles.get(i).getId();
        }
        user.setRoleIDs(roleIDs);

        User login = SecurityUtil.getUser(userRepository);
        List<Role> loginRoles = login.getRoles();
        if (loginRoles != null) {
            if (loginRoles.size() == 1) {
                //如果是公益点管理员
                if (loginRoles.get(0).getCode().equals(Constants.ROLE_ADMIN)) {
                    List<City> cities = cityService.findAll();
                    Shop shop = user.getShop();
                    if (shop != null) {
                        City localCity = shop.getCity();
                        cities.remove(localCity);
                        cities.add(0, localCity);
                        user.setShopID(shop.getId());
                    }
                    model.addAttribute("roles", roleService.findAll());
                    model.addAttribute("cities", cities);

                } else {
                    model.addAttribute("roles", roles);
                    //设置shops集合（仅限本人公益点）
                    List<Shop> shops = new ArrayList<>();
                    shops.add(login.getShop());
                    model.addAttribute("shops", shops);
                    user.setShopID(login.getShop().getId());
                }
                model.addAttribute("userType", loginRoles.get(0).getCode());
            } else {
                //TODO
            }
        }
        model.addAttribute("user", user);

        return "main.user.edit";
    }

    @RequestMapping(value = "/changePsd",produces="text/html;charset=UTF-8",method =RequestMethod.POST)
    @ResponseBody
    public String changePsd(PasswordModel passwordModel){
        String reason =userService.changePsd(passwordModel);
        return reason;
    }
}
