package com.ifuture.iread.util;

import java.io.File;

/**
 * Created by maofn on 2017/3/15.
 */
public class Constants {
    public static final String USER_MANAGEMENT = "user_management";

    public static final String ROLE_MANAGER = "role_manager";

    public static final String ROLE_STAFF = "role_staff";

    public static final String ROLE_ADMIN = "role_admin";

    public static final String ADMIN_USERNAME = "admin";

    public static final String ADMIN_PASSWORD = "admin";

    public static final String LU_RU_XI_TONG = "录入系统";

    public static final String GUAN_CANG = "馆藏";

    public static final String JIE_CHU = "借出";

    public static final String SHI_ZONG = "失踪";

    public static String EXCEPTION_CONSTRAINTVIOLATIONEXCEPTION="ConstraintViolationException";

    public static final String PDF_NAME = "qrcode.pdf";
    public static final String PDF_PATH = File.separator + "data" + File.separator + "qrcode" + File.separator + PDF_NAME;

    public static final String SESSION_OPEN_ID = "SESSION_OPEN_ID";
    public static final String SESSION_USER = "SESSION_USER";
    public static final String SESSION_MEMBER = "SESSION_MEMBER";

    public static final String LOGINER = "loginer";

    public static final String REGEX_MOBILE = "^1\\d{10}$";


    public static final String IMG_BASE_URL = File.separator + "data" + File.separator +"qrcode" + File.separator;

    public static final String EXCEPTION_MSG = "后台异常，请联系管理员";
}
