/**
* @author ZhangHongdong
* @date 2015年8月26日-下午10:51:48
* @see (参阅)
*/
package cn.signit.server.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import cn.signit.conf.ConfigProps;
import cn.signit.cons.UrlPath;
import cn.signit.untils.message.SessionKeys;

/**
*基于注解的Spring Security相关配置
*
* @ClassName SecurityConfig
* @author ZhangHongdong
* @date 2015年8月26日-下午10:51:48
* @version 1.1.0
*/
@Configuration
//启用Spring Security的安全管理
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Override  
    public void configure(WebSecurity web) throws Exception {  
        // 设置不拦截规则  
        web.ignoring().antMatchers("/js/**","/css/**","/img/**","/docs/**","/html/**","/res/user/**");  
    }  
  
    @Override  
    protected void configure(HttpSecurity http) throws Exception {  
    	//设置Http协议规则
    	customRequestSecureRules(http);
    	
        // 设置拦截规则  
        // 自定义accessDecisionManager访问控制器,并开启表达式语言  
       // http.authorizeRequests().accessDecisionManager(accessDecisionManager())  
       //         .expressionHandler(webSecurityExpressionHandler())  
        //       .antMatchers("/**/*.do*").hasRole("USER")  
        //        .antMatchers("/**/*.html").hasRole("ADMIN").and()  
         //       .exceptionHandling().accessDeniedPage("/login");  
  
    	/*http.formLogin().loginPage(UrlPath.PAGE_USER_LOGIN)
    		.usernameParameter(SessionKeys.USERNAME)
    		.passwordParameter(SessionKeys.PASSWORD);
    	
    	http.logout()
    	    .logoutRequestMatcher(new AntPathRequestMatcher(UrlPath.getRealUrlPath(UrlPath.PAGE_USER_LOGOUT, "**")));*/
    	//CSRF
    	http.csrf()
    		.disable();
    	http.exceptionHandling()
    		.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(UrlPath.PAGE_USER_LOGIN));
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter,CsrfFilter.class);
  
        
    }  
    
    //自定义安全请求规则
	private void customRequestSecureRules(HttpSecurity http) throws Exception{
		String val = ConfigProps.get("server"
				+ ""
				+ ".request_requires_secure");
		
		//设置Http协议规则
		if(val == null || "auto".equals(val)){
	    	http.requiresChannel().antMatchers("/user/**").requiresSecure()
	    										  .antMatchers("/user/sys/**/callback/**").requires(ChannelDecisionManagerImpl.ANY_CHANNEL)
	    										  .anyRequest().requires(ChannelDecisionManagerImpl.ANY_CHANNEL);
	    	return;
		}
		
		if("no".equals(val)){
			http.requiresChannel().anyRequest().requiresInsecure();
			return;
		}
		
		if("all".equals(val)){
			http.requiresChannel().anyRequest().requiresSecure();
			return;
		}
	}
	
	
}
