package com.ifuture.iread.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by maofn on 2017/3/15.
 */
public class PinyinUtil {
    public static String hanyuConvertToPinyin(String hanyu) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //转成的英文拼音格式
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);          //小写    zhong
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);   //有声调，用数字表示。 zhong1  zhong4
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);   //碰到unicode 的ü 、v 和 u时的显示方式。此处为lv
        char[] input = hanyu.trim().toCharArray();
        StringBuffer output = new StringBuffer("");
        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);
                } else
                    output.append(Character.toString(input[i]));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
