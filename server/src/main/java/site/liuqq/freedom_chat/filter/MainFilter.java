package site.liuqq.freedom_chat.filter;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.utils.Tools;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/api/*") //拦截所有api接口请求,要想生效启动类必须加上@ServletComponentScan注解
public class MainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //前置：强制转换为http协议的请求对象、响应对象 （转换原因：要使用子类中特有方法）
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求路径：{}", url); //请求路径：http://localhost/api/login


        //2.判断请求url中是否包含要放行的接口
        if(url.contains("/login")||
                url.contains("/email_login")||
                url.contains("/register")||
                url.contains("/captcha")||
                url.contains("/mailVerifyCode")||
                url.contains("/checkToken")||
                url.contains("/checkAccount")||
                url.contains("/checkEmailVerifyCode")||
                url.contains("/resetPwd")||
                url.contains("/resource")){
            chain.doFilter(request, response);//放行请求
            return;//结束当前方法的执行
        }


        //3.获取请求头中的令牌（token）
        String token = request.getHeader("token");
        log.info("从请求头中获取的令牌：{}",token);


        //4.判断令牌是否存在且有效
        User user=Tools.checkJwtToken(token);
        if(user==null){
            log.info("Token不存在或无效");

            Result result = Result.error("invalid token");
            //把Result对象转换为JSON格式字符串 (fastjson是阿里巴巴提供的用于实现对象和json的转换工具类)
            String json = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=utf-8");
            //响应
            response.getWriter().write(json);

            return;
        }

        //5.放行，设置用户uid信息
        HttpSession session = request.getSession();
        if(session.isNew()){
            session.setAttribute("user",user);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
