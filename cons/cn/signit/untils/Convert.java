package cn.signit.untils;

public class Convert {

	/**
	 * 转换整型变量为布尔类型
	 * @param n 整型类型变量
	 * @return true - 变量 n 大于 0, false - 变量 n 小于等于 0
	 */
	public static boolean toBoolean(Integer n) {
		if (n > 0) {
			return true;
		}
		
		return false;
	}
}
