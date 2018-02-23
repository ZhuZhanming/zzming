package top.zzming.service;

import top.zzming.model.User;

public interface UserService {

    String encrypt(String str);

    User register(User user);

    int active(String activeCode);
}
