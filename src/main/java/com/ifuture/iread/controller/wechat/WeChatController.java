package com.ifuture.iread.controller.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ifuture.iread.controller.BaseController;
import com.ifuture.iread.entity.*;
import com.ifuture.iread.service.*;
import com.ifuture.iread.util.Constants;
import com.ifuture.iread.util.DateUtil;
import com.ifuture.iread.util.Json;
import com.ifuture.iread.util.StringUtil;
import com.ifuture.iread.util.wechat.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by maofn on 2017/3/28.
 */
@Controller
@RequestMapping("/wechat")
public class WeChatController extends BaseController {
    private  static  final org.slf4j.Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private IBookService bookService;

    @Autowired
    private IBookletService bookletService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private WechatHelper wechatHelper;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IBorrowRecordService borrowRecordService;

    @Autowired
    private ICheckCodeService checkCodeService;

    @Autowired
    private ISessionRecordService sessionRecordService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/test")
    public String test() {
        return "/wechat/1";
    }


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(@RequestParam(value = "selectedShopID", required = false) String selectedShopID,
                       Model model, HttpServletRequest request, HttpSession session) {
        //如果是在position.jsp跳转来的必定有selectedShopID参数
        //否则需要定位
        if (!StringUtils.isEmpty(selectedShopID)) {
            session.setAttribute("selectedShopID", selectedShopID);
            Shop shop = shopService.findOneById(selectedShopID);
            model.addAttribute("selectedShopName", handleNameTooLong(shop.getShopName()));
            model.addAttribute("selectedShopID", shop.getId());
        }
        User loginUser = (User)session.getAttribute(Constants.SESSION_USER);
        if (loginUser != null) {
            request.setAttribute("userType", "user");
        }
        Member member = (Member)session.getAttribute(Constants.SESSION_MEMBER);
        if (member != null) {
            request.setAttribute("userType", "member");
        }
        return "/wechat/main";
    }

    @RequestMapping("/showBooksByShopID")
    @ResponseBody
    public String showBooksByShopID(@RequestParam(value = "shopID",required = false) String shopID, HttpServletRequest request) {
        String bookName = request.getParameter("bookName");
        JSONObject jsonObject = new JSONObject();
        Shop shop = shopService.findOneById(shopID);
        List<Book> books =  bookService.listBooksByShopID(shopID, bookName);
        List<Book> orderedBooks = bookService.sortByBorrowTimes(books, shopID);
        JSONArray ja = new JSONArray();
        for (Book book : orderedBooks) {
            JSONObject j = new JSONObject();
            j.put("isbn", book.getIsbn());
            j.put("bookName", book.getBookName());
            j.put("press", book.getPress());
            j.put("author", book.getAuthor());
            j.put("borrowTimes", book.getBorrowTimes());
            j.put("summary", book.getSummary());
            j.put("price", book.getPrice());
            j.put("grade", book.getGrade());
            j.put("simgUrl", book.getSimgUrl());
            j.put("allowBookletBorrowNumber", book.getAllowBookletBorrowNumber());
            ja.put(j);
        }
        jsonObject.put("books", ja);
//        jsonObject.put("shopName", shop.getShopName());
        return jsonObject.toString();
    }


    /**
     *  根据经纬度找到最近的公益点
     * @param lat   纬度
     * @param lng   经度
     * @return
     */
    @RequestMapping("/getNearestShopName")
    @ResponseBody
    public String getNearestShopName(@RequestParam(value = "lat") String lat, @RequestParam(value = "lng") String lng) {
        // TODO 异常处理
        JSONObject jsonObject = new JSONObject();
        Shop shop = shopService.getNearestShop(lat, lng);
        String nearestShopName = shop.getShopName();
        jsonObject.put("nearestShopName", handleNameTooLong(nearestShopName));
        jsonObject.put("id", shop.getId());
        return jsonObject.toString();
    }

    private String handleNameTooLong(String name) {
        if (name.length() > 18) {
            name = name.substring(0, 18);
            name = name + "..";
            return name;
        }
        return name;
    }


