package top.zzming.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zzming.dao.UserMapper;
import top.zzming.model.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Value("${default.role-id}")
    private Integer roleId;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public void register(User user) {
        userMapper.insertSelective(user);
        userMapper.insertUserRole(user.getUserId(),roleId);
    }

    @Override
    public int active(String activeCode) {
        return userMapper.activeUser(activeCode);
    }
}
