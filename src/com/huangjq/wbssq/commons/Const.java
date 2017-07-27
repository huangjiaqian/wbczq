package com.huangjq.wbssq.commons;

import java.net.URL;

import com.huangjq.common.Prop;


public class Const {
	
	public static String CLASSPATH;
	public static String PROPPATH;
	
	private static String ROOTPATH;
	private static String UPLOADPATH;
	private static String TEMPPATH;
	private static String SQLPATH;
	
	static {
		URL url = Const.class.getResource("/");
		CLASSPATH = url.getPath().substring(1, url.getPath().length());
		
		PROPPATH = FilePathUtil.commandPath(CLASSPATH+"/prop");
	}
	
	public final static String getRootPath(){
		if(StringUtils.isBlank(ROOTPATH)){
			ROOTPATH = System.getProperty("web.root");
		}
		return ROOTPATH;
	}
	public final static String getUploadPath(){
		if(StringUtils.isBlank(UPLOADPATH)){			
			UPLOADPATH = FilePathUtil.commandPath(getRootPath()+"/"+Prop.getPropValue("myjfly", "upload.path")+"/");
		}
		return UPLOADPATH;
	}
	public final static String getTempPath(){
		if(StringUtils.isBlank(TEMPPATH)){			
			TEMPPATH = FilePathUtil.commandPath(getRootPath()+"/"+Prop.getPropValue("myjfly", "temp.path")+"/");
		}
		return TEMPPATH;
	}
	public static String getSqlPath(){
		if(StringUtils.isBlank(SQLPATH)){			
			SQLPATH = FilePathUtil.commandPath(CLASSPATH+"/sql");
		}
		return SQLPATH;
	}
	

	
   /**
   * 网站图片目录
   */
   public final static String PIC_ROOT = "/pic/";

   /**
    * 网站软件目录
    */

   public final static String SOFT_ROOT = "/soft/";

   /**
    * 附件文件存放的绝对路径
    */
   public final static String ATTACH_ROOT = "/attach/";
   
   /**
    * 上传文件存放的绝对路径
    */
   public final static String UPLOAD_ROOT = "/upload/";
   
   /**
    * 缓存绝对路径
    */
   public final static String TEMP_ROOT = "/temp/";
   
   /**
    * 动态程序路径
    */
   public final static String ProgramPath = "/programs/";


   /**
    * 上传图片大小限制，单位byte
    */
   public final static  long MAX_UPLOAD_PIC_SIZE = 1000 * 1024 * 4;
   /**
    * 上传文件大小限制，单位byte
    */
   public final static  long MAX_UPLOAD_FILE_SIZE = 10 * 1024 * 1024 * 8;

   /**
    * 上传软件大小限制，单位byte
    */
   public final static  long MAX_UPLOAD_SOFT_SIZE = 100 * 1024 * 1024 * 8;

   /**
     * 错误登录次数最多3次
     */
   public final static  int MAX_LOGIN_TIMES = 3;
  
   /**
     * 错误登录3次后用户被锁10分钟
     */
   public final static int LOCK_TIME = +10;

   public final static String[] ADMIN = {"admin", "longlob"};

}
