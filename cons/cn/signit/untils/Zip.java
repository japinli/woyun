package cn.signit.untils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
	
	/**
	 * 压缩给定的文件到 *zipFileName* 中
	 * 
	 * @param zipFileName
	 *            压缩后的文件名，函数会在该文件名后添加 zip 后缀
	 * @param filenames
	 *            待压缩文件名
	 * @return true - 成功, false - 失败
	 */
	public static boolean zip(String zipFileName, String... filenames) {
		try {
			ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName + ".zip"));

			for (String name : filenames) {
				File file = new File(name);
				if (file != null) {
					zip(outputStream, file, file.getName());
				}
			}

			outputStream.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 递归压缩
	 * 
	 * @param out
	 *            Zip 的输出流
	 * @param file
	 *            待压缩的文件对象
	 * @param filename
	 *            待压缩的文件名
	 * @note 若 file 为目录时，该函数将递归的压缩其子目录
	 */
	public static void zip(ZipOutputStream out, File file, String filename) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				out.putNextEntry(new ZipEntry(filename + "/"));
			}

			for (File f : files) {
				zip(out, f, filename + "/" + f.getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(filename));
			FileInputStream inputStream = new FileInputStream(file);
			BufferedInputStream bi = new BufferedInputStream(inputStream);
			int b;
			while ((b = bi.read()) != -1) {
				out.write(b);
			}
			out.closeEntry();
			inputStream.close();
		}
	}
}