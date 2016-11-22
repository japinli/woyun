package cn.signit.utils;

import java.text.DecimalFormat;

/**
 *数字格式化器
 * @ClassName DecimalFormator
 * @author ZhangHongdong
 * @date 2015年8月5日-下午7:34:48
 * @version 1.1.0
 */
public class DecimalFormator {
	private final static DecimalFormat DF = new DecimalFormat("#.00");
	
	public static <T> String getString(T num){
		return DF.format(num);
	}
	
	public static double getDouble(double num){
		return Double.parseDouble(getString(num));
	}
	
	public static long getLong(long num){
		return Long.parseLong(getString(num));
	}
	
	public static <T> String getString(T num,int digit){
		StringBuilder sb = new StringBuilder("#.");
		for (int i = 0; i < digit; i++) {
			sb.append("0");
		}
		DF.applyPattern(sb.toString());
		return DF.format(num);
	}

	public static double getDouble(double num,int digit){
		return Double.parseDouble(getString(num,digit));
	}
	
	public static long getLong(long num,int digit){
		return Long.parseLong(getString(num,digit));
	}
}
