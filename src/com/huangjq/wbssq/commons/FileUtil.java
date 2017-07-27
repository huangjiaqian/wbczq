package com.huangjq.wbssq.commons;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 封装了些文件相关的操作
 */
public final class FileUtil {
    /**
     * Buffer的大小
     */
    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

    public final static Map<String,String> FILE_TYPE_MAP = new HashMap<String,String>();

    static {
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE_MAP.put("mpg", "000001BA");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
    }
    
    
    public static MessageDigest MD5 = null;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        }
    }

    /**
     * 获取文件的md5
     * @param file
     * @return
     */
    public static String fileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new BigInteger(1, MD5.digest()).toString(16);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public final static int countLines(File file) {
    	LineNumberReader rf = null;
    	try{
        	rf = new LineNumberReader(new FileReader(file));
            long fileLength = file.length();
            rf.skip(fileLength);
            return rf.getLineNumber();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			try {
				rf.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return 0;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file 需要出来的文件
     * @return 包含所有行的list
     */
    public final static List<String> lines(File file) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
        	  reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return list;
    }
    
    /**
     * 读取文件
     * @param file
     * @param encoding
     * @return
     */
    public final static String loadFile(File file, String encoding){
    	StringBuffer sb = new StringBuffer();
    	InputStreamReader isr = null;
        try{
        	isr = new InputStreamReader(new FileInputStream(file), encoding);
            int len = 0;
            char[] buffter = new char[1024];
        	while((len = isr.read(buffter)) > 0){
        		sb.append(new String(buffter, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
    }
    
    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     需要处理的文件
     * @param encoding 指定读取文件的编码
     * @return 包含所有行的list
     */
    public final static List<String> lines(File file, String encoding) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try{
        	reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				reader.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file  处理的文件
     * @param lines 需要读取的行数
     * @return 包含制定行的list
     */
    public final static List<String> lines(File file, int lines) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
        	  reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				reader.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file     需要处理的函数
     * @param lines    需要处理的行还俗
     * @param encoding 指定读取文件的编码
     * @return 包含制定行的list
     */
    public final static List<String> lines(File file, int lines, String encoding) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try{
        	  reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				reader.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return list;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file 需要处理的函数
     * @param str  添加的子字符串
     * @return 是否成功
     * @throws IOException 
     */
    public final static boolean appendLine(File file, String str) throws IOException {
    	RandomAccessFile randomFile = null;
    	try{
        	  randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(File.separator + str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			randomFile.close();
		}
        return false;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file     需要处理的文件
     * @param str      添加的字符串
     * @param encoding 指定写入的编码
     * @return 是否成功
     */
    public final static boolean appendLine(File file, String str, String encoding) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        RandomAccessFile randomFile = null;
        try{
        	  randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write((lineSeparator + str).getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    public static void clearFile(File file){
        try {
        	if(file != null && file.exists() && file.isFile()){
        		FileWriter fileWriter =new FileWriter(file);
        		fileWriter.write("");
        		fileWriter.flush();
        		fileWriter.close();        		
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * 写入文件内容
	 * @param filePath
	 * @param charset
	 * @return
	 */
	public static boolean write(String str,String filePath,String charset,Boolean isAppend){
		return write(str, new File(filePath), charset, isAppend);
	}
	 /**
		 * 写入文件内容
		 * @param filePath
		 * @param charset
		 * @return
		 */
		public static boolean write(String str,File file,String charset,Boolean isAppend){
			RandomAccessFile raf = null;
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				raf = new RandomAccessFile(file, "rw");
				if(isAppend){
					raf.seek(raf.length());
				}else{
					raf.seek(0L);
				}
				raf.write(str.getBytes(charset));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(raf != null){
						raf.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	
    /**
     * 将字符串写入到文件中
     */
    public final static boolean write(File file, String str) {
    	RandomAccessFile randomFile = null;
    	try{
        	randomFile = new RandomAccessFile(file, "rw");
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    /**
     * 将字符串以追加的方式写入到文件中
     */
    public final static boolean writeAppend(File file, String str) {
    	RandomAccessFile randomFile = null;
    	try{
        	   randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    /**
     * 将字符串以制定的编码写入到文件中
     */
    public final static boolean write(File file, String str, String encoding) {
    	RandomAccessFile randomFile = null;
    	try{
        	randomFile = new RandomAccessFile(file, "rw");
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    /**
     * 将字符串以追加的方式以制定的编码写入到文件中
     */
    public final static boolean writeAppend(File file, String str, String encoding) {
    	RandomAccessFile randomFile = null;
    	try{
        	   randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    /**
     * 快速清空一个超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean cleanFile(File file) {
    	FileWriter fw = null;
    	try{
        	  fw = new FileWriter(file);
            fw.write("");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				fw.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }

    /**
     * 获取文件的Mime类型
     *
     * @param file 需要处理的文件
     * @return 返回文件的mime类型
     * @throws java.io.IOException
     */
    public final static String mimeType(String file) throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    /**
     * 获取文件的类型
     * <p/>
     * Summary:只利用文件头做判断故不全
     *
     * @param file 需要处理的文件
     * @return 文件类型
     */

    public final static String getFileType(File file) {
        String filetype = null;
        byte[] b        = new byte[50];
        InputStream is = null;
        try{
        	  is = new FileInputStream(file);
            is.read(b);
            filetype = getFileTypeByStream(b);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return filetype;
    }
    /**
     * Created on 2010-7-1
     * <p>Discription:[getFileTypeByStream]</p>
     *
     * @param b
     * @return fileType
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileTypeByStream(byte[] b) {
        String                             filetypeHex   = String.valueOf(getFileHexString(b));
        Iterator<Map.Entry<String,String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Map.Entry<String,String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Created on 2010-7-1
     * <p>Discription:[getFileHexString]</p>
     *
     * @param b
     * @return fileTypeHex
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
     * 获取文件最后的修改时间
     *
     * @param file 需要处理的文件
     * @return 返回文件的修改时间
     */
    public final static Date modifyTime(File file) {
        return new Date(file.lastModified());
    }




    /**
     * 复制文件
     *
     * @param resourcePath 源文件
     * @param targetPath   目标文件
     * @return 是否成功
     */
    public final static boolean copy(String resourcePath, String targetPath) {
        File file = new File(resourcePath);
        return copy(file, targetPath);
    }

    /**
     * 复制文件
     * 通过该方式复制文件文件越大速度越是明显
     *
     * @param file       需要处理的文件
     * @param targetFile 目标文件
     * @return 是否成功
     */
    public final static boolean copy(File file, String targetFile) {
    	FileInputStream fin = null;
        FileOutputStream fout = null;
    	try{
        	  fin = new FileInputStream(file);
              fout = new FileOutputStream(new File(targetFile));
            FileChannel in = fin.getChannel();
            FileChannel out = fout.getChannel();
            //设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                //准备写入，防止其他读取，锁住文件
                buffer.flip();
                out.write(buffer);
                //准备读取。将缓冲区清理完毕，移动文件内部指针
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				fin.close();
				fout.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
        return false;
    }



    /**
     * 利用简单的文件头字节特征探测文件编码
     *
     * @param file 需要处理的文件
     * @return UTF-8 Unicode UTF-16BE GBK
     */
    public static String simpleEncoding(String fileName) {
        int p = 0;
        BufferedInputStream bin = null;
        try{
        	bin = new BufferedInputStream(new FileInputStream(fileName));
            p = (bin.read() << 8) + bin.read();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
			try {
				bin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }

    /**
     * 创建多级目录
     *
     * @param paths 需要创建的目录
     * @return 是否成功
     */
    public final static boolean createPaths(String paths) {
        File dir = new File(paths);
        return !dir.exists() && dir.mkdir();
    }

    /**
     * 创建文件支持多级目录
     *
     * @param filePath 需要创建的文件
     * @return 是否成功
     */
    public final static boolean createFiles(String filePath) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 删除一个文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 删除一个目录
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteDir(File file) {
        List<File> files = listFileAll(file);
        if (valid(Arrays.asList(files))) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f);
                } else {
                    deleteFile(f);
                }
            }
        }
        return file.delete();
    }
    
    /**
     * 判断集合的有效性
     */
    public final static boolean valid(Collection<?> col) {
        return !(col == null || col.isEmpty());
    }

    /**
     * 快速的删除超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteBigFile(File file) {
        return cleanFile(file) && file.delete();
    }


    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public final static void copyDir(String filePath, String targetPath) {
        File file = new File(filePath);
        copyDir(file, targetPath);
    }

    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public final static void copyDir(File filePath, String targetPath) {
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            createPaths(targetPath);
        }
        File[] files = filePath.listFiles();
        
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                String path = file.getName();
                if (file.isDirectory()) {
                    copyDir(file, targetPath + "/" + path);
                } else {
                    copy(file, targetPath + "/" + path);
                }
            }
        }
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 包含所有文件的的list
     */
    public final static List<File> listFile(String path) {
        File file = new File(path);
        return listFile(file);
    }

    /**
     * 罗列指定路径下的全部文件
     * @param path 需要处理的文件
     * @param child 是否罗列子文件
     * @return 包含所有文件的的list
     */
    public final static List<File> listFile(String path,boolean child){
        return listFile(new File(path),child);
    }


    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public final static List<File> listFile(File path) {
        List<File> list = new ArrayList<File>();
        File[] files = path.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件
     * @param path 指定的路径
     * @param child 是否罗列子目录
     * @return
     */
    public final static List<File> listFile(File path,boolean child){
        List<File> list = new ArrayList<File>();
        File[] files = path.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (child && file.isDirectory()) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public final static List<File> listFileAll(File path) {
        List<File> list = new ArrayList<File>();
        File[] files = path.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFileAll(file));
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path   需要处理的文件
     * @param filter 处理文件的filter
     * @return 返回文件列表
     */
    public final static List<File> listFileFilter(File path, FilenameFilter filter) {
        List<File> list = new ArrayList<File>();
        File[] files = path.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, filter));
                } else {
                    if (filter.accept(file.getParentFile(), file.getName())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取指定目录下的特点文件,通过后缀名过滤
     *
     * @param dirPath  需要处理的文件
     * @param postfixs 文件后缀
     * @return 返回文件列表
     */
    public final static List<File> listFileFilter(File dirPath, final String postfixs) {
        /*
        如果在当前目录中使用Filter讲只罗列当前目录下的文件不会罗列孙子目录下的文件
        FilenameFilter filefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(postfixs);
            }
        };
        */
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, postfixs));
                } else {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(postfixs.toLowerCase())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下搜寻文个文件
     *
     * @param dirPath  搜索的目录
     * @param fileName 搜索的文件名
     * @return 返回文件列表
     */
    public final static List<File> searchFile(File dirPath, String fileName) {
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, fileName));
                } else {
                    String Name = file.getName();
                    if (Name.equals(fileName)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查找符合正则表达式reg的的文件
     *
     * @param dirPath 搜索的目录
     * @param reg     正则表达式
     * @return 返回文件列表
     */
    public final static List<File> searchFileReg(File dirPath, String reg) {
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if (valid(Arrays.asList(files))) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, reg));
                } else {
                    String Name = file.getName();
                    if (RegUtil.isMatche(Name, reg)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    public final static String suffix(File file){
        String fileName=file.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
    public final static String suffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
    /**
     * 获取文件名无后缀
     * @param file
     * @return
     */
    public final static String fileNameNosuffix(File file){
        String fileName=file.getName();
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
    public final static String fileNameNosuffix(String fileName){
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
    
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param file 需要修改最后访问时间的文件。
     * @since  1.0
     */
    public static void touch(File file) {
      long currentTime = System.currentTimeMillis();
      if (!file.exists()) {
        System.err.println("file not found:" + file.getName());
        System.err.println("Create a new file:" + file.getName());
        try {
          if (file.createNewFile()) {
            System.out.println("Succeeded!");
          }
          else {
            System.err.println("Create file failed!");
          }
        }
        catch (IOException e) {
          System.err.println("Create file failed!");
          e.printStackTrace();
        }
      }
      boolean result = file.setLastModified(currentTime);
      if (!result) {
        System.err.println("touch failed: " + file.getName());
      }
    }
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param fileName 需要修改最后访问时间的文件的文件名。
     * @since  1.0
     */
    public static void touch(String fileName) {
      File file = new File(fileName);
      touch(file);
    }
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param files 需要修改最后访问时间的文件数组。
     * @since  1.0
     */
    public static void touch(File[] files) {
      for (int i = 0; i < files.length; i++) {
        touch(files[i]);
      }
    }

    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param fileNames 需要修改最后访问时间的文件名数组。
     * @since  1.0
     */
    public static void touch(String[] fileNames) {
      File[] files = new File[fileNames.length];
      for (int i = 0; i < fileNames.length; i++) {
        files[i] = new File(fileNames[i]);
      }
      touch(files);
    }
    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param fileName 要创建的目录的目录名
     * @return 完全创建成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean makeDirectory(String fileName) {
      File file = new File(fileName);
      return makeDirectory(file);
    }
    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directory 要清空的目录
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
     * @since  1.0
     */
    public static boolean emptyDirectory(File directory) {
      boolean result = true;
      File[] entries = directory.listFiles();
      for (int i = 0; i < entries.length; i++) {
        if (!entries[i].delete()) {
          result = false;
        }
      }
      return result;
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directoryName 要清空的目录的目录名
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean emptyDirectory(String directoryName) {
      File dir = new File(directoryName);
      return emptyDirectory(dir);
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean deleteDirectory(String dirName) {
      return deleteDirectory(new File(dirName));
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean deleteDirectory(File dir) {
      if ( (dir == null) || !dir.isDirectory()) {
        throw new IllegalArgumentException("Argument " + dir +
                                           " is not a directory. ");
      }

      File[] entries = dir.listFiles();
      int sz = entries.length;

      for (int i = 0; i < sz; i++) {
        if (entries[i].isDirectory()) {
          if (!deleteDirectory(entries[i])) {
            return false;
          }
        }
        else {
          if (!entries[i].delete()) {
            return false;
          }
        }
      }

      if (!dir.delete()) {
        return false;
      }
      return true;
    }

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param fileName 要列出的目录的目录名
     * @return 目录内容的文件数组。
     * @since  1.0
     */
    /*public static File[] listAll(String fileName) {
      return listAll(new File(fileName));
    }*/

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param file 要列出的目录
     * @return 目录内容的文件数组。
     * @since  1.0
     */
    /*public static File[] listAll(File file) {
      ArrayList list = new ArrayList();
      File[] files;
      if (!file.exists() || file.isFile()) {
        return null;
      }
      list(list, file, new AllFileFilter());
      list.remove(file);
      files = new File[list.size()];
      list.toArray(files);
      return files;
    }*/

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param file 要列出的目录
     * @param filter 过滤器
     * @return 目录内容的文件数组。
     * @since  1.0
     */
    public static File[] listAll(File file,
                                 javax.swing.filechooser.FileFilter filter) {
      ArrayList<File> list = new ArrayList<File>();
      File[] files;
      if (!file.exists() || file.isFile()) {
        return null;
      }
      list(list, file, filter);
      files = new File[list.size()];
      list.toArray(files);
      return files;
    }

    /**
     * 将目录中的内容添加到列表。
     * @param list 文件列表
     * @param filter 过滤器
     * @param file 目录
     */
    private static void list(ArrayList<File> list, File file,
                             javax.swing.filechooser.FileFilter filter) {
      if (filter.accept(file)) {
        list.add(file);
        if (file.isFile()) {
          return;
        }
      }
      if (file.isDirectory()) {
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
          list(list, files[i], filter);
        }
      }

    }

    /**
     * 返回文件的URL地址。
     * @param file 文件
     * @return 文件对应的的URL地址
     * @throws MalformedURLException
     * @since  1.0
     * @deprecated 在实现的时候没有注意到File类本身带一个toURL方法将文件路径转换为URL。
     *             请使用File.toURL方法。
     */
    public static URL getURL(File file) throws MalformedURLException {
      String fileURL = "file:/" + file.getAbsolutePath();
      URL url = new URL(fileURL);
      return url;
    }

    /**
     * 从文件路径得到文件名。
     * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since  1.0
     */
    public static String getFileName(String filePath) {
      File file = new File(filePath);
      return file.getName();
    }

    /**
     * 从文件名得到文件绝对路径。
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since  1.0
     */
    public static String getFilePath(String fileName) {
      File file = new File(fileName);
      return file.getAbsolutePath();
    }

    /**
     * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
     * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
     * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
     * @param filePath 转换前的路径
     * @return 转换后的路径
     * @since  1.0
     */
    public static String toUNIXpath(String filePath) {
      return filePath.replace('\\', '/');
    }

    /**
     * 从文件名得到UNIX风格的文件绝对路径。
     * @param fileName 文件名
     * @return 对应的UNIX风格的文件路径
     * @since  1.0
     * @see #toUNIXpath(String filePath) toUNIXpath
     */
    public static String getUNIXfilePath(String fileName) {
      File file = new File(fileName);
      return toUNIXpath(file.getAbsolutePath());
    }

    /**
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     * @param fileName 文件名
     * @return 文件名中的类型部分
     * @since  1.0
     */
    public static String getTypePart(String fileName) {
      int point = fileName.lastIndexOf('.');
      int length = fileName.length();
      if (point == -1 || point == length - 1) {
        return "";
      }
      else {
        return fileName.substring(point + 1, length);
      }
    }


    /**
     * 得到文件的名字部分。
     * 实际上就是路径中的最后一个路径分隔符后的部分。
     * @param fileName 文件名
     * @return 文件名中的名字部分
     * @since  1.0
     */
    public static String getNamePart(String fileName) {
      int point = getPathLsatIndex(fileName);
      int length = fileName.length();
      if (point == -1) {
        return fileName;
      }
      else if (point == length - 1) {
        int secondPoint = getPathLsatIndex(fileName, point - 1);
        if (secondPoint == -1) {
          if (length == 1) {
            return fileName;
          }
          else {
            return fileName.substring(0, point);
          }
        }
        else {
          return fileName.substring(secondPoint + 1, point);
        }
      }
      else {
        return fileName.substring(point + 1);
      }
    }

    /**
     * 得到文件名中的父路径部分。
     * 对两种路径分隔符都有效。
     * 不存在时返回""。
     * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
     * @param fileName 文件名
     * @return 父路径，不存在或者已经是父目录时返回""
     * @since  1.0
     */
    public static String getPathPart(String fileName) {
      int point = getPathLsatIndex(fileName);
      int length = fileName.length();
      if (point == -1) {
        return "";
      }
      else if (point == length - 1) {
        int secondPoint = getPathLsatIndex(fileName, point - 1);
        if (secondPoint == -1) {
          return "";
        }
        else {
          return fileName.substring(0, secondPoint);
        }
      }
      else {
        return fileName.substring(0, point);
      }
    }

    /**
     * 得到路径分隔符在文件路径中首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
     * @since  1.0
     */
    public static int getPathIndex(String fileName) {
      int point = fileName.indexOf('/');
      if (point == -1) {
        point = fileName.indexOf('\\');
      }
      return point;
    }

    /**
     * 得到路径分隔符在文件路径中指定位置后首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
     * @since  1.0
     */
    public static int getPathIndex(String fileName, int fromIndex) {
      int point = fileName.indexOf('/', fromIndex);
      if (point == -1) {
        point = fileName.indexOf('\\', fromIndex);
      }
      return point;
    }

    /**
     * 得到路径分隔符在文件路径中最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
     * @since  1.0
     */
    public static int getPathLsatIndex(String fileName) {
      int point = fileName.lastIndexOf('/');
      if (point == -1) {
        point = fileName.lastIndexOf('\\');
      }
      return point;
    }

    /**
     * 得到路径分隔符在文件路径中指定位置前最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
     * @since  1.0
     */
    public static int getPathLsatIndex(String fileName, int fromIndex) {
      int point = fileName.lastIndexOf('/', fromIndex);
      if (point == -1) {
        point = fileName.lastIndexOf('\\', fromIndex);
      }
      return point;
    }

    /**
     * 将文件名中的类型部分去掉。
     * @param filename 文件名
     * @return 去掉类型部分的结果
     * @since  1.0
     */
    public static String trimType(String filename) {
      int index = filename.lastIndexOf(".");
      if (index != -1) {
        return filename.substring(0, index);
      }
      else {
        return filename;
      }
    }
    /**
     * 得到相对路径。
     * 文件名不是目录名的子节点时返回文件名。
     * @param pathName 目录名
     * @param fileName 文件名
     * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
     * @since  1.0
     */
    public static String getSubpath(String pathName,String fileName) {
      int index = fileName.indexOf(pathName);
      if (index != -1) {
        return fileName.substring(index + pathName.length() + 1);
      }
      else {
        return fileName;
      }
    }

    /**
     * 检查给定目录的存在性
     * 保证指定的路径可用，如果指定的路径不存在，那么建立该路径，可以为多级路径
     * @param path
     * @return 真假值
     * @since  1.0
     */
     public static final boolean pathValidate(String path)
     {
       //String path="d:/web/www/sub";
       //System.out.println(path);
       //path = getUNIXfilePath(path);

       //path = ereg_replace("^\\/+", "", path);
       //path = ereg_replace("\\/+$", "", path);
       String[] arraypath = path.split("/");
       String tmppath = "";
       for (int i = 0; i < arraypath.length; i++)
       {
         tmppath += "/" + arraypath[i];
         File d = new File(tmppath.substring(1));
         if (!d.exists()) { //检查Sub目录是否存在
             System.out.println(tmppath.substring(1));
           if (!d.mkdir())
           {
             return false;
           }
         }
       }
       return true;
     }

     /**
      * 读取文件的内容
      * 读取指定文件的内容
      * @param path 为要读取文件的绝对路径
      * @return 以行读取文件后的内容。
      * @since  1.0
      */
     public static final String getFileContent(String path) throws IOException
     {
       String filecontent = "";
       try {
         File f = new File(path);
         if (f.exists()) {
           FileReader fr = new FileReader(path);
           BufferedReader br = new BufferedReader(fr); //建立BufferedReader对象，并实例化为br
           String line = br.readLine(); //从文件读取一行字符串
           //判断读取到的字符串是否不为空
           while (line != null) {
             filecontent += line + "\n";
             line = br.readLine(); //从文件中继续读取一行数据
           }
           br.close(); //关闭BufferedReader对象
           fr.close(); //关闭文件
         }

       }
       catch (IOException e) {
         throw e;
       }
       return filecontent;
     }

     /**
      * 根据内容生成文件
      * @param path要生成文件的绝对路径，
      * @param 文件的内容。
      * @return 真假值
      * @since  1.0
      */
     public static final boolean genModuleTpl(String path, String modulecontent)  throws IOException
     {

       path = getUNIXfilePath(path);
       String[] patharray = path.split("\\/");
       String modulepath = "";
       for (int i = 0; i < patharray.length - 1; i++) {
         modulepath += "/" + patharray[i];
       }
       File d = new File(modulepath.substring(1));
       if (!d.exists()) {
         if (!pathValidate(modulepath.substring(1))) {
           return false;
         }
       }
       try {
         FileWriter fw = new FileWriter(path); //建立FileWriter对象，并实例化fw
         //将字符串写入文件
         fw.write(modulecontent);
         fw.close();
       }
       catch (IOException e) {
         throw e;
       }
       return true;
     }

     /**
      * 获取图片文件的扩展名（发布系统专用）
      * @param picname 为图片名称加上前面的路径不包括扩展名
      * @return 图片的扩展名
      * @since  1.0
      */
     public static final String getPicExtendName(String pic_path)
     {
       pic_path = getUNIXfilePath(pic_path);
       String pic_extend = "";
       if (isFileExist(pic_path + ".gif"))
       {
         pic_extend = ".gif";
       }
       if (isFileExist(pic_path + ".jpeg"))
       {
         pic_extend = ".jpeg";
       }
       if (isFileExist(pic_path + ".jpg"))
       {
         pic_extend = ".jpg";
       }
       if (isFileExist(pic_path + ".png"))
       {
         pic_extend = ".png";
       }
       return pic_extend; //返回图片扩展名
     }
     //拷贝文件
     public static final boolean CopyFile(File in, File out) throws Exception {
         try {
             FileInputStream fis = new FileInputStream(in);
             FileOutputStream fos = new FileOutputStream(out);
             byte[] buf = new byte[1024];
             int i = 0;
             while ((i = fis.read(buf)) != -1) {
                 fos.write(buf, 0, i);
             }
             fis.close();
             fos.close();
             return true;
         } catch (IOException ie) {
             ie.printStackTrace();
             return false;
         }
     }
     //拷贝文件
     public static final boolean CopyFile(String infile, String outfile) throws Exception {
         try {
             File in = new File(infile);
             File out = new File(outfile);
             return CopyFile(in, out);
         } catch (IOException ie) {
             ie.printStackTrace();
             return false;
         }

     }
     
     
     /**
      * 判断指定的文件是否存在。
      * @param fileName 要判断的文件的文件名
      * @return 存在时返回true，否则返回false。
      * @since  1.0
      */
     public static boolean isFileExist(String fileName) {
       return new File(fileName).isFile();
     }

     /**
      * 创建指定的目录。
      * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
      * <b>注意：可能会在返回false的时候创建部分父目录。</b>
      * @param file 要创建的目录
      * @return 完全创建成功时返回true，否则返回false。
      * @since  1.0
      */
     public static boolean makeDirectory(File file) {
       File parent = file.getParentFile();
       if (parent != null) {
         return parent.mkdirs();
       }
       return false;
     }
}