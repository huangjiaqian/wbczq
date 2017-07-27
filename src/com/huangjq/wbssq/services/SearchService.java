package com.huangjq.wbssq.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.huangjq.wbssq.commons.StringUtils;

public class SearchService {

	private JTextArea resultArea;
	private JLabel statusLabel;
	private String searchNr; //查找内容
	private Integer resultCount = 0;
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	
	private String[] searchTyle = {"txt","js","ftl","jsp","java","html","css","php"}; //搜索类型
	
	public String checkFile(File file){
		String path = file.getPath();
		for (String string : searchTyle) {
			if(path.endsWith("."+string)){
				return string;
			}
		}
		return "";
	}
	
	
	/**
	 * 查找文本
	 */
	public void searchText(File file,String fileLx){
		if(file == null || file.isDirectory()) return;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			int i = 1;
			while((line = br.readLine()) != null){
				/*
				Pattern pattern = Pattern.compile(".*"+searchNr+".*");
				Matcher matcher = pattern.matcher(line);
				boolean dyzf = matcher.matches();
				*/
				if(line.indexOf(searchNr) != -1){
					resultCount ++;
					writeText(file.getPath()+" 第"+i+"行");
				}
				i++;
			}
			if(!"".equals(fileLx)){
				Integer lineNumber = (Integer) resultMap.get(fileLx);
				if(lineNumber != null){
					resultMap.put(fileLx, lineNumber+i-1);
				}else{
					resultMap.put(fileLx, i-1);
				}
				Integer fileNumber = (Integer) resultMap.get("fileNumber"+fileLx);
				if(fileNumber != null){
					resultMap.put("fileNumber"+fileLx, fileNumber+1);
				}else{
					resultMap.put("fileNumber"+fileLx, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void searchSubFiles(File file) {
		if (file == null || file.isFile())
			return;
		File[] subFiles = file.listFiles();
		if(subFiles == null) return;
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				searchSubFiles(subFile);
			} else {
				String fileLx = checkFile(subFile);
				if(!"".equals(fileLx)){
					searchText(subFile,fileLx);					
				}
				statusLabel.setText(subFile.getPath());
			}
		}
	}

	public synchronized void writeText(String text) {
		if (StringUtils.isBlank(resultArea.getText())) {
			resultArea.setText(text);
		} else {
			resultArea.setText(resultArea.getText() + "\n" + text);
		}
	}

	public JTextArea getResultArea() {
		return resultArea;
	}

	public void setResultArea(JTextArea resultArea) {
		this.resultArea = resultArea;
	}
	public JLabel getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getSearchNr() {
		return searchNr;
	}
	public void setSearchNr(String searchNr) {
		this.searchNr = searchNr;
	}
	public Integer getResultCount() {
		return resultCount;
	}
	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
}
