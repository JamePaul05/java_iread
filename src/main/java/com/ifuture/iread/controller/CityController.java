package com.ifuture.iread.controller;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import com.ifuture.iread.util.DataTableUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

/**
 * Created by maofn on 2017/3/20.
 */
@Controller
@RequestMapping("/admin/city")
public class CityController {

    Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private ICityService cityService;

    @RequestMapping("/list")
    public String list() {
        return "main.city.list";
    }

    @RequestMapping("/fetchCities")
    @ResponseBody
    public String fetchCities(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = cityService.fetchCities(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(City city, Model model) {
        return "main.city.create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid City city, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            cityService.saveOrUpdate(city);
        }
        if (!flag) {
            if ("create".equalsIgnoreCase(type)) {
                return "main.city.create";
            } else {
                return "main.city.edit";
            }

        }
        return "redirect:/admin/city/list";
    }
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save1(@Valid City city, BindingResult result, @RequestParam("type")String type, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            cityService.saveOrUpdate(city);
        }
        if (!flag) {
            if ("create".equalsIgnoreCase(type)) {
                return "main.city.create";
            } else {
                return "main.city.edit";
            }

        }
        return "redirect:/admin/city/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = cityService.remove(ids);
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
        City city = cityService.findOneById(id);
        model.addAttribute("city", city);
        return "main.city.edit";
    }

    @RequestMapping(value = "getID")
    @ResponseBody
    public String getID(@RequestParam(value = "cityName")String cityName) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        City city = cityService.findByCityName(DataTableUtil.handleCN(cityName));
        if(city != null) {
            jsonObject.put("flag", true);
            jsonObject.put("id", city.getId());
        } else {
            jsonObject.put("flag", false);
        }

        return jsonObject.toString();
    }

}
