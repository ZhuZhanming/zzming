package top.zzming.service;

import top.zzming.model.User;

public interface UserService {

    User findByUsername(String username);

    void register(User user);

    int active(String activeCode);
}
