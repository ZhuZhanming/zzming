package top.zzming.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.zzming.feign.UserFeignClient;
import top.zzming.model.User;
import top.zzming.util.MailUtil;

import java.util.UUID;

/**
 * 用户模块业务层实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String encrypt(String str) {
        if (StringUtils.isEmpty(str)){
            return "";
        }
        return encoder.encode(str);
    }

    @Override
    public User register(User user) {

        if (StringUtils.isEmpty(user.getNickname())){
            user.setNickname(user.getUsername());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if (StringUtils.isEmpty(user.getPhone())){
            user.setPhone(null);
        }
        user.setActiveCode(UUID.randomUUID().toString().replace("-", ""));
        User result = userFeignClient.registerUser(user);
        MailUtil.SimpleMailMessage(mailSender,"1020030139@qq.com",user.getEmail(),user.getActiveCode());
        return result;
    }

    @Override
    public int active(String activeCode) {
        return userFeignClient.active(activeCode);
    }
}
