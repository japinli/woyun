package cn.signit.utils.time;

import java.util.Collections;
/**
*标准时间创建工厂
* @ClassName StandardTimesFactory
* @author ZhangHongdong
* @date 2016年4月14日-下午3:23:50
* @version 1.2.0
*/
public class StandardTimesFactory {
			private static StandardTimes ST;
			
			/**
			* 单例方式创建默认标准时间对象
			*@return 标准时间对象
			*@since1.2.0
			*@author Zhanghongdong
			*/
			public static StandardTimes getDefault(){
				return getDefault(-1);
			}
			
			/**
			* 单例方式创建默认标准时间对象
			* @param timeout 获取超时毫秒数
			*@return 标准时间对象
			*@since1.2.0
			*@author Zhanghongdong
			*/
			@SuppressWarnings("unchecked")
			public static StandardTimes getDefault(int timeout){
				if(ST != null){
					return ST;
				}
				return (ST = new StandardTimes(Collections.EMPTY_SET,timeout));
			}
}
