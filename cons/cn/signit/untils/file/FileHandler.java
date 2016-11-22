/**
 * 
 */
package cn.signit.untils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;

import cn.signit.tools.utils.Base64Utils;

/**
 * 
 * 文件操作助手
 * 
 * @author zhanghongdong   
 * @date 2014-3-27 下午4:53:56 
 */

public class FileHandler {
	public final static String TEMP_DIR = System.getProperty("java.io.tmpdir");
	public final static String TEMP_UUID_FILE_TEMPLATE = TEMP_DIR+FileSeparator.AUTO+"%s%s%s";
	public final static String TEMP_UUID_DIR_TEMPLATE = TEMP_DIR+FileSeparator.AUTO+"%s";

	/**
	* 根据文件对象迭代创建文件
	* 
	*@param file 文件对象
	*@return 成功－返回已创建的文件对象；失败－返回null
	*/
	public static File createFile(File file) {
		if(file == null){
			return null;
		}
		if(file.isFile()){
			return file;
		}
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			if(!file.isFile()){
				return null;
			}else{
				return file;
			}
		} catch (IOException e) {
			return null;
		}
	}
	
	
	/**
	* 迭代创建文件
	* 
	*@param existFile 已存在的文件对象
	*@param isOnlyChangeExtension 是否只改变扩展名来生成对应文件（默认：false，即：自动随机生成UUID无扩展名的文件名）
	*@param newExtension 新的扩展名（如果为null,则扩展名默认采用 "tmp" ）
	*@return 成功－返回已创建的文件对象；失败－返回null
	*/
	public static File createFile(File existFile,boolean isOnlyChangeExtension, String newExtension){
		if(existFile == null || !existFile.isFile()){
			return null;
		}
		
		if(newExtension == null){
			newExtension = "tmp";
		}
		
		if(!isOnlyChangeExtension){
			return createFile(new File(existFile.getParent(),UUID.randomUUID().toString()+"."+newExtension.replaceFirst("\\.", "")));
		}
		
		if(isOnlyChangeExtension){//只改变扩展名
			String parent = existFile.getParent();
			String fileName = existFile.getName();
			int lastPointIndex = fileName.lastIndexOf(".");
			if(lastPointIndex < 0 || lastPointIndex == fileName.length() -1){//文件名无后缀
				return createFile(new File(parent,fileName+"."+newExtension.replaceFirst("\\.", "")));
			}else{
				return createFile(new File(parent,fileName.substring(0, lastPointIndex+1).concat(newExtension.replaceFirst("\\.", ""))));
			}
		}
		return null;
	}
	
	/**
	* 迭代创建临时文件
	* 
	*@param noExtensionName 文件名（不带扩展名）
	*@param extension 扩展名（如果为null,则扩展名默认采用 "tmp" ）
	*@return 成功－返回已创建的临时文件对象；失败－返回null
	*/
	public static File createTempFile(String noExtensionName,String extension){
		String defaultDivPoint = ".";
		
		if(noExtensionName != null && extension == null){
			defaultDivPoint = "";
			extension = "";
		}
		
		if(noExtensionName == null){
			noExtensionName = "~"+UUID.randomUUID().toString();
		}
		
		if(extension == null){
			extension = "tmp";
		}
		
		String path = String.format(TEMP_UUID_FILE_TEMPLATE, noExtensionName,defaultDivPoint,extension.replaceFirst("\\.", ""));
		
		File tmpFile = createFile(new File(path));
		if(tmpFile == null){
			return null;
		}
		tmpFile.deleteOnExit();
		return tmpFile;
	}
	
	/**
	* 迭代创建临时文件
	* 
	*@param fileName 文件名（可带扩展名）
	*@param isOnlyChangeExtension 是否只改变扩展名来生成对应临时文件（默认：false）
	*@param newExtension 新的扩展名（isOnlyChangeExtension为 true 时有效，如果为null,则扩展名默认采用 "tmp" ）
	*@return 成功－返回已创建的临时文件对象；失败－返回null
	*/
	public static File createTempFile(String fileName, boolean isOnlyChangeExtension, String newExtension){
		if(fileName == null){
			return createTempFile(null,null);
		}

		int lastPointIndex = fileName.lastIndexOf(".");
		if(lastPointIndex < 0 || lastPointIndex == fileName.length() -1){//文件名无后缀
			return createTempFile(fileName,null);
		}else{
			if(isOnlyChangeExtension){//只改变扩展名
				if(newExtension == null){
					newExtension = "tmp";
				}
				return createTempFile(fileName.substring(0,lastPointIndex+1).concat(newExtension.replaceFirst("\\.", "")),null);
			}
		}
		String noExtensionName = fileName.substring(0, lastPointIndex);
		String extension = fileName.substring(lastPointIndex+1);
		return createTempFile(noExtensionName,extension);
	}
	
	/**
	* 迭代创建临时文件
	* 
	*@param fileName 文件名（可带扩展名）
	*@return 成功－返回已创建的临时文件对象；失败－返回null
	*/
	public static File createTempFile(String fileName){
		return createTempFile(fileName,false,null);
	}
	
	/**
	* 迭代创建临时文件
	* 
	*@param extension 扩展名（如果为null,则扩展名默认采用 "tmp" ）
	*@param isRandomUUIDName 是否以UUID方式产生唯一名字，否则以当前Unix时间戳方式产生瞬时时间名字
	*@return 成功－返回已创建的临时文件对象；失败－返回null
	*/
	public static File createTempFile(String extension,boolean isRandomUUIDName){
		if(extension == null){
			return createTempFile();
		}
		if(isRandomUUIDName){
			return createTempFile("~"+UUID.randomUUID().toString(),extension.replaceFirst("\\.", ""));
		}else{
			return createTempFile("~"+System.currentTimeMillis(),extension.replaceFirst("\\.", ""));
		}
	}
	
	/**
	* 迭代创建临时文件
	* 
	*@return 成功－返回已创建的文件对象；失败－返回null
	*/
	public static File createTempFile(){
		return createTempFile("~"+UUID.randomUUID().toString(),"tmp");
	}
	
	/**
	* 根据文件路径迭代创建文件
	* 
	*@param filePath  文件路径
	*@return 成功－返回已创建的文件路径；失败－返回null
	*@throws IOException
	*/
	public static String createFile(String filePath) {
		return createFile(new File(filePath)).getAbsolutePath();
	}
	
	/**
	* 根据文件路径迭代创建文件
	* 
	*@param existFilePath 已存在的文件路径
	*@param isOnlyChangeExtension 是否只改变扩展名来生成对应文件（默认：false，即：自动随机生成UUID无扩展名的文件名）
	*@param newExtension 新的扩展名（如果为null,则扩展名默认采用 "tmp" ）
	*@return 成功－返回已创建的文件路径；失败－返回null
	*/
	public static String createFile(String existFilePath,boolean isOnlyChangeExtension, String newExtension){
		if(existFilePath == null){
			return null;
		}
		return createFile(new File(existFilePath), isOnlyChangeExtension, newExtension).getAbsolutePath();
	}
	
	/**
	* 根据文件夹文件对象迭代创建文件夹
	* 
	*@param file 文件夹文件对象
	*@return 成功－返回已创建的文件夹文件对象；失败－返回null
	*/
	public static File createDir(File file){
		if(file == null){
			return null;
		}
		if(file.isDirectory()){
			return file;
		}
		if(file.mkdirs() || file.isDirectory()){
			return file;
		}else{
			return null;
		}
	}
	
	/**
	* 根据文件夹路径迭代创建文件夹
	* 
	*@param filePath 文件夹路径
	*@return 成功－返回已创建的文件夹路径；失败－返回null
	*@throws IOException
	*/
	public static String createDir(String filePath){
		return createDir(new File(filePath)).getAbsolutePath();
	}
	
	
	/**
	* 缓冲包装输入流
	* 
	*@param is 输入流。如果已包装，则直接返回。
	*@return 包装后的输入流
	*/
	public static InputStream buffered(InputStream is){
		return buffered(is, (int)(Math.random()*1024)+1024);
	}
 
	/**
	*  缓冲包装输入流
	* 
	*@param is 输入流。如果已包装，则直接返回。
	*@param size 缓冲区大小（>0）
	*@return 包装后的输入流
	*/
	public static InputStream buffered(InputStream is,int size){
		if(is instanceof BufferedInputStream){
			return is;
		}
		return new BufferedInputStream(is,size);
	}
	
	/**
	*  缓冲包装输出流
	* 
	*@param os 输出流。如果已包装，则直接返回。
	*@return 包装后的输出流
	*/
	public static OutputStream buffered(OutputStream os){
		return buffered(os, (int)(Math.random()*1024)+1024);
	}
	
	/**
	*  缓冲包装输出流
	* 
	*@param os 输出流。如果已包装，则直接返回。
	*@param size 缓冲区大小（>0）
	*@return 包装后的输出流
	*/
	public static OutputStream buffered(OutputStream os,int size){
		if(os instanceof BufferedOutputStream){
			return os;
		}
		return new BufferedOutputStream(os,size);
	}
	
	/**
	 * 读取文件到内存中<br/>
	 *@param fromFilePath 文件路径
	 *@return 读取的文件字节数组
	 *@throws IOException
	 */
	public static byte[] readFile2Bytes(String fromFilePath) throws IOException{
		byte[] fileBytes = null;
		try(FileInputStream	fis = new FileInputStream(fromFilePath);
				FileChannel fc = fis.getChannel();){
			long fileSize = fc.size();
			if(fileSize < 52428800){//如果文件尺寸小于500m,则直接读取
				ByteBuffer buf = ByteBuffer.allocate((int)fileSize);
				buf.clear();
				fc.read(buf);
				buf.flip();
				fileBytes = buf.array();
			}else if(fileSize >= 52428800){//如果文件尺寸大于500m，则文件映射读取
				try(RandomAccessFile acessFile = new RandomAccessFile(fromFilePath, "rw");
						FileChannel fileChannel = acessFile.getChannel();){
					long dataLen = fileChannel.size();
					fileBytes = new byte[(int)dataLen];
					MappedByteBuffer mapBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, dataLen);
					while(mapBuf.hasRemaining()){
						mapBuf.get(fileBytes);
					}
				}
			}
		}
		return fileBytes;
	}
	/**
	 * 读取文件到内存中<br/>
	 *@param fromFile 文件路径
	 *@return 读取的文件字节数组
	 *@throws IOException
	 */
	public static byte[] readFile2Bytes(File fromFile) throws IOException{
		return readFile2Bytes(fromFile.getCanonicalPath());
	}
	
    /**
     * 读取数据流转换成对应的字节数组，并关闭输入流
     * @param is 输入的数据流
     * @return 读取的数据流对应的字节数组
     * @throws IOException
     */
    public static byte[] readStream2Bytes(InputStream is) throws IOException{
    	 byte[] buf = null;
    	 try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
    		   buf = new byte[4096];
               int len = -1;
               while((len = is.read(buf)) > 0){
                   baos.write(buf, 0, len);
               }
       			buf = null;
               return baos.toByteArray(); // baos不需要关闭
    	}
    }
    
    /**
     * 读取Base64编码后的二进制数据字符串到内存中
     * @param base64Str 输入的Base64编码后的二进制数据字符串
     * @return 读取的base64解码后的字节数组
     */
    public static byte[]  readBase64Str2Bytes(String base64Str) {
    	return Base64Utils.decode(base64Str);
    }
    
    /**
     * 读取字节数组转换成对应的数据流
     * @param FromFileBytes 输入的文件字节数组
     * @return 读取的数据流
     */
    public static InputStream readBytes2Stream( byte[] FromFileBytes) {
    	return new ByteArrayInputStream(FromFileBytes);
    }
    
    /**
     * 读取Base64编码后的二进制数据字符串转换成对应的数据流
     * @param base64Str 输入的Base64编码后的二进制数据字符串
     * @return 读取的base64解码后的数据流
     */
    public static InputStream readBase64Str2Stream(String base64Str) {
    	return readBytes2Stream(Base64Utils.decode(base64Str));
    }
    
	/**
	 * 写指定字节的文件字节数组到指定输出流中<br/>
	 *@param fromFileBytes 要写的文件字节数组
	 *@param toOutStream 写到的文件输出流
	 * @throws IOException 
	 */
	public static void writeBytes2Stream(byte[] fromFileBytes,OutputStream toOutStream) throws IOException{
		try(OutputStream bufOs = buffered(toOutStream)){
			toOutStream.write(fromFileBytes);
			toOutStream.flush();
		}
	}
    
	/**
	 * 写指定字节的文件数据到指定文件中<br/>
	 *@param fromFileBytes 要写的文件字节数组
	 *@param toDestPath 写到的文件路径
	 *@return 写到的文件路径
	 * @throws IOException 
	 */
	public static String writeBytes2Path(byte[] fromFileBytes,String toDestPath) throws IOException{
		try(FileOutputStream fos = new FileOutputStream(toDestPath);FileChannel fc = fos.getChannel();){
			fc.write(ByteBuffer.wrap(fromFileBytes));
			fos.flush();
		}
		return toDestPath;
	}
	
	/**
	* 写指定字节的文件数据到指定文件中<br/>
	*@param fromFileBytes 要写的文件字节数组
	*@param toDestFile 写到的文件对象
	*@return 写到的文件对象
	*@throws IOException
	*/
	public static File writeBytes2File(byte[] fromFileBytes,File toDestFile) throws IOException{
		return new File(writeBytes2Path(fromFileBytes, toDestFile.getCanonicalPath()));
	}
	
	
	/**
	*写指定字节的输入流到指定文件中
	*@param fromFileStream 输入流
	*@param toDestPath 写到的文件路径
	*@return 写到的文件对象
	*@throws IOException
	*/
	public static File writeStream2File(InputStream fromFileStream,File toDestFile) throws IOException{
		try(InputStream is = fromFileStream;
				FileOutputStream fos = new FileOutputStream(toDestFile);
				FileChannel fc = fos.getChannel();
				ReadableByteChannel readableByteChannel = Channels.newChannel(fromFileStream);){
			long offset = 0;
			long quantum = 1048576;//1024*1024
			long count = fc.transferFrom(readableByteChannel, offset, quantum);
			while(count > 0){
				offset += count;
				count =  fc.transferFrom(readableByteChannel, offset, quantum);
			}
		}
		return toDestFile;
	}
	
	/**
	*写指定字节的输入流到指定路径中
	*@param fromFileStream 输入流
	*@param toDestPath 写到的文件路径
	*@return 写到的文件路径
	*@throws IOException
	*/
	public static String writeStream2Path(InputStream fromFileStream,String toDestPath) throws IOException{
		return writeStream2File(fromFileStream, new File(toDestPath)).getCanonicalPath();
	}
	
	/**
	* 写指定Base64解码后的数据字符串到指定文件中
	* 
	*@param base64Str 输入的base64编码的数据字符串
	*@param toDestFile 解码后写到的文件对象
	*@return 解码后写到的文件对象
	 * @throws IOException 
	*/
	public static File writeBase64Str2File(String base64Str,File toDestFile) throws IOException {
		return writeBytes2File(Base64Utils.decode(base64Str), toDestFile);
	}
	
	/**
	* 写指定Base64解码后的数据字符串到指定路径中
	* 
	*@param base64Str 输入的base64编码的数据字符串
	*@param toDestFile 解码后写到的文件路径
	*@return 解码后写到的文件路径
	 * @throws IOException 
	*/
	public static String writeBase64Str2Path(String base64Str,String toDestPath) throws IOException{
		return writeBytes2Path(Base64Utils.decode(base64Str), toDestPath);
	}
	
	/**
	*写指定字节的输入流到指定的输出流中
	*@param fromStream 输入流
	*@param toStream 输出流
	*@throws IOException
	*/
	public static void writeStream2Stream(InputStream fromStream,OutputStream toStream) throws IOException{
		try(InputStream is = fromStream;OutputStream os = toStream;){
			byte[] buf = new byte[4096];
			int len;
			while((len = is.read(buf)) > 0){
				os.write(buf, 0, len);
			}
			os.flush();
			buf = null;
		}
	}
	
	
	/**
	 * 写指定文件对象到指定的输出流中<br/>
	 *@param fromFile 要写的文件对象
	 *@param toStream 写到的数据流中
	 * @throws IOException 
	 */
	public static void writeFile2Stream(File fromFile,OutputStream toStream) throws IOException{
		try(InputStream fis = new FileInputStream(fromFile);
				OutputStream os = toStream;){
			 writeStream2Stream(fis, toStream);
		}
	}
	
	/**
	 * 写指定文件路径到指定的输出流中<br/>
	 *@param fromPath 要写的文件路径
	 *@param toStream 写到的数据流中
	 * @throws IOException 
	 */
	public static void writePath2Stream(String fromPath,OutputStream toStream) throws IOException{
		try(InputStream fis = new FileInputStream(fromPath);
				OutputStream os = toStream;){
			 writeStream2Stream(fis, toStream);
		}
	}

	
	public static void main(String[] args) throws IOException {
		System.out.println(FileHandler.createTempFile(null, "jpg").getAbsolutePath());
		//System.out.println(new File("/zhd/zxdsf/ss.pdf").isFile());
		//System.out.println(createTempFile("xxxx.doc","pdf").getAbsolutePath());
		//System.out.println(createTempFile(null,true,"pdf").getAbsolutePath());
		System.out.println(createFile("", true, "doc") == null);
		System.out.println(createFile(new File("/home/zhd/test/test-doc.docx"), true, "doc") == null);
		System.out.println(createFile(new File("/home/zhd/test/test-doc.doc"), true, "xxx").getAbsolutePath());
		System.out.println(createFile(new File("/home/zhd/test/test-doc.doc"), false, "xxx").getAbsolutePath());
		System.out.println(createFile(new File("/home/zhd/test/test-doc.doc"), false, null).getAbsolutePath());
		//System.out.println(createTempFile(".pdf",true).getAbsolutePath());
		//System.out.println(createTempFile("123456.pdf",true,"pdf").getAbsolutePath());
		//System.out.println(createTempFile("123456.pdf.",true,"pdf").getAbsolutePath());
		//System.out.println(createTempFile(".123456.pdf.").getAbsolutePath());
		//new File(TEMP_DIR+"/xxx").createNewFile();
		long s = System.currentTimeMillis();
		//InputStream is = FileOperaterHandler.buffered(FileOperaterHandler.reuse(new FileInputStream("/home/zhd/下载/test/jquery.pdf")));
		/*FileInputStream fis = new FileInputStream("/home/zhd/下载/test/jquery.pdf");
		FileOutputStream fos = new FileOutputStream("/home/zhd/下载/test/jquery.pdf2");
		System.out.println("");
		writeStream2Stream(fis, fos);
		System.out.println("");*/
		//writeStream2File(is, "/home/zhd/下载/test/jquery2.pdf");
		//writeStream2File(FileOperaterHandler.reuse(is), "/home/zhd/下载/test/jquery3.pdf");
		//writeStream2File(FileOperaterHandler.reuse(is), "/home/zhd/下载/test/jquery4.pdf");
		System.out.println("\n" + (System.currentTimeMillis() - s) / 1000.00
				+ " 秒");
		//FileOperaterHandler.reuseFinish(is);
		//FileOperaterHandler foh = new FileOperaterHandler();
		//foh.hide(new File("C:\\Users\\Administrator\\Desktop\\test big file\\JavaScript高级程序设计(第二版).pdf"));
		//foh.show(new File("C:\\Users\\Administrator\\Desktop\\test big file\\JavaScript高级程序设计(第二版).pdf"));
	/*	long s1 = System.currentTimeMillis();
		System.out.println(FileOperaterHandler.readFileBytes("C:\\Users\\Administrator\\Desktop\\test big file\\JavaScript高级程序设计(第二版).pdf").length);
		System.out.println(System.currentTimeMillis() - s1);
		long s2 = System.currentTimeMillis();
		System.out.println(FileOperaterHandler.readFileBytes2("C:\\Users\\Administrator\\Desktop\\test big file\\JavaScript高级程序设计(第二版).pdf").length);
		System.out.println(System.currentTimeMillis() - s2);*/
		//C:\\Users\\zhanghongdong\\Documents\\JavaScript高级程序设计(第二版).pdf
		//System.out.println(FileOperaterHandler.readFileBytes2("C:\\Users\\Administrator\\Desktop\\test big file\\JavaScript高级程序设计(第二版).pdf").length);
	}
}
