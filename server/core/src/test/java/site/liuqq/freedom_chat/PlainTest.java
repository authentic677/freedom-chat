package site.liuqq.freedom_chat;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class PlainTest {

    public static void main(String[] args) throws Exception {


        Resend resend = new Resend("re_Q4viMZtB_JUdFVK2tcvbqMTP4pUMEtc8p");


        CreateEmailOptions build = CreateEmailOptions.builder()
                .from("freedom-chat <admin@liuqq.site>")
                .to("945491917@qq.com")
                .subject("自由聊天")
                .html("<p>您的邮箱验证码是：<strong>"+123+"</strong></p>")
                .build();

        CreateEmailResponse send = resend.emails().send(build);

        System.out.println(send.getId());


    }
}
