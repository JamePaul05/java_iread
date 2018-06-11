package com.ifuture.iread.controller;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.service.IBookletService;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.service.IShopService;
import com.ifuture.iread.util.StringUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maofn on 2017/5/25.
 */
@Controller
@RequestMapping("/admin/index")
public class IndexController {

    @Autowired
    private IShopService shopService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IBookletService bookletService;

    @RequestMapping("/list")
    public String list() {
        return "main.index.list";
    }

    @RequestMapping("/getShopsByCity")
    @ResponseBody
    public String getShopsByCity() {
        JSONObject jsonObject = new JSONObject();
        List<City> cities = cityService.findAll();
        List<String> cityNames = new ArrayList<>();
        List<String> shops = new ArrayList<>();
        if (cities.size() > 0) {
            for (City city : cities) {
                cityNames.add(city.getCityName());
                shops.add(String.valueOf(shopService.findByCity(city.getId()).size()));
            }
        }
        jsonObject.put("cityNames", cityNames.toArray());
        jsonObject.put("shops", shops.toArray());
        return jsonObject.toString();
    }

    @RequestMapping("/getBookletsChart")
    @ResponseBody
    public String getBookletsChart() {
        JSONObject jsonObject = new JSONObject();
        List<Shop> shops = shopService.findAll();
        List<String> shopNames = new ArrayList<>();
        List<Integer> bookletTotal = new ArrayList<>();
        List<Integer> borrowedTotal = new ArrayList<>();
        for (Shop shop : shops) {
            shopNames.add(shop.getShopName());
            int total = bookletService.countByShop(shop);
            bookletTotal.add(total);
            int borrowed = bookletService.countBorrowedByShop(shop);
            borrowedTotal.add(borrowed);
        }
        jsonObject.put("shopNames", shopNames.toArray());
        jsonObject.put("bookletTotal", bookletTotal.toArray());
        jsonObject.put("borrowedTotal", borrowedTotal.toArray());
        return jsonObject.toString();
    }
}
