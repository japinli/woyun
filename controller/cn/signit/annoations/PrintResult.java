package cn.signit.annoations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*(这里用一句话描述这个类的作用)
* @ClassName PrintResult
* @author liwen
* @date 2016年5月31日-下午4:45:26
* @version (版本号)
* @see (参阅)
*/
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrintResult {
	int argIndex() default 0;
}
