package cn.signit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import cn.signit.sdk.BaseResponse;

/**
* @ClassName PrintRespAspect
* @author liwen
* @date 2016年5月31日-下午4:47:18
* @version (版本号)
* @see (参阅)
*/
@Aspect
@Controller
public class PrintRespAspect extends BaseAspect{
	public final static String REQUEST_INFO = "-----已处理.状态码:{},处理结果:{},结果描述:{}";
	@Pointcut("@annotation(cn.signit.annotation.PrintResult)")
	public void controllerAspect(){}
	
	@Around("controllerAspect()")
	public Object arount(ProceedingJoinPoint pjp) throws Throwable{
		Logger LOG = LoggerFactory.getLogger(pjp.getTarget().getClass());
		Object  object = null;
		object = pjp.proceed();
		if(object instanceof BaseResponse){
			BaseResponse resp=(BaseResponse) object;
			LOG.info(REQUEST_INFO,resp.getResultStatusCode(),resp.getResultCode(),resp.getResultDesc());
		}
		return object;
	}
}
