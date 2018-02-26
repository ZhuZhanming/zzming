package top.zzming.service;

import top.zzming.model.User;

/**
 * User功能的Service层
 */
public interface UserService {

    /**
     * 加密字符串
     */
    String encrypt(String str);

    /**
     * 注册user
     */
    User register(User user);

    /**
     * 激活user
     */
    int active(String activeCode);
}
