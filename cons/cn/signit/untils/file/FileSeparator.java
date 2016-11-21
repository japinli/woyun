/**
* @author ZhangHongdong
* @date 2015年8月5日-下午2:25:41
* @see (参阅)
*/
package cn.signit.untils.file;

import java.io.File;

/**
 *文件分隔符常量
 * @ClassName FileSparator
 * @author ZhangHongdong
 * @date 2015年8月5日-下午2:25:41
 */
public final class FileSeparator {
		public final static String AUTO = File.separator;
		public final static String WIN = "\\";
		public final static String UNIX = "/";
		public final static char AUTO_CHAR = File.separatorChar;
		public final static char WIN_CHAR = '\\';
		public final static char UNIX_CHAR = '/';
}
