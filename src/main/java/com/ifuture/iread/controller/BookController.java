package com.ifuture.iread.controller;

import com.ifuture.iread.entity.Book;
import com.ifuture.iread.entity.City;
import com.ifuture.iread.service.IBookService;
import com.ifuture.iread.service.IBookletService;
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
 * Created by maofn on 2017/3/26.
 */
@Controller
@RequestMapping("/admin/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private IBookletService bookletService;

    @RequestMapping("/list")
    public String list() {
        return "main.book.list";
    }

    @RequestMapping("/fetchBooks")
    @ResponseBody
    public String fetchBooks(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = bookService.fetchBooks(dr);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Book book, Model model) {
        return "main.book.create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@Valid Book book, BindingResult result, Model model, HttpServletRequest request) {
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        boolean flag = true;
        boolean success = true;
        boolean success2 = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            if (!bookService.exist(book.getIsbn())) {
                success2  = bookService.create(book);
            }
            //如果新建书本不成功
            if (!success2) {
                flag = success2;
            } else {
                //保存书册
                success = bookletService.save(book.getIsbn(), start, end);
                //如果保存书册失败（编号已经存在）
                if (!success) {
                    flag = success;
                }
            }
        }
        if (!flag) {
            if (!success) {
                model.addAttribute("error", "输入的编号在数据库中已经存在！");
            } else {
                model.addAttribute("error", "后台出错！");
            }
            return "main.book.create";
        }
        model.addAttribute("success", success2);
        return "redirect:/admin/book/list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = bookService.remove(ids);
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
        Book book = bookService.findOneById(id);
        model.addAttribute("book", book);
        return "main.book.edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid Book book, BindingResult result, Model model) {
        boolean flag = true;
        if (result.hasErrors()) {
            flag = false;
        }
        if (flag) {
            bookService.update(book);
        }
        if (!flag) {
            return "main.book.edit";
        }
        return "redirect:/admin/book/list";
    }

}
