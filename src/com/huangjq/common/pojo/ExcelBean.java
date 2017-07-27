package com.huangjq.common.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelBean {
	
	private Map<Integer,Map<String, Object>> valueMap; //值
	private List<ExcelHeader> excelHeaders; //标签
	private Map<String, String> dateFormats;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ExcelHeader> getExcelHeaders() {
		 Collections.sort(excelHeaders, new Comparator(){  
	        public int compare(Object o1, Object o2) {
	        	ExcelHeader e1=(ExcelHeader)o1;  
	        	ExcelHeader e2=(ExcelHeader)o2;
	        	if(e1.getSort() > e2.getSort()) return 1;
	            return 0;  
	        }
	    }); 
		return excelHeaders;
	}
	public void setExcelHeaders(List<ExcelHeader> excelHeaders) {
		this.excelHeaders = excelHeaders;
	}

	public void setValue(int row,String key,Object value){
		if(valueMap == null) valueMap = new HashMap<Integer, Map<String,Object>>();
		Map<String, Object> map = valueMap.get(row);
		if(map == null) map = new HashMap<String,Object>();
		map.put(key, value);
		valueMap.put(row,map);
	}

	public void setHeader(int sort,String key,String label){
		ExcelHeader excelHeader = new ExcelHeader(sort, key, label);
		if(excelHeaders == null) excelHeaders = new ArrayList<ExcelHeader>();
		excelHeaders.add(excelHeader);
	}
	
	public void setDateFormat(String key,String pattern){
		if(dateFormats == null) dateFormats = new HashMap<String, String>();
		dateFormats.put(key, pattern);
	}
	public String getDateFormat(String key){
		if(dateFormats == null) return null;
		return dateFormats.get(key);
	}
	
	/**
	 * 获取值
	 * @param key
	 * @param row
	 * @return
	 */
	public Object getValue(int row,String key){
		if(valueMap == null || valueMap.get(row) == null) return null;
		return valueMap.get(row).get(key);
	}
	
	public int getRowSize(){
		if(valueMap == null) return 0;
		return valueMap.size();
	}
}

