package top.zzming.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailUtil {
    public static void SimpleMailMessage(JavaMailSender mailSender, String from, String to, String activeCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("激活你的账号");
        message.setText("你的激活url:http://zzming.top/user/active/"+ activeCode);
        mailSender.send(message);
    }
}
