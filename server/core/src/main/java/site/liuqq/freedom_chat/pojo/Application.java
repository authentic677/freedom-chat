package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    int id;
    LocalDateTime time;
    String leaveMessage;
    String status;
}
