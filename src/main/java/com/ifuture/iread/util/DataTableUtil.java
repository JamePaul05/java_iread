package com.ifuture.iread.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * datatable工具类
 * 
 * @author 
 * 
 */
public class DataTableUtil {

	private static final Logger logger = LoggerFactory.getLogger(DataTableUtil.class);



	public static DataRequest trans(HttpServletRequest request) {
		DataRequest dr=new DataRequest();
        String draw = request.getParameter(DataTablesContants.DRAW);
		//获取数据起始位置
		Integer start = Integer.valueOf(request.getParameter(DataTablesContants.START));
		//获取每页显示长度
		Integer length = Integer.valueOf(request.getParameter(DataTablesContants.LENGTH));
		//获取需要排序的下标
		String  orderIndex = request.getParameter(DataTablesContants.ORDER_INDEX);
        String orderField = null;
        //如果不是第一列需要排序
		if(!"0".equalsIgnoreCase(orderIndex)){
            //获取排序列名
			orderField = request.getParameter(DataTablesContants.ORDER_FIELD_PREFIX + orderIndex + DataTablesContants.ORDER_FIELD_SUFFIX);
		}
		//获取搜索关键词
		String search = request.getParameter(DataTablesContants.SEARCH);
		try {
			//如果是ISO-8859-1编码， 就转为utf-8
			if (search.equals(new String(search.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
				search = new String(request.getParameter(DataTablesContants.SEARCH).getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
        String direction = request.getParameter(DataTablesContants.DIRECTION);

        //获取当前第几页
		Integer page = start / length;

        dr.setDraw(draw);
        dr.setDirection(direction);
		dr.setPage(page + 1);
		dr.setRows(length);
		dr.setSearch(search);
        dr.setOrderField(orderField);
		return dr;
	}

	/**
	 * 解决中文乱码
	 * @param search
	 * @return
	 */
	public static String handleCN(String search) {
		try {
			//如果是ISO-8859-1编码， 就转为utf-8
			if (search.equals(new String(search.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
				search = new String(search.getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return search;
	}

	public static Pageable convertToPage(DataRequest dataRequest, Class<?> clazz) {
        String orderField = dataRequest.getOrderField();
        String direction = dataRequest.getDirection();
        int pageIndex = dataRequest.getPage();
        int rows = dataRequest.getRows();
        Sort sort = null;
        if(StringUtil.isNotNull(orderField)) {
            String clazzName = clazz.getSimpleName().toLowerCase();
            if ((clazzName + "Name").equalsIgnoreCase(orderField)) {
                orderField = "pinyin";
            }
            sort = new Sort(DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC, orderField);
        }
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        return pageable;
    }
	
	public static String trans(Class obj,List<Object> param,String search,String[] ingores) {
		Field[] fs = obj.getDeclaredFields();
		String str="";
		String[] valids=getValidPara(fs,ingores);
		int length = valids.length;
		for(int i=0;i<length;i++){
			String name=valids[i];
			if("createTime".equalsIgnoreCase(name)){
			}else{
				str+=name+" like ?";
				param.add("%"+search+"%");
			}
			if(i!=length-1){
				str+=" or ";
			}
		}
		return str;
	}
	
	private static String[] getValidPara(Field[] fs,String[] ingores){
		List<String> list=new ArrayList<String>();
		int length = fs.length;
		for(int i=0;i<length;i++){
			boolean isIngore=true;
			Field f = fs[i];
			String name = f.getName();
			for(String ingore:ingores){
				if(ingore.equalsIgnoreCase(name)){
					isIngore=false;
				}
			}
			if(isIngore){
				list.add(name);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String transToJsonStr(DataTableReturnObject dro){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put(DataTablesContants.DRAW, dro.getDraw());
		jsonObject.put(DataTablesContants.RECORDS_TOTAL, dro.getRecordsTotal());
		jsonObject.put(DataTablesContants.RECORDS_FILTERED, dro.getRecordsFiltered());
		jsonObject.put(DataTablesContants.DATA, dro.getData());
		return jsonObject.toString();
	}
	
}
