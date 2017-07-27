package com.huangjq.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.huangjq.common.pojo.BaseField;
import com.huangjq.wbssq.commons.StringUtils;

public class GeneratePojoByDb {
	private String filePath = "D:/test";
	private String packageStr = "com.huangjq.bbs.vo";
	
	public void generate(String table) throws IOException {
		generate(table, null);
	}
	public void generate(String table,String className) throws IOException {
		if(StringUtils.isBlank(className)){
			className = table.toLowerCase();			
		}
		
		
		className = firstCharacterToUpper(replaceUnderlineAndfirstToUpper(className, "_", ""));
		new File(this.filePath + "/" + packageStr.replace(".", "/")).mkdirs();
		String srcPath = this.filePath + "/" + packageStr.replace(".", "/")
				+ "/" + className + ".java";

		PrintWriter pw = new PrintWriter(srcPath, "UTF-8");
		List<BaseField> baseFields = getBaseFields(table);
		pw.println("package " + packageStr + ";\r\n");
		pw.println(importStr());
		pw.println("@Entity");
		pw.println("@Table(name=\""+table+"\")");
		
		pw.println("public class " + className + "{");
		for (BaseField baseField : baseFields) {
			String name = replaceUnderlineAndfirstToUpper(baseField.getColumnName().toLowerCase(),"_","");
			String type = getType(baseField.getColumnType(),baseField.getDatasize());
			
			if(name.toLowerCase().equals("id")){
				pw.println("\t@Id\n\t@GeneratedValue(strategy=GenerationType.IDENTITY)");
			}else pw.println("\t@Column(name=\""+baseField.getColumnName()+"\")");
			
			pw.println(getColumnStr(name, type));
		}
		for (BaseField baseField : baseFields) {
			String name = replaceUnderlineAndfirstToUpper(baseField.getColumnName().toLowerCase(),"_","");
			String type = getType(baseField.getColumnType(),
					baseField.getDatasize());
			pw.println(getGetMethod(name, type));
			pw.println(getSetMethod(name, type));
		}
		String toStringStr = "\tpublic String toString(){ \r\n\t\treturn \""
				+className + " [";
		for (BaseField baseField : baseFields) {
			String name = replaceUnderlineAndfirstToUpper(baseField.getColumnName().toLowerCase(),"_","");
			toStringStr += name + "=\" + "+name+" + \",";
		}
		toStringStr = toStringStr.replaceAll("^,*|,*$", "");
		toStringStr += "]\";\r\n\t}";
		pw.println(toStringStr);
		pw.println("}");

		pw.close();
	}

	public String getType(String dbType, int size) {
		return Prop.getPropValue("db2java", dbType.toUpperCase());
	}
	public String importStr(){
		return "import javax.persistence.Column;\r\n"
			+"import javax.persistence.Entity;\r\n"
			+"import javax.persistence.GeneratedValue;\r\n"
			+"import javax.persistence.GenerationType;\r\n"
			+"import javax.persistence.Id;\r\n"
			+"import javax.persistence.Table;\r\n";	
	}
	public List<BaseField> getBaseFields(String table) {
		List<BaseField> baseFields = new ArrayList<BaseField>();
		Connection conn = getConn();
		ResultSet rs = null;
		DatabaseMetaData dbmd = null;
		try {
			dbmd = conn.getMetaData();
			rs = dbmd.getColumns(null, "%", table, "%");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME").toLowerCase();
				String columnType = rs.getString("TYPE_NAME");
				int datasize = rs.getInt("COLUMN_SIZE");
				int digits = rs.getInt("DECIMAL_DIGITS");
				int nullable = rs.getInt("NULLABLE");
				BaseField baseField = new BaseField();
				baseField.setColumnName(columnName);
				baseField.setColumnType(columnType);
				baseField.setDatasize(datasize);
				baseField.setDigits(digits);
				baseField.setNullable(nullable);
				baseFields.add(baseField);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return baseFields;
	}

	public Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://127.0.0.1:3306/bbs?useUnicode=true&characterEncoding=UTF-8";
	    String username = "root";
	    String password = "123456";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}


	public String getGetMethod(String name, String type) {
		String mehtodStr = "\tpublic " + type + " get"
				+ name.substring(0, 1).toUpperCase() + name.substring(1);
		mehtodStr += "(){ \r\n\t\treturn this." + name + ";\r\n\t}";
		return mehtodStr;
	}

	public String getSetMethod(String name, String type) {
		String mehtodStr = "\tpublic void set"
				+ name.substring(0, 1).toUpperCase() + name.substring(1);
		mehtodStr += "(" + type + " " + name + "){ \r\n\t\tthis." + name + " = " + name
				+ ";\r\n\t}";
		return mehtodStr;
	}

	public String getColumnStr(String name, String type) {
		return "\tprivate " + type + " " + name + ";";
	}

	/**
	 * 首字母大写
	 * 
	 * @param srcStr
	 * @return
	 */
	public static String firstCharacterToUpper(String srcStr) {
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
	}

	/**
	 * 替换字符串并让它的下一个字母为大写
	 * 
	 * @param srcStr
	 * @param org
	 * @param ob
	 * @return
	 */
	public static String replaceUnderlineAndfirstToUpper(String srcStr,
			String org, String ob) {
		String newString = "";
		int first = 0;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first) + ob;
				srcStr = srcStr
						.substring(first + org.length(), srcStr.length());
				srcStr = firstCharacterToUpper(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}
	
	public static void first(){
		/*
		File f = new File(CConst.CLASSPATH);
		f = f.getParentFile();
		f = f.getParentFile();
		System.setProperty("web.root", FilePathUtil.commandPath(f.getPath() + "/src/main/webapp"));
		*/
	}
	public static void main(String[] args) throws IOException {
		first();
		
		GeneratePojoByDb g = new GeneratePojoByDb();
		g.generate("bbs_module","module");
		g.generate("bbs_post","post");
		g.generate("bbs_reply","reply");
		g.generate("bbs_topic","topic");
		g.generate("bbs_user","user");
	}
}
