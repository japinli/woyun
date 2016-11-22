/**
* @author ZhangHongdong
* @date 2015年8月11日-下午3:58:50
* @see (参阅)
*/
package cn.signit.cons.desc;

/**
 *用户认证状态类型
 * @ClassName UserAuthStateType
 * @author ZhangHongdong
 * @date 2015年8月11日-下午3:58:50
 * @version 1.1.0
 */
public class UserAuthStateType {
	//未处理（默认值，非管理平台使用字段）
	public final static int  UNTREATED = 0;
	//验证通过
	public final static int PASS = 1;
	//未验证
	public final static int UNCHECKED = 2;
	//验证未通过
	public final static int UNPASSED = 3;
}
