package com.ifuture.iread.controller;

import com.ifuture.iread.service.IBookletService;
import com.ifuture.iread.service.ICityService;
import com.ifuture.iread.service.IShopService;
import com.ifuture.iread.util.*;
import com.mchange.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maofn on 2017/3/27.
 */
@Controller
@RequestMapping("/admin/booklet")
public class BookletController {

    @Autowired
    private IShopService shopService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IBookletService bookletService;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("shops", shopService.findAll());
        List<String> states = new ArrayList<>();
        states.add(Constants.GUAN_CANG);
        states.add(Constants.LU_RU_XI_TONG);
        states.add(Constants.JIE_CHU);
        states.add(Constants.SHI_ZONG);
        model.addAttribute("states", states);
        return "main.booklet.list";
    }

    @RequestMapping("/fetchBooklets")
    @ResponseBody
    public String fetchBooklets(HttpServletRequest request) {
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro = bookletService.fetchBooklets(dr, request);
        return DataTableUtil.transToJsonStr(dro);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@RequestParam(value = "ids") String ids) {
        JSONObject json = new JSONObject();
        boolean flag = bookletService.remove(ids);
        json.put("flag", flag);
        if (flag) {
            json.put("msg", "删除成功");
        } else {
            json.put("msg", "删除失败");
        }
        return json.toString();
    }

    @RequestMapping(value = "/allocate")
    @ResponseBody
    public String allocate(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        String shopID = request.getParameter("shopID");
        Boolean flag = bookletService.allocate(ids, shopID);
        JSONObject jsonObject = new JSONObject();
        if (flag) {
            jsonObject.put("msg", "调拨成功");
        } else {
            jsonObject.put("msg", "调拨失败");
        }
        jsonObject.put("flag", flag);
        return jsonObject.toString();
    }

    /*@RequestMapping(value = "/saveToPDF", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String saveToPDF(@RequestBody Qrcode[] qrcodeList) {
        // TODO 弄清楚这些注解的含义

        return "";
    }*/


    /**
     *  我们约定所有的图片名都是i.png
     *  当只有一条数据时springmvc绑定base64[]会出错
     * @param base64s   图片的base64
     * @param maxPerRow 每一行的二维码个数
     * @return
     */
    @RequestMapping(value = "/saveToPDF", method = RequestMethod.POST)
    @ResponseBody
    public String saveToPDF( @RequestParam("base64s[]") String[] base64s, @RequestParam("bianhaos[]") String[] bianhaos, @RequestParam("maxPerRow") String maxPerRow, HttpServletRequest request) {
        //当传过来的只有一维数组时，解析出错，将一条数据拆成两条了
        if (base64s.length == 2) {
            if (base64s[0].length() < 30|| base64s[1].length() < 30) {
                String temp = base64s[0] + "," + base64s[1];
                base64s = new String[]{temp};
            }
        }
        //获取项目运行的根路径
        String basePath = request.getSession().getServletContext().getRealPath("/");
        JSONObject jsonObject = new JSONObject();
        try {
            int i = 0;
            String[] images = new String[base64s.length];
            //将所有的base64都转成图片保存在本地
            for (String base64 : base64s) {
                String imageName = i + ".png";
                Base64Util.base64ToImage(basePath, base64, imageName);
                images[i] = imageName;
                i++;
            }
            //将html转成pdf
//            GeneratePdfUtil.html2Pdf(basePath, html, base64s);
//            new GeneratePdfUtil().html2Pdf(basePath, html);
            //画出pdf
            new DrawPdfUtil(basePath + Constants.IMG_BASE_URL, images, bianhaos, Integer.valueOf(maxPerRow)).draw();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("flag", false);
            jsonObject.put("msg", "生成失败了！");
            return jsonObject.toString();
        }
        jsonObject.put("flag", true);
        jsonObject.put("msg", "生成成功！");
        return jsonObject.toString();
    }

    @RequestMapping("/download/pdf")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws Exception {
        String basePath = request.getSession().getServletContext().getRealPath("/");
        File file = new File(basePath + Constants.PDF_PATH);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String(Constants.PDF_NAME.getBytes("UTF-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.getBytes(file), headers, HttpStatus.CREATED);
    }

}
