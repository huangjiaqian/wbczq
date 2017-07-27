package com.huangjq.common.pojo;

public class ExcelValue {
	
	private String key;
	private Object value;
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
	@Override
	public String toString() {
		return "ExcelValue [key=" + key + ", value=" + value + "]";
	}
}
