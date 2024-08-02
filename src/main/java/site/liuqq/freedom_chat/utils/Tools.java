package site.liuqq.freedom_chat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import site.liuqq.freedom_chat.pojo.User;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Tools {
    private static final String secretKey="lqq677...";

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean validateEmail(String email){
        String regex="^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean sendMail(String email,String content){
        // 设置发件人邮箱地址和密码
        String senderEmail = "16618822309@163.com";
        String password = "DEJPGFDMKEWRVBDG";

        // 设置收件人邮箱地址
        String recipientEmail = email;

        // 设置邮件主题和内容
        String subject = "【自由聊天】验证码";
        String body = content;

        // 设置邮件服务器主机
        String host = "smtp.163.com";

        // 创建邮件会话
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "25"); // SMTP 端口号（可以根据邮件服务器配置调整）
        properties.put("mail.smtp.starttls.enable", "true"); // 启用 TLS 连接（可选）

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // 创建 MimeMessage 对象
            Message message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(senderEmail));
            // 设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            // 设置邮件主题
            message.setSubject(subject);
            // 设置邮件内容
            message.setText(body);

            // 发送邮件
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateRandomNumberString(int length){
        String characters = "0123456789";
        StringBuilder builder=new StringBuilder();
        Random random=new Random();

        for(int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return builder.toString();
    }

    public static boolean validatePwd(String pwd){
        String regex="^(?=.*[a-zA-Z]).{6,}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(pwd);

        return matcher.matches();
    }

    public static String passwordHash(String password){
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            return bytesToHex(digest.digest(password.getBytes()));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static String makeJwtToken(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("uid",user.getUid());
        claims.put("username",user.getUsername());

        String jwt = Jwts.builder()
                .setClaims(claims) //自定义内容(载荷)
                .signWith(SignatureAlgorithm.HS256, secretKey) //签名算法
                .setExpiration(new Date(System.currentTimeMillis() + 72*60*60*1000)) //有效期
                .compact();

        return jwt;
    }

    public static User checkJwtToken(String jwt){
        try {
            Claims claims=Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();

            System.out.println(claims);

            User user = new User();
            user.setUid((String) claims.get("uid"));
            user.setUsername((String) claims.get("username"));

            return user;
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDateTime now(String zone){
        //设置正确而统一的时区
        ZonedDateTime zonedDateTime=ZonedDateTime.now();
        ZonedDateTime zonedDateTime1 = zonedDateTime.withZoneSameInstant(ZoneId.of(zone));
        return zonedDateTime1.toLocalDateTime();
    }

    public static String getCurrentYearMonth() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化当前日期并返回
        return currentDate.format(formatter);
    }

}
