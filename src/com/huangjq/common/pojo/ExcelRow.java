package com.huangjq.common.pojo;

import java.text.SimpleDateFormat;


/**
 * excel 行
 * @author hjq
 *
 */
public class ExcelRow {
	
	private String key;
	private Object value;
	private SimpleDateFormat sdf;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	@Override
	public String toString() {
		return "ExcelValue [key=" + key + ", value=" + value + ", sdf=" + sdf + "]";
	}
}
