/**
* @author ZhangHongdong
* @date 2014年12月17日-下午7:45:23
* @see  (参阅)
*/
package cn.signit.untils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *日期工具
 * @ClassName DateUtil
 * @author ZhangHongdong
 * @date 2014年12月17日-下午7:45:23
 * @version 1.0.0
 */
public class DateUtil {
		
	/**
	* 两个日期相差的天数
	*@param newDate 新日期
	*@param oldDate 旧日期
	*@return -1 相差天数错误，>=0 相差的天数
	*/
	public static int getDaysBetween(Date newDate,Date oldDate){
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();
		newCal.setTime(newDate);
		oldCal.setTime(oldDate);
		long between = newCal.getTimeInMillis() - oldCal.getTimeInMillis();
		if(between > 0){
			return (int) (between/86400000);//1000*3600*24
		}else{
			return -1;
		}
	}
	
	/**
	*  两个日期相差的小时
	*@param newDate 新日期
	*@param oldDate 旧日期
	*@return  -1 相差小时错误，>=0 相差的小时
	*/
	public static int getHoursBetween(Date newDate,Date oldDate){
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();
		newCal.setTime(newDate);
		oldCal.setTime(oldDate);
		long between = newCal.getTimeInMillis() - oldCal.getTimeInMillis();
		if(between > 0){
			return (int) (between/3600000);//1000*3600
		}else{
			return -1;
		}
	}
	
	/**
	*  两个日期相差的分钟
	*@param newDate 新日期
	*@param oldDate 旧日期
	*@return  -1 相差分钟错误，>=0 相差的分钟
	*/
	public static int getMinutesBetween(Date newDate,Date oldDate){
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();
		newCal.setTime(newDate);
		oldCal.setTime(oldDate);
		long between = newCal.getTimeInMillis() - oldCal.getTimeInMillis();
		if(between > 0){
			return (int) (between/60000);//1000*60
		}else{
			return -1;
		}
	}
	
	/**
	*  两个日期相差的秒数
	*@param newDate 新日期
	*@param oldDate 旧日期
	*@return  -1 相差秒数错误，>=0 相差的秒数
	*/
	public static int getSecondsBetween(Date newDate,Date oldDate){
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();
		newCal.setTime(newDate);
		oldCal.setTime(oldDate);
		long between = newCal.getTimeInMillis() - oldCal.getTimeInMillis();
		if(between > 0){
			return (int) (between/1000);
		}else{
			return -1;
		}
	}
	
	/**
	*  两个日期相差的毫秒数
	*@param newDate 新日期
	*@param oldDate 旧日期
	*@return  -1 相差毫秒数错误，>=0 相差的毫秒数
	*/
	public static long getMillisBetween(Date newDate,Date oldDate){
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();
		newCal.setTime(newDate);
		oldCal.setTime(oldDate);
		long between = newCal.getTimeInMillis() - oldCal.getTimeInMillis();
		if(between > 0){
			return between;
		}else{
			return -1;
		}
	}
	
	/**
	* 日期字符串转日期（java.util.Date）对象
	* 
	*@param pattern 字符串日期的表示格式（如："yyyy-MM-dd HH:mm:ss"）
	*@param dateStr 将要转换的日期字符串
	*@return 日期（java.util.Date）对象。如果转换失败，则返回null
	*/
	public static Date string2Date(String pattern,String dateStr){
		if(pattern == null){
			pattern = DEFAULT_DATE_PATTERN;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);                
			Date date = sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			return null;
		}     
	}
	
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
