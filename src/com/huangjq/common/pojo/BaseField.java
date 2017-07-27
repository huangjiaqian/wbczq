package com.huangjq.common.pojo;

public class BaseField {
	private String columnName;
	private String columnType; 
	private int datasize; 
	private int digits; 
	private int nullable;
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public int getDatasize() {
		return datasize;
	}
	public void setDatasize(int datasize) {
		this.datasize = datasize;
	}
	public int getDigits() {
		return digits;
	}
	public void setDigits(int digits) {
		this.digits = digits;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	@Override
	public String toString() {
		return "BaseField [columnName=" + columnName + ", columnType="
				+ columnType + ", datasize=" + datasize + ", digits=" + digits
				+ ", nullable=" + nullable + "]";
	}
}
