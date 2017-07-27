package com.huangjq.common.pojo;

/**
 * excel 头
 * @author hjq
 *
 */
public class ExcelHeader {
	
	private Integer sort;
	private String key;
	private String label;
	
	public ExcelHeader() {
	}
	
	public ExcelHeader(Integer sort, String key, String label) {
		this.sort = sort;
		this.key = key;
		this.label = label;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "ExcelHeader [sort=" + sort + ", key=" + key + ", label=" + label + "]";
	}
}
