package site.liuqq.freedom_chat.conf;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/email_login")
                .excludePathPatterns("/api/captcha")
                .excludePathPatterns("/api/mailVerifyCode")
                .excludePathPatterns("/api/checkToken")
                .excludePathPatterns("/api/checkAccount")
                .excludePathPatterns("/api/checkEmailVerifyCode")
                .excludePathPatterns("/api/resetPwd");
    }
}

