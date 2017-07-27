package com.huangjq.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.huangjq.wbssq.commons.Const;
import com.huangjq.wbssq.commons.DateUtil;
import com.huangjq.wbssq.commons.FilePathUtil;
import com.huangjq.wbssq.commons.StringUtils;

public class Prop {
	private static final Map<String, Map<String, String>> propCache = new HashMap<String, Map<String,String>>();
	
	public static String getPropValue(String propFileName, String key) {
		return StringUtils.null2String(getAllPropValue(propFileName).get(key));
	}
	
	public static void removeCache(String propFileName){
		propCache.remove(propFileName);
	}
	
	public static Map<String, String> getAllPropValue(String propFileName) {
		if(propCache.get(propFileName) != null){
			return propCache.get(propFileName);
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		String hz = ".properties";// 后缀
		Properties properties = new Properties();
		InputStream in = null;
		try {

			String filePath = FilePathUtil.commandPath(StringUtils.appendStr(Const.PROPPATH,"\\",propFileName,hz));
			File file = new File(filePath);
			if (!file.exists()) {
				System.err.println("文件("+file+")不存在!");
				return map;
			}
			in = new BufferedInputStream(new FileInputStream(file));

			properties.load(in);
			Set<Object> keys = properties.keySet();
			for (Object key : keys) {
				map.put(key.toString(), StringUtils.null2String(properties.getProperty(key.toString())));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		propCache.put(propFileName, map);
		return map;
	}

	public static boolean setPropValue(String propFileName, String key, String value) {
		Map<String, String> map = getAllPropValue(propFileName);
		map.put(key, value);

		return setPropValue(propFileName, map);
	}

	public static boolean removePropValue(String propFileName, String key) {
		Set<String> set = new HashSet<String>();
		set.add(key);
		return removePropValue(propFileName, set);
	}

	public static boolean removePropValue(String propFileName, String... key) {
		Set<String> set = new HashSet<String>();
		for (String string : key) {
			set.add(string);
		}
		return removePropValue(propFileName, set);
	}

	public static boolean removePropValue(String propFileName, Set<String> keys) {
		Map<String, String> map = getAllPropValue(propFileName);
		for (String key : keys) {
			map.remove(key);
		}
		return setPropValue(propFileName, map);
	}

	public static boolean setPropValue(String propFileName, Map<String, String> map) {
		propCache.put(propFileName, map);
		String msg = "modify ";
		String hz = ".properties";// 后缀
		Properties properties = new Properties();
		OutputStream out = null;
		try {
			String filePath = FilePathUtil.commandPath(Const.PROPPATH + propFileName + hz);
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
				msg = "create ";
			}
			out = new BufferedOutputStream(new FileOutputStream(file));
			map.putAll(getAllPropValue(propFileName));

			properties.clear();

			Set<String> keys = map.keySet();
			for (String key : keys) {
				String value = map.get(key);
				properties.setProperty(key, value);
			}
			properties.store(out, msg + DateUtil.getDateNow("yyyy-MM-dd HH:mm:ss"));// 保存键值对到文件中
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 读取属性文件中的属性值
	 * 
	 * @param attr
	 * @return
	 */
	public static String readSingleProps(String attr) {
		String retValue = "";
		Properties props = new Properties();
		try {
			/*
			 * if (!FileUtil.isFileExist(getPropsFilePath())) { return ""; }
			 * FileInputStream fi = new FileInputStream(getPropsFilePath());
			 */
			InputStream fi = getPropsIS();
			props.load(fi);
			fi.close();

			retValue = props.getProperty(attr);
		} catch (Exception e) {
			return "";
		}
		return retValue;
	}

	public static InputStream getPropsIS() {
		InputStream ins = Prop.class.getResourceAsStream("/destinations.properties");
		return ins;
	}

}
