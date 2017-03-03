/**
 * Copyright Shanghai COS Software Co., Ltd.
 * All right reserved
 */
package com.tww.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件相关操作辅助类
 * <p>
 * <br>
 * 
 * @Description:
 * @Company: Shanghai COS Software
 * @Copyright: Copyright (c)2011
 * 
 * @author Platform Development Group
 * @date 2010年06月24日
 * @version 1.0
 * 
 * @Modification History
 * @----------------------------------------------------
 * @2012-10-10 1.0 create
 * 
 * 
 */
public class FileUtil {

	private static final String FOLDER_SEPARATOR = File.separator;
	private static final char EXTENSION_SEPARATOR = '.';

	/**
	 * 工具类不可实例化
	 */
	private FileUtil() {

	}

	/**
	 * 从文件路径中抽取文件名,不包含扩展名
	 * <p>
	 * 例如. "mypath/myfile.txt" -> "myfile"<br>
	 * 
	 * @param 文件路径
	 * @return 如果path为null，直接返回null。
	 */
	public static String getFileSimpleName(String path) {
		if (path == null) {
			return null;
		}
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}
		return path.substring(folderIndex + 1, extIndex);
	}

	/**
	 * 从文件路径中抽取文件的扩展名
	 * <p>
	 * 例如. "mypath/myfile.txt" -> "txt"<br>
	 * 
	 * @param 文件路径
	 * @return 如果path为null，直接返回null。
	 */
	public static String getFileExtension(String path) {
		if (path == null) {
			return null;
		}
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}
		return path.substring(extIndex + 1);
	}

	/**
	 * 从文件路径中抽取文件名
	 * <p>
	 * 例如： "mypath/myfile.txt" -> "myfile.txt"<br>
	 * 
	 * @param path
	 *            文件路径。
	 * @return 抽取出来的文件名, 如果path为null，直接返回null。
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1)
				: path);
	}

	/**
	 * 创建目录或者文件
	 * 
	 * @param path
	 *            路径
	 * @param isFile
	 *            是否是文件
	 */
	public static File create(String path, boolean isFile) {
		File file = new File(path);
		create(file, isFile);
		return file;
	}

	/**
	 * 创建目录或者文件
	 * 
	 * @param file
	 *            File
	 * @param isFile
	 *            是否是文件
	 */
	public static void create(File file, boolean isFile) {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				create(file.getParentFile(), false);
			}
			if (isFile) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				file.mkdir();
			}
		}
	}

	/**
	 * 删除文件
	 * <p>
	 * <br>
	 * 
	 * @param file
	 *            文件
	 */
	public static void delete(String file) {
		delete(new File(file));
	}

	/**
	 * 删除文件，内部递归使用
	 * <p>
	 * <br>
	 * 
	 * @param file
	 *            文件
	 * @return boolean true 删除成功，false 删除失败。
	 */
	private static void delete(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		// 单文件
		if (!file.isDirectory()) {
			boolean delFlag = file.delete();
			if (!delFlag) {
				throw new RuntimeException(file.getPath() + ",删除失败！");
			} else {
				return;
			}
		}
		// 删除子目录
		for (File child : file.listFiles()) {
			delete(child);
		}
		// 删除自己
		file.delete();
	}

	/**
	 * 删除单个文件
	 * <p>
	 * <br>
	 * 
	 * @param file
	 */
	public void deleteFile(String file) {
		deleteFile(new File(file));
	}

	/**
	 * 删除单个文件
	 * <p>
	 * <br>
	 * 
	 * @param file
	 */
	public boolean deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			return file.delete();
		} else {
			return false;
		}
	}

	/**
	 * 复制文件或者文件夹。
	 * <p>
	 * <br>
	 * 
	 * @param inputFile
	 *            源文件
	 * @param outputFile
	 *            目的文件
	 * @param isOverWrite
	 *            是否覆盖(只针对文件)
	 * @throws IOException
	 */
	public static void copy(File inputFile, File outputFile, boolean isOverWrite)
			throws IOException {
		if (!inputFile.exists()) {
			throw new RuntimeException(inputFile.getPath() + ",源目录不存在!");
		}
		copyDir(inputFile, outputFile, isOverWrite);
	}

	/**
	 * 递归复制所有文件。
	 * <p>
	 * <br>
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param isOverWrite
	 * @throws IOException
	 */
	private static void copyDir(File inputFile, File outputFile,
			boolean isOverWrite) throws IOException {
		// 是个文件。
		if (inputFile.isFile()) {
			copySimpleFile(inputFile, outputFile, isOverWrite);
		} else {
			// 文件夹
			if (!outputFile.exists()) {
				outputFile.mkdir();
			}
			// 循环子文件夹
			for (File child : inputFile.listFiles()) {
				copy(child, new File(outputFile.getPath() + FOLDER_SEPARATOR
						+ child.getName()), isOverWrite);
			}
		}
	}

	/**
	 * 功能：copy单个文件
	 * <p>
	 * <br>
	 * 
	 * @param inputFile
	 *            源文件
	 * @param outputFile
	 *            目标文件
	 * @param isOverWrite
	 *            是否允许覆盖
	 * @throws IOException
	 */
	private static void copySimpleFile(File inputFile, File outputFile,
			boolean isOverWrite) throws IOException {
		// 目标文件已经存在
		if (outputFile.exists()) {
			if (isOverWrite) {
				if (!outputFile.delete()) {
					throw new RuntimeException(outputFile.getPath() + ",无法覆盖！");
				}
			} else {
				// 不允许覆盖
				return;
			}
		}
		InputStream in = new FileInputStream(inputFile);
		OutputStream out = new FileOutputStream(outputFile);
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		in.close();
		out.close();
	}

	/**
	 * 保存文件。
	 * <p>
	 * <br>
	 * 
	 * @param content
	 *            字节
	 * @param file
	 *            保存到的文件
	 * @throws IOException
	 */
	public static void save(byte[] content, File file) throws IOException {
		if (file == null) {
			throw new RuntimeException("保存文件不能为空");
		}
		if (content == null) {
			throw new RuntimeException("文件流不能为空");
		}
		InputStream is = new ByteArrayInputStream(content);
		save(is, file);
		is.close();
	}

	/**
	 * 保存文件
	 * <p>
	 * <br>
	 * 
	 * @param streamIn
	 *            文件流
	 * @param file
	 *            保存到的文件
	 * @throws IOException
	 */
	public static void save(InputStream streamIn, File file) throws IOException {
		if (file == null) {
			throw new RuntimeException("保存文件不能为空");
		}
		if (streamIn == null) {
			throw new RuntimeException("文件流不能为空");
		}
		// 输出流
		OutputStream streamOut = null;
		// 文件夹不存在就创建。
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		streamOut = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[2048];
		while ((bytesRead = streamIn.read(buffer, 0, buffer.length)) != -1) {
			streamOut.write(buffer, 0, bytesRead);
		}
		streamOut.flush();
		streamOut.close();
		// streamIn.close();
	}

	/**
	 * 移动文件，是否覆盖
	 * 
	 * @param filePath
	 * @param toPath
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(String filePath, String toPath,
			boolean override) throws IOException {
		// 创建目录
		File toDir = new File(toPath);
		if (!toDir.exists()) {
			toDir.mkdirs();
		}
		File file = new File(filePath);
		// Move file to new directory
		File newFile = new File(toPath, file.getName());
		if (override && newFile.exists()) {
			newFile.delete();
		}
		boolean success = file.renameTo(newFile);
		return success;
	}

	/**
	 * 列出文件夹下的子文件夹名or文件名
	 * 
	 * @param rootPath
	 * @param listFile
	 *            true列文件,false表目录
	 * @return
	 * @throws Exception
	 */
	public static List<String> list(String rootPath, boolean listFile)
			throws Exception {
		List<String> fileList = new ArrayList<String>();

		File[] fs = new File(rootPath).listFiles();
		if ((fs == null) || (fs.length <= 0)) {
			return fileList;
		}
		for (File f : fs) {
			if (f.isFile()) {
				if (listFile) {
					fileList.add(f.getName());
				}
			} else {
				if (!listFile) {
					fileList.add(f.getName());
				}
			}
		}
		return fileList;
	}

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();

		String filePath = "C://cosw//123//123.txt";
		filePath = "C://cosw//123";
		filePath = "C://cosw//123.txt";
		filePath = "C://cosw";

		// create(filePath, false);
		// delete(filePath);

		List<String> list = list(filePath, true);
		for (String s : list) {
			System.out.println(" ---------file:" + s);
		}

		long end = System.currentTimeMillis();
		System.out.println(" time:" + ((end - begin) / 1000) + "s");
	}
}
