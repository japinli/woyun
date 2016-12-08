package cn.signit.untils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.signit.utils.repo.RepoPath;

public class Zip {
	
	private ZipOutputStream zipOutputStream;
	private String zipFile;
	private String zipName;
	
	public String getZipFile() {
		return zipFile;
	}
	public Zip(String zipFile) {
		this.zipName = zipFile + ".zip";
		this.zipFile = RepoPath.getTemp(this.zipName);
	}
	
	public void init() throws IOException {
		zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
	}
	
	public void update(String filename, byte[] data) throws IOException {
		zipOutputStream.putNextEntry(new ZipEntry(filename));
		zipOutputStream.write(data);
		zipOutputStream.closeEntry();
	}
	
	public String zip() throws IOException {
		zipOutputStream.close();
		return zipName;
	}
	
	/**
	 * 压缩给定的文件到 *zipFileName* 中
	 * 
	 * @param zipFileName
	 *            压缩后的文件名，函数会在该文件名后添加 zip 后缀
	 * @param filenames
	 *            待压缩文件名
	 * @return true - 成功, false - 失败
	 */
	public static String zip(String zipFileName, String... filenames) {
		try {
			zipFileName += ".zip";
			String tmpFileName = RepoPath.getTemp(zipFileName);
			ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(tmpFileName));

			for (String name : filenames) {
				File file = new File(name);
				if (file != null) {
					zip(outputStream, file, file.getName());
				}
			}

			outputStream.close();
			return zipFileName;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String zip(String zipFileName, List<String> filenames) {
		try {
			zipFileName += ".zip";
			String tmpFileName = RepoPath.getTemp(zipFileName);
			ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(tmpFileName));
			
			for (String name : filenames) {
				File file = new File(name);
				if (file != null) {
					zip(outputStream, file, file.getName());
				}
			}

			outputStream.close();
			return zipFileName;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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