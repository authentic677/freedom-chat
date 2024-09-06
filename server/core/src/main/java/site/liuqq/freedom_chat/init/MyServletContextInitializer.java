package site.liuqq.freedom_chat.init;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MyServletContextInitializer implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //这个操作貌似废弃
        servletContext.setAttribute("emailMap",new HashMap<>());
    }
}
