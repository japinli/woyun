/**
* @author ZhangHongdong
* @date 2015年3月16日-下午7:52:42
* @see (参阅)
*/
package cn.signit.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Controller;

/**
 *基础切面控制器
 * @ClassName BaseAspect
 * @author ZhangHongdong
 * @date 2015年3月16日-下午7:52:42
 * @version 1.1.0
 */
@Aspect
@Controller
public abstract class BaseAspect {
	
	/**
	* 获得连接点的方法
	* 
	*@param joinPoint 连接点
	*@return 连接点的方法对象
	*@throws Throwable 
	*@since 1.2.0
	*/
	public Method getJoinPointMethod(JoinPoint joinPoint)  throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
       Method method =  joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
       return method;
    }
	
	public Class<?> getJoinPointClass(JoinPoint joinPoint) throws Throwable{
		return joinPoint.getTarget().getClass();
	}
}
