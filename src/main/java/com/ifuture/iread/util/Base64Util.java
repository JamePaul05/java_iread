package com.ifuture.iread.util;

import com.ifuture.iread.controller.wechat.WeChatController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by maofn on 2017/3/30.
 */
public class Base64Util {

    private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    /**
     * 将图片的base64码写入到本地图片
     * @param base64
     * @param fileName  图片名称
     */
    public static void base64ToImage(String basePath, String base64, String fileName) {
        String head = "data:image/png;base64,";
        String imgBase64 = base64.substring(head.length());
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            byte[] b = decoder.decodeBuffer(imgBase64);
            Date date = new Date();

            //判断目录是否创建
            File dir = new File(basePath + Constants.IMG_BASE_URL);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //创建文件
            File file = new File(dir, fileName);
//            logger.info(file.getAbsolutePath());
            if (!file.exists()) {
                file.createNewFile();
            }
            //写入输出流
            out= new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
