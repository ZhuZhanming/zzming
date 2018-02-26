package top.zzming.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件工具
 */
public class MailUtil {
    /**
     * 发生简单文字
     * @param mailSender 发送类
     * @param from 发送方
     * @param to 接受方
     * @param activeCode 激活码
     */
    public static void SimpleMailMessage(JavaMailSender mailSender, String from, String to, String activeCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("激活你的账号");
        message.setText("你的激活url:http://zzming.top/user/active/"+ activeCode);
        mailSender.send(message);
    }
}
