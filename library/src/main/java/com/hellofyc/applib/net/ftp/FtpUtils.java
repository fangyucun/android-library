/*
 * Copyright (C) 2014 Jason Fang ( ijasonfang@gmail.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.applib.net.ftp;

public class FtpUtils {
	
	private static final boolean DEBUG = true;
	
	public static final String USERNAME = "kslogs";
	public static final String PASSWORD = "kkg6@123";
	public static final String URL = "121.199.63.147";
	public static final String REMOTE_PATH = "android/logs/";
	public static final int PORT = 21;
	
//	public static boolean upload(String remoteDir, String uploadFilePath) {
//		return upload(remoteDir, new File(uploadFilePath));
//	}
//
//	public static boolean upload(String remoteDir, File uploadFile) {
//		return upload(remoteDir, Arrays.asList(new File[]{uploadFile})).get(uploadFile.getPath());
//	}
//
//	public static Map<String, Boolean> upload(String remoteDir, List<File> uploadFiles) {
//		return upload(URL, PORT, USERNAME, PASSWORD, REMOTE_PATH + remoteDir, uploadFiles);
//	}
//
//	public static Map<String, Boolean> upload(String url, int port, String username, String password, String remotePath, List<File> uploadFiles) {
//		FTPClient ftpClient = new FTPClient();
//		FileInputStream fis = null;
//		try {
//			ftpClient.connect(url, port);
//			boolean loginResult = ftpClient.login(username, password);
//			int returnCode = ftpClient.getReplyCode();
//			if (returnCode == FTPReply.NOT_LOGGED_IN) {
//				Flog.e("UserName Or Password Is Wrong!");
//				return null;
//			}
//
//			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {
//				ftpClient.makeDirectory(remotePath);
//				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//				ftpClient.changeWorkingDirectory(remotePath);
//				ftpClient.setBufferSize(1024);
//				ftpClient.setControlEncoding("UTF-8");
//				ftpClient.enterLocalPassiveMode();
//				return storeFiles(ftpClient, fis, uploadFiles);
//			} else {
//				return null;
//			}
//
//		} catch (IOException e) {
//			if (DEBUG) Flog.e(e);
//		} finally {
//			try {
//				ftpClient.disconnect();
//			} catch (IOException e) {
//				Flog.e(e);
//			}
//		}
//		return null;
//	}
//
//	private static Map<String, Boolean> storeFiles(FTPClient ftpClient, FileInputStream fis, List<File> files) {
//		if (files == null || files.size() == 0) return null;
//
//		Map<String, Boolean> tempMap = new ArrayMap<String, Boolean>();
//		for (File file : files) {
//			try {
//				fis = new FileInputStream(file);
//				if (ftpClient.storeFile(StringUtils.getFileNameFromPath(file.getPath()), fis)) {
//					tempMap.put(file.getPath(), true);
//				} else {
//					tempMap.put(file.getPath(), false);
//				}
//			} catch (IOException e) {
//				Flog.e(e);
//			}
//		}
//		return tempMap;
//	}
}