    @RequestMapping("/position")
    public String position(HttpServletRequest request, Model model) {
        String shopID = request.getParameter("shopID");
        String shopName = request.getParameter("shopName");
        if (!StringUtil.isNotNull(shopID)) {
            model.addAttribute("position", "定位失败");
        } else {
            Shop shop = shopService.findOneById(shopID);
            City city = shop.getCity();
            String position = city.getCityName() + shop.getShopName();
            model.addAttribute("position", position);
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("shops", shopService.findAll());
        return "/wechat/position";
    }

    @RequestMapping("/getCitiesAndShops")
    @ResponseBody
    public String getCitiesAndShops(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String shopName = request.getParameter("shopName");
        List<City> cities = cityService.findAll();
        List<Shop> shops;
        if (StringUtils.isEmpty(shopName)) {
            shops = shopService.findAll();
        } else {
            shops = shopService.findAllByShopName(shopName);
        }
        JSONArray ja1 = new JSONArray();
        for (City city : cities) {
            JSONObject j = new JSONObject();
            j.put("value", city.getId());
            j.put("title", city.getCityName());
            ja1.put(j);
        }
        JSONArray ja2 = new JSONArray();
        for (Shop shop : shops) {
            JSONObject j = new JSONObject();
            j.put("value", shop.getId());
            j.put("title", shop.getShopName());
            ja2.put(j);
        }
        jsonObject.put("cities", ja1);
        jsonObject.put("shops", ja2);
        return jsonObject.toString();
    }

    @RequestMapping("/findShopsByCity")
    @ResponseBody
    public String findByCity(@RequestParam(value = "cityID") String cityID, Model model) {
        List<Shop> shops = shopService.findByCity(cityID);
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();
        for (Shop shop : shops) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", shop.getId());
            jsonObject.put("name", shop.getShopName());
            jsonObject.put("lat", shop.getLatitude());
            jsonObject.put("lng", shop.getLongitude());
            ja.put(jsonObject);
        }
        json.put("shops", ja);
        return json.toString();
    }

    @RequestMapping(value = "/findShopsByCityName", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findShopsByCityName(@RequestParam(value = "cityName") String cityName, HttpServletRequest request) {
        System.out.println(request.getCharacterEncoding());
        System.out.println(request.getParameter("cityName"));
        List<Shop> shops = shopService.findByCityName(cityName);
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();
        for (Shop shop : shops) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", shop.getId());
            jsonObject.put("name", shop.getShopName());
            jsonObject.put("lat", shop.getLatitude());
            jsonObject.put("lng", shop.getLongitude());
            ja.put(jsonObject);
        }
        json.put("shops", ja);
        return json.toString();
    }


    /**
     * 生成调用js-sdk的签名
     * @param request
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     */
    @RequestMapping(value="/createSignature",method= RequestMethod.GET)
    @ResponseBody
    private String createSignature(HttpServletRequest request) throws JsonProcessingException, IOException {
        String url=request.getParameter("url");
        JSONObject jsonObject = new JSONObject();
        //页面上调用js-sdk需要一些参数传到前台进行配置
        jsonObject.put("appId", wechatHelper.getAppid());
        try {
            Map<String, String> map = wechatHelper.sign(url);
            for (Map.Entry entry : map.entrySet()) {
                jsonObject.put(entry.getKey().toString(), entry.getValue());
            }
        } catch (Exception e) {

        }
        return jsonObject.toString();

    }


    @RequestMapping(value="/borrow",method= RequestMethod.GET)
    public String borrow(String bianhao, Model model) {
        Book book = bookService.findOneByIsbn(bianhao.split("-")[0]);
        model.addAttribute("book", book);
        model.addAttribute("bianhao", bianhao);
        return "wechat/borrow";
    }

    @RequestMapping(value="/returnBack",method= RequestMethod.GET)
    public String returnBack(String bianhao, Model model) {
        Book book = bookService.findOneByIsbn(bianhao.split("-")[0]);
        model.addAttribute("book", book);
        model.addAttribute("bianhao", bianhao);
        return "wechat/returnBack";
    }


    @RequestMapping("/borrowBook")
    @ResponseBody
    public String borrowBook(HttpSession session, @RequestParam("bianhao" ) String bianhao) {
        JSONObject jsonObject = new JSONObject();
        String openid = (String) session.getAttribute(Constants.SESSION_OPEN_ID);
        Member borrower = memberService.findOneByWechatId(openid);
        int type = bookletService.borrowBook(bianhao, borrower);
        switch (type) {
            case 200:
                jsonObject.put("msg", "成功借出");
                break;
            case 1:
                jsonObject.put("msg", "数据库中不存在这本书");
                break;
            case 2:
                jsonObject.put("msg", "此书已被借走");
                break;
            case 3:
                jsonObject.put("msg", "借书已满");
                break;
            case 500:
                jsonObject.put("msg", "后台报错");
                break;
        }
        if (type == 200) {
            jsonObject.put("flag", true);
        } else {
            jsonObject.put("flag", false);
        }
        return jsonObject.toString();
    }

