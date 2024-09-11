package xyz677123.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import site.liuqq.freedom_chat.common.Tools;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;



import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/api/zone/*")
public class MainFilter implements Filter {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static ThreadLocal<User> getThreadLocal() {
        return threadLocal;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//        //前置：强制转换为http协议的请求对象、响应对象 （转换原因：要使用子类中特有方法）
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        //1.获取请求url
//        String url = request.getRequestURL().toString();
//        log.info("请求路径：{}", url);
//
//
//        //2.获取请求头中的令牌（token）
//        String token = request.getHeader("token");
//        log.info("从请求头中获取的令牌：{}",token);
//
//
//        //3.判断令牌是否存在且有效
//        User user= Tools.checkJwtToken(token);
//        if(user==null){
//            log.info("Token不存在或无效");
//
//            Result result = Result.error("invalid token");
//            //把Result对象转换为JSON格式字符串 (fastjson是阿里巴巴提供的用于实现对象和json的转换工具类)
//            String json = JSONObject.toJSONString(result);
//            response.setContentType("application/json;charset=utf-8");
//            //响应
//            response.getWriter().write(json);
//
//            return;
//        }
//
//        //5.放行，设置用户uid信息，使用threadlocal共享数据
//        try {
//            threadLocal.set(user);
//            chain.doFilter(request, response);
//        }finally {
//            threadLocal.remove();
//        }



        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("filter destroy");
    }
}
