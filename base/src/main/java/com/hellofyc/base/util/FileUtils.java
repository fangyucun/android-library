/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */package com.hellofyc.base.util;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * @author Jason Fang
 * Create on 2014年11月22日 下午2:10:12
 */
public final class FileUtils {
	private static final boolean DEBUG = true;

	public static void emptyDir(File file) {
		if (!isDirExists(file)) return;
		
		deleteFile(file);
	}
	
	public static byte[] getBytes(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) != -1) {
            baos.write(bytes, 0, length);
        }
        IoUtils.close(baos, fis);
        return baos.toByteArray();
	}

	public static byte[] readFile(File file, int offset, long length) {
		if (!isFileExists(file)) {
			FLog.e("file not exists!");
			return null;
		}

		if (offset < 0) {
			FLog.e("readFile offset cannot below 0");
			return null;
		}

		if (length < -1) {
			FLog.e("readFile length cannot below -1");
			return null;
		}

		if (length == -1) {
			length = file.length();
		}

		if (offset + length > file.length()) {
			FLog.e("readFile offset plus length more than file length!");
			return null;
		}

		byte[] bytes;
		RandomAccessFile raFile = null;
		try {
			raFile = new RandomAccessFile(file, "r");
			bytes = new byte[(int)length];
			raFile.seek(offset);
			raFile.readFully(bytes);
		} catch (Exception e) {
            if (DEBUG) FLog.e(e);
		} finally {
			IoUtils.close(raFile);
		}
		return null;
	}

	public static byte[] readFile(String filePath, int offset, long length) {
		if (TextUtils.isEmpty(filePath)) {
			FLog.e("filePath is empty!");
			return null;
		}
		return readFile(new File(filePath), offset, length);
	}

	public static File getDirectory (String path) {
		File file = new File(path);
		if (createDir(path)) {
			return file;
		}
		return null;
	}

	/**
	 * 文件是否存在
	 */
	public static File checkFileExists(String filePath) {
		if (TextUtils.isEmpty(filePath)) return null;

		File file = new File(filePath);
		if (isFileExists(file)) {
			return file;
		}
		return null;
	}

	public static boolean isFileExists(File file) {
		return file != null && file.exists() && file.isFile();
	}

	public static boolean isDirExists(File file) {
		return file != null && file.exists() && file.isDirectory();
	}

	/**
	 * 目录是否存在
	 */
	public static boolean isDirExists(String dirPath) {
		return !TextUtils.isEmpty(dirPath) && isDirExists(new File(dirPath));
	}

	public static boolean createDir(String path) {
		return createDir(new File(path));
	}

	/**
     * 创建文件夹
     */
    public static boolean createDir(File dir) {
    	if (dir == null) return false;

    	if (!dir.exists()) {
    		return dir.mkdirs();
    	} else if (dir.isFile()) {
			deleteFile(dir);
			return dir.mkdirs();
    	} else {
    		return true;
    	}
    }

    /**
     * @see #createFilesDir(File, boolean)
     */
    public synchronized static File createFilesDir(File file) {
        return createFilesDir(file, false);
    }

    /**
     * 创建文件夹
     */
    public synchronized static File createFilesDir(File file, boolean isDeleteSameNameFile) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                if (file.exists()) {
                	if (file.isDirectory()) {
                		return file;
                	} else if (file.isFile() && isDeleteSameNameFile) {
                		if (FileUtils.deleteFile(file)) {
                			return createFilesDir(file, false);
                		}
                	}
                }
                return null;
            }
        }
        return file;
    }

    public static boolean createFile(File dir, String fileName) {
    	return dir != null && createFile(new File(dir, fileName));
    }

    /**
     * 创建文件
     */
    public static boolean createFile(String path) {
    	return createFile(new File(path));
    }

    public static boolean createFile(File file) {
    	try {
	    	if (!file.exists()) {
	    		if (createDir(file.getParent())) {
	    				return file.createNewFile();
	    		}
	    	} else if (file.isDirectory()) {
	    		deleteFile(file);
	    		return file.createNewFile();
	    	} else {
	    		return true;
	    	}
    	} catch (IOException e) {
    		if (DEBUG) FLog.e(e);
    	}
    	return false;
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        return file.delete();
    }

	public static boolean deleteFile(String path) {
		return deleteFile(new File(path));
	}

	public static File saveBitmapToFile(Bitmap bitmap, File dir, String fileName) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) return null;
		}
		if (bitmap == null) {
			return null;
		}
		File bitmapFile = new File(dir, fileName);
		if (!bitmapFile.exists()) createFile(dir, fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			return bitmapFile;
		} catch (FileNotFoundException e) {
			if (DEBUG) FLog.e(e);
		} catch (Exception e) {
			FileUtils.deleteFile(bitmapFile);
		}
		return null;
	}

	public static File writeString(String text, File dir, String fileName) {
		return writeString(text, dir, fileName, true);
	}

	public static File writeString(String text, File dir, String fileName, boolean isAppend) {
		if (createFile(dir, fileName)) {
			if (text == null) return null;

			FileWriter writer = null;
			BufferedReader reader = null;
			try {
				File file = new File(dir, fileName);
				writer = new FileWriter(file, isAppend);

				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				if (!TextUtils.isEmpty(reader.readLine())) {
					writer.write("\n");
				}
				writer.write(text);
				return file;
			} catch (IOException e) {
				FLog.e(e);
			} finally {
				IoUtils.close(writer, reader);
			}
		}
		return null;
	}

    /**
     *
     * copy file
     *
     * @param srcFile
     *            source file
     * @param destFile
     *            target file
     * @throws IOException
     */
    public static boolean copy(File srcFile, File destFile) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!destFile.exists()) {
                if (!destFile.createNewFile()) {
                    FLog.e("Create File Error!");
                    return false;
                }
            }
            inChannel = new FileInputStream(srcFile).getChannel();
            outChannel = new FileOutputStream(destFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            return true;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

	@SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static void writeByteArrayToFile(File file, byte[] bytes) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		try {
			fout.write(bytes);
		} catch (Exception e) {
            if (DEBUG) FLog.e(e);
        } finally {
			fout.close();
		}
	}

	/**
	 * 判断文件的编码格式
	 */
	public static String getTextCodeFormat(String filePath) {
		String code;
		BufferedInputStream bis = null;
		int p = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			p = (bis.read() << 8) + bis.read();
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		} finally {
			IoUtils.close(bis);
		}
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
	
	public static void zipFile(String baseDir, String fileName) throws Exception {
		List<File> fileList = getSubFiles(new File(baseDir));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
		ZipEntry entry;
		byte[] buf = new byte[1024];
		int readLen;
		InputStream is = null;
		
		for(int i = 0; i < fileList.size(); i++) {
			File f = fileList.get(i);
			entry = new ZipEntry(getAbsoluteFileName(baseDir, f));
			entry.setSize(f.length());
			entry.setTime(f.lastModified());
			zos.putNextEntry(entry);
			is = new BufferedInputStream(new FileInputStream(f));
			
			while ((readLen=is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, readLen);
			}
		}
		IoUtils.close(is, zos);
   }
   
   private static String getAbsoluteFileName(String baseDir, File realFileName) {
       File realName = realFileName;
       File base = new File(baseDir);
       String name = realName.getName();
       while (true) {  
    	   realName = realName.getParentFile();  
           if(realName == null) break;
           
           if(realName.equals(base)) {
        	   break;
           } else {  
               name = realName.getName() + "/" + name; 
           }
       }  
       return name;  
   }
   
   public static List<File> getSubFiles(File baseDir){
       List<File> fileList = new ArrayList<>();
       File[] tmpList = baseDir.listFiles();
       for (File file : tmpList) {
           if (file.isFile()) {
               fileList.add(file);
           }
           if (file.isDirectory()) {
               fileList.addAll(getSubFiles(file));
           }
       }
       return fileList;
   }
	
	/**
     * 解压一个压缩文档 到指定位置
     * 
     * @param inFilePath 压缩包的名字
     * @param outFilePath 指定的路径
     * @throws Exception
     */
    public static void unzipFile(String inFilePath, String outFilePath) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(inFilePath));
        ZipEntry zipEntry;
        String szName;

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                FileUtils.createDir(outFilePath + File.separator + szName);
            } else {
                FileUtils.createFile(outFilePath + File.separator + szName);
                FileOutputStream out = new FileOutputStream(new File(outFilePath + File.separator + szName));
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inZip.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 创建路径
     */
    public static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                cur = new File(segment);
            } else if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

	private FileUtils() {/* Do not new me */}
}
