package com.ifuture.iread.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 
 * @author 
 * 
 */
public class DateUtil {
	public static final String COMMON_DATE_INDEX_FORMAT = "yyyyMMdd";
	
	private static Calendar cld = Calendar.getInstance();
	
	public static int getMonthIndex(Date date) {
		cld.setTime(date);
		return cld.get(Calendar.MONTH);
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}
	

	/**
	 * 将截止日期Date类型转换为字符串某日00:00:00转换为前一天24:00:00
	 * 
	 * @param date 日期类型
	 * @return 日期字符串
	 */
	public static String formatEndDate(Date date) {
		//return format(date, "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String s=df.format(new Date(date.getTime()));
		if(s.contains("00:00:00")){
			s=df.format(new Date(date.getTime() - 24 * 60 * 60 * 1000)); 
			s=s.replace("00:00:00", "二十四时");
		}else{
			s=s.replace("12:00:00", "十二时");
		}
		return s;
	}
	

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "null";
		}
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, null);
	}
	
	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			//pattern = "yyyy-MM-dd HH:mm:ss";
			pattern = "MM-dd-yyyy HH:mm:ss";
		}
		if (date == null || date.equals("") || date.equals("null")) {
			return new Date();
		}
		Date d = null;
		try {
			d = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
		}
		return d;
	}
	
	/*获得当前时间*/  
    public static String getCurrentTime(){  
        Date currentTime = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateString = formatter.format(currentTime);  
        return dateString;  
    }  

    /**
     * 将毫秒数转化为时分秒
     * 
     * @param elapse 用时毫秒数
     * @return 时分秒字符串，格式为hh:mm:ss
     */
    public static String getElapseTimeStr(long elapse){
    	long hh = 0;
    	long mm = 0;
    	long ss = 0;
    	long ms = 0;
    	if (elapse > 0) {
	    	ms = elapse % 1000;
	    	ss = elapse / 1000;
	    	if (ss > 60) {
	    		mm = ss / 60;
	    		ss = ss % 60;
	    		if (mm > 60) {
	    			hh = mm / 60;
	    			mm = mm % 60;
	    		}
	    	}
    	}
    	return String.format("%1$02d:%2$02d:%3$02d", hh, mm, ss);
    }

	/**
	 * 两个日期相差的月份
	 * 
	 * @param beginDate
	 * 			开始日期
	 * @param endDate
	 *          结束日期
	 * @return
	 * 			相差月份数
	 * @throws ParseException 
	 */
	public static Integer getTheNumberOfMonth(String beginDate, String endDate) throws ParseException {
		
        int iMonth = 0;
        int flag = 0;
    	Calendar objCalendarDate1 = Calendar.getInstance();
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        objCalendarDate1.setTime(format.parse(beginDate));

        Calendar objCalendarDate2 = Calendar.getInstance();
        objCalendarDate2.setTime(format.parse(endDate));

        if(objCalendarDate2.equals(objCalendarDate1)) {
        	return 0;
        }
        if(objCalendarDate1.after(objCalendarDate2)) {
            Calendar temp = objCalendarDate1;
            objCalendarDate1 = objCalendarDate2;
            objCalendarDate2 = temp;
        }
        if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) - objCalendarDate1.get(Calendar.DAY_OF_MONTH) < -1) {
        	flag = 1;
        }
        if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
            iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12 
            		+ objCalendarDate2.get(Calendar.MONTH) - flag) 
            		- objCalendarDate1.get(Calendar.MONTH);
        } else {
            iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
        }
        return iMonth;
	}

	public static String getPremiumDeliveryTime(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"); 
		 Calendar cd = Calendar.getInstance();   
		 cd.setTime(date);
		 cd.add(Calendar.MONTH, 3);
		 return sdf.format(cd.getTime());
	}
	
	public static Date getDateLastSecond(Date date){
		if(date==null){
			return null;
		}
		 Calendar cd = Calendar.getInstance();   
		 cd.setTime(date);
		 cd.add(Calendar.HOUR_OF_DAY, 23);
		 cd.add(Calendar.MINUTE, 59);
		 cd.add(Calendar.SECOND,59);
		 return cd.getTime();
	}
	
	
	/**
	 * 根据国家和时间格式来格式化时间
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String formatDateWithCountry(Date date, String pattern, Locale locale) {

		return new SimpleDateFormat(pattern,locale).format(date);
	}
	
    public static void main(String args[]) throws ParseException {
//    	System.out.println(DateUtil.getElapseTimeStr(386600 * 1000));
    	
    	
//		String beginDate = "2013-01-18";
//		String endDate = "2014-01-17";
		
//		String beginDate = "2013-01-26";
//		String endDate = "2014-10-25";
//		
//		System.out.println(getTheNumberOfMonth(beginDate, endDate));
    	
    	
//    	System.out.println(formatDateWithCountry(new Date(), "yyyy年MM月dd日", Locale.CHINA));
//    	System.out.println(formatDateWithCountry(new Date(), "MMM d, yyyy", Locale.ENGLISH));
    	
    	String date = "2016年1月";
    	Date d = DateUtil.format(date, "yyyy年MM月");
    	System.out.println(DateUtil.format(d));
    }
}
