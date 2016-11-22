package cn.signit.untils.zip;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 基于apache工具的压缩输出流和数据输入流的数据压缩
 * @ClassName ZipTool
 * @author panxy
 * @date 2016年10月6日-下午4:26:02
 * @version 2.5.0
 */
public class ZipTool {

	public void compress(ZipOutputStream zipOut,InputStream inputStream,String filename) throws IOException{
		zipOut.putNextEntry(new ZipEntry(filename));
		byte[] buffer =new byte[2048];
		int temp =0;
		while ((temp = inputStream.read(buffer)) != -1) {
			zipOut.write(buffer,0,temp);
		}
		zipOut.closeEntry();
		inputStream.close();
	}
}
