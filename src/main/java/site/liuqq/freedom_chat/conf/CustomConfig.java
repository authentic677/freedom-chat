package site.liuqq.freedom_chat.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "site.liuqq.freedomchat")
@PropertySource("classpath:application-custom.properties")
public class CustomConfig {
    private String zone; //时区信息
}
