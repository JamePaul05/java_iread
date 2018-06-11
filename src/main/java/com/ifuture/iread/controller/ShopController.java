package com.ifuture.iread.controller;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.service.IShopService;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import com.ifuture.iread.util.DataTableUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by maofn on 2017/3/19.
 */
@Controller
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private IShopService shopService;

    @Autowired
    private ICityService cityService;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "main.shop.list";
    }


    @RequestMapping("/fetchShops")
    @ResponseBody
    public String fetchShops(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = shopService.fetchShops(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Shop shop, Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "main.shop.create";
    }

    @RequestMapping(value = "/save")
    public String save(@Valid Shop shop, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            shopService.saveOrUpdate(shop);
        }
        if (!flag) {
            model.addAttribute("cities", cityService.findAll());
            if ("create".equalsIgnoreCase(type)) {
                return "main.shop.create";
            } else {
                return "main.shop.edit";
            }
        }
        return "redirect:/admin/shop/list";
    }


    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = shopService.remove(ids);
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
        Shop shop = shopService.findOneById(id);
        City localCity = shop.getCity();
        List<City> cities = cityService.findAll();
        cities.remove(localCity);
        cities.add(0, localCity);
        model.addAttribute("shop", shop);
        model.addAttribute("cities", cities);
        return "main.shop.edit";
    }

    @RequestMapping("/findByCity")
    @ResponseBody
    public String findByCity(@RequestParam(value = "id") String cityID, Model model) {
        List<Shop> shops = shopService.findByCity(cityID);
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();
        for (Shop shop : shops) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("shopID", shop.getId());
            jsonObject.put("shopName", shop.getShopName());
            ja.put(jsonObject);
        }
        json.put("shops", ja);
        return json.toString();
    }

}