    @RequestMapping("/returnBook")
    @ResponseBody
    public String returnBook(HttpSession session, @RequestParam(value = "bianhao",required = false) String bianhao) {
        JSONObject jsonObject = new JSONObject();
        //获取工作人员
        User returner = (User) session.getAttribute(Constants.SESSION_USER);
        int type = bookletService.returnBook(bianhao, returner);
        switch (type) {
            case 200:
                jsonObject.put("msg", "成功还书");
                break;
            case 2:
                jsonObject.put("msg", "此书已被归还");
                break;
            case 500:
                jsonObject.put("msg", "后台报错");
                break;
        }
        if (type == 200) {
            jsonObject.put("flag", true);
        } else {
            jsonObject.put("flag", false);
        }
        return jsonObject.toString();
    }


    @RequestMapping("/personInfo")
    public String personInfo(HttpSession session, Model model) {
        String openid = (String) session.getAttribute(Constants.SESSION_OPEN_ID);
        Member member = memberService.findOneByWechatId(openid);
        model.addAttribute("member", member);
        return "wechat/personInfo";
    }

    @RequestMapping("/record")
    public String record() {
        return "wechat/record";
    }



    @RequestMapping("/getAllRecord")
    @ResponseBody
    public String getAllRecord(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String openid = (String) session.getAttribute(Constants.SESSION_OPEN_ID);
        List<BorrowRecord> records = borrowRecordService.getAllRecordByOpenId(openid);
        JSONArray ja = new JSONArray();
        for (BorrowRecord record : records) {
            Booklet booklet = bookletService.findOneById(record.getBookletId());
            Shop borrowShop = shopService.findOneById(record.getBorrowShopId());
            String returnShopId = record.getReturnShopId();
            JSONObject j = new JSONObject();
            j.put("id", record.getId());
            j.put("bookName", booklet.getBook().getBookName());
            j.put("bianhao", booklet.getBianHao());
            j.put("author", booklet.getBook().getAuthor());
            j.put("borrowShopName", borrowShop.getShopName());
            j.put("borrowTime", DateUtil.format(record.getBorrowTime(), "yyyy-MM-dd HH:mm:ss"));
            if (!StringUtil.isNotNull(returnShopId)) {
                j.put("returnShopName", "--");
                j.put("returnTime", "--");
            } else {
                Shop returnShop = shopService.findOneById(returnShopId);
                j.put("returnShopName", returnShop.getShopName());
                j.put("returnTime", DateUtil.format(record.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
            }

            ja.put(j);
        }
        jsonObject.put("records", ja);
        return jsonObject.toString();
    }


    @RequestMapping("/getRecordByBianhao")
    @ResponseBody
    public String getRecordByBianhao(@RequestParam("bianhao") String bianhao) {
        JSONObject jsonObject = new JSONObject();
        List<BorrowRecord> records = borrowRecordService.getRecordByBianhao(bianhao);
        Booklet booklet = bookletService.findOneByIndex(bianhao);
        JSONArray ja = new JSONArray();
        for (BorrowRecord record : records) {
            JSONObject j = new JSONObject();
            Member borrower = memberService.findOneById(record.getBorrowerId());
            j.put("borrowerName", borrower == null ? "":borrower.getMemberName());
            if (!StringUtils.isEmpty(record.getBorrowShopId())) {
                Shop shop = shopService.findOneById(record.getBorrowShopId());
                j.put("borrowShopName", shop.getShopName());
            } else {
                j.put("borrowShopName", "");
            }
            j.put("borrowTime", DateUtil.format(record.getBorrowTime(), "yyyy-MM-dd HH:mm:ss"));
            if (StringUtil.isNotNull(record.getReturnShopId())) {
                j.put("returnTime", DateUtil.format(record.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                j.put("returnTime", "--");
            }
            if (!StringUtils.isEmpty(record.getReturnShopId())) {
                Shop shop = shopService.findOneById(record.getReturnShopId());
                j.put("returnShopName", shop.getShopName());
            } else {
                j.put("returnShopName", "");
            }
            ja.put(j);
        }
        jsonObject.put("records", ja);
        if (booklet != null) {
            jsonObject.put("bookName", booklet.getBook().getBookName());
            jsonObject.put("bookNo", booklet.getBianHao());
        } else {
            jsonObject.put("bookName", "");
            jsonObject.put("bookNo", "");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getCityList", method = RequestMethod.GET)
    @ResponseBody
    public Json getCityList(HttpServletRequest request) {
        Json j = new Json();
        List<City> dataList = cityService.findAll();
        j.setObj(dataList);
        j.setSuccess(true);
        return j;
    }

    @RequestMapping(value = "/getMemberLevel", method = RequestMethod.GET)
    @ResponseBody
    public Json getMemberLevel(HttpServletRequest request) {
        Json j = new Json();
        List<MemberLevel> dataList = memberLevelService.findAllEnable();
        j.setObj(dataList);
        j.setSuccess(true);
        return j;
    }

    /**
     * 会员注册保存
     * @param member
     * @param session
     * @return
     */
    @RequestMapping(value = "/saveMember", method = RequestMethod.POST)
    public ModelAndView saveMember(Member member, HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String checkCode = request.getParameter("checkCode");
        Member saveMember = memberService.saveOrUpdate(member);
        SessionRecord sessionRecord = new SessionRecord(saveMember.getWechatId(), saveMember);
        sessionRecordService.save(sessionRecord);
        session.setAttribute(Constants.SESSION_MEMBER, saveMember);
        //验证码验证日期
        CheckCode ck = checkCodeService.findByMobileAndCode(member.getMobile(), checkCode);
        ck.setVerifyDate(new Date());
        checkCodeService.update(ck);
        mav.addObject("memberId", saveMember.getId());
        mav.setViewName("wechat/pay");
        return mav;
    }

    /**
     * 登录，仅限工作人员登录
     */
    @RequestMapping(value = "/userLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView userLogin(User user, HttpServletRequest request, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String openId = (String) session.getAttribute(Constants.SESSION_OPEN_ID);
        if (!StringUtil.isNotNull(openId)) {
            mav.addObject("msg", Constants.EXCEPTION_MSG);
            mav.setViewName("wechat/register");
            return mav;
        }
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        User loginUser = userService.getUserByUserName(userName);
        //如果用户名不存在
        if (loginUser == null) {
            mav.addObject("msg", "用户名不存在");
            mav.setViewName("wechat/register");
            return mav;
        } else {
            //如果密码正确
            if (loginUser.getPassWord().equals(new Md5PasswordEncoder().encodePassword(passWord, ""))) {
                logger.info(loginUser.getNickName() + "-----login");
                //如果用户的公益点所属为空，无法登陆
                if (loginUser.getShop() == null) {
                    mav.addObject("msg", "该用户没有公益点信息，无法登陆！");
                    mav.setViewName("wechat/register");
                    return mav;
                }
                SessionRecord sessionRecord = new SessionRecord(openId, loginUser);
                sessionRecordService.save(sessionRecord);
                session.setAttribute(Constants.SESSION_USER, loginUser);
                mav.setViewName("redirect:/wechat/main");
                return mav;
            } else {
                mav.addObject("msg", "密码不正确");
                mav.setViewName("wechat/register");
                return mav;
            }
        }
    }

    /**
     * 退出登录
     * 删除对应的SessionRecord
     */
    @RequestMapping(value = "/exitLogin", method = RequestMethod.GET)
    public ModelAndView exitLogin(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        Member member = (Member) session.getAttribute(Constants.SESSION_MEMBER);
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (member != null) {
            session.removeAttribute(Constants.SESSION_MEMBER);
            sessionRecordService.deleteByMember(member);
        }
        if (user != null) {
            session.removeAttribute(Constants.SESSION_USER);
            sessionRecordService.deleteByUser(user);
        }
        mav.setViewName("wechat/register");
        return mav;
    }

    /**
     * 微信支付接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/paymentMember", method = RequestMethod.POST)
    @ResponseBody
    public Json paymentParameter(HttpServletRequest request, HttpServletResponse response) {
        Json j = new Json();
        try {
            String memberLevelID = request.getParameter("memberLevelID");
            String memberId = request.getParameter("memberId");
            Member member = memberService.findOneById(memberId);
            MemberLevel memberLevel = memberLevelService.findOneById(memberLevelID);
            HttpSession session = request.getSession();
            String openId = (String) session.getAttribute(Constants.SESSION_OPEN_ID);
            //如果openid为空
            if (StringUtils.isEmpty(openId)) {
                j.setSuccess(false);
                j.setMsg(Constants.EXCEPTION_MSG);
                return j;
            }
            //如果都不为空
            if (member != null && memberLevel != null) {
                BigDecimal paymentAmount = memberLevel.getAmount();
                //如果支付金额是0
                if (paymentAmount.compareTo(new BigDecimal(0)) == 0) {
                    String dateAsString = DateUtil.format(new Date(), DateUtil.COMMON_DATE_INDEX_FORMAT);
                    String tradeNo = memberService.getCurrentDateMaxPaymentNo(dateAsString);
                    member.setPayTime(new Date());
                    member.setPaymentNo(tradeNo);
                    member.setMemberLevel(memberLevel);
                    memberService.updateMember(member);
                    j.setMsg("0");
                    return j;
                }
                BigDecimal hundred = new BigDecimal("100");
                int paymentAmountAsString = hundred.multiply(paymentAmount).intValue();
                // 设置金额等支付参数
                Map map = getPaymentParameters(request, openId, String.valueOf(paymentAmountAsString));
                SortedMap<Object,Object> params = new TreeMap<Object,Object>();
                params.put("appId", WeChatUtil.getProperties().get("appid"));
                params.put("timeStamp", Long.toString(new Date().getTime()));
                params.put("nonceStr", PayCommonUtil.createNoncestr());
                params.put("package", "prepay_id="+map.get("prepay_id"));
                params.put("signType", ConfigUtil.SIGN_TYPE);
                String paySign =  PayCommonUtil.createSign("UTF-8", params);
                params.put("packageValue", "prepay_id="+map.get("prepay_id"));    //这里用packageValue是预防package是关键字在js获取值出错
                params.put("paySign", paySign);                                                          //paySign的生成规则和Sign的生成规则一致
                params.put("sendUrl", getContextPath(request) + "/wechat/paymentSuccess");                               //付款成功后跳转的页面
                String userAgent = request.getHeader("user-agent");
                char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger")+15);
                params.put("agent", new String(new char[]{agent}));//微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。
                j.setObj(params);
                j.setSuccess(true);
                member.setMemberLevel(memberLevel);
                memberService.updateMember(member);
            } else {
                j.setSuccess(false);
                j.setMsg(Constants.EXCEPTION_MSG);
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 支付成功的回调接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/paymentSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public String paymentSuccess(HttpServletRequest request) throws Exception {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result  = new String(outSteam.toByteArray(),"utf-8");//获取微信调用我们notify_url的返回信息
        Map<Object, Object> map = XMLUtil.doXMLParse(result);
        String openId = "";
        String paymentNo = "";
        for(Object keyValue : map.keySet()) {
            logger.info(keyValue+"="+map.get(keyValue));
            if ("out_trade_no".equals(keyValue)) {
                paymentNo = (String) map.get(keyValue);
            }
            if ("openid".equals(keyValue)) {
                openId = (String) map.get(keyValue);
            }
        }
        if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
            memberService.updateMemberPaymentInfo(new Date(), paymentNo, openId);
        }
        return "redirect:/wechat/main";
    }

    /**
     * 获取页面中发起支付的参数和签名
     * @param request
     * @param openId
     * @param paymentAmount
     * @return
     * @throws Exception
     */
    private Map getPaymentParameters(HttpServletRequest request, String openId, String paymentAmount) throws Exception {
        SortedMap<Object, Object> parameters = new java.util.TreeMap<Object, Object>();
        parameters.put("appid", WeChatUtil.getProperties().get("appid"));   //appid
        parameters.put("mch_id", ConfigUtil.MCH_ID);    //商户号
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());    //随机字符串
        parameters.put("body", "爱阅读.理想家会员支付");  //商品描述
        String dateAsString = DateUtil.format(new Date(), DateUtil.COMMON_DATE_INDEX_FORMAT);
        String tradeNo = memberService.getCurrentDateMaxPaymentNo(dateAsString);
        parameters.put("out_trade_no", tradeNo);    //商品id
        parameters.put("spbill_create_ip", getIpAddress(request));  //用户端ip
        parameters.put("total_fee", paymentAmount); //商品金额（单位为分）
        parameters.put("notify_url", getContextPath(request) + "/wechat/paymentSuccess");   //异步接收微信支付结果通知的回调地址
        parameters.put("trade_type", "JSAPI");  //交易类型
        parameters.put("openid", openId);   //用户标识
        String sign = PayCommonUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);   //根据参数按字典序再md5加密算来的签名
        String requestXML = PayCommonUtil.getRequestXml(parameters);    //转成xml格式
        //调用统一下单api，生成预付单
        String result = PayCommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
        logger.info(result);
        return XMLUtil.doXMLParse(result);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtil.isNotNull(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNotNull(ip) && !"unknown".equalsIgnoreCase(ip)) {
        // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
    @RequestMapping("/ERROR")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("wechat/errors");
        return mav;
    }
    /*@RequestMapping(name = "/zeroMember", method = RequestMethod.POST)
    @ResponseBody
    public String zeroMember(HttpServletRequest request) {
        String memberLevelID = request.getParameter("memberLevelID");
        String memberId = request.getParameter("memberId");
        Member member = memberService.findOneById(memberId);
        MemberLevel memberLevel = memberLevelService.findOneById(memberLevelID);

    }*/
}
