package cn.signit.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import cn.signit.conf.ConfigProps;

/**
*基于 Thymeleaf为模块引擎的配置
* @ClassName ThymeleafConfig
* @author ZhangHongdong
* @date 2016年4月12日-上午10:38:41
* @version 1.2.0
*/
@Configuration
@Import(BaseConfig.class)
public class ThymeleafConfig implements ApplicationContextAware {
	private final static Logger LOG = LoggerFactory.getLogger(ThymeleafConfig.class);

    private static final String UTF8 = "UTF-8";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver viewResolver() {
    	LOG.info("======================>>   启用模板引擎( {} ) ",ThymeleafConfig.class.getName());
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(UTF8);
        return resolver;
    }

    private TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        //添加对SpringSecurity标签扩展的支持
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(ConfigProps.get("page.template.thymeleaf.prefix"));
        resolver.setTemplateMode(TemplateMode.HTML);
        boolean isCache = ConfigProps.getBoolean("page.template.thymeleaf.cacheable");
        long cacheTTLms = ConfigProps.getLong("page.template.thymeleaf.cacheable.TTLMs");
        if(isCache){
        	resolver.setCacheable(true);
        	if(cacheTTLms > 0){
        		resolver.setCacheTTLMs(cacheTTLms);
        	}
        }else{
        	resolver.setCacheable(false);
        }
        return resolver;
    }
}
