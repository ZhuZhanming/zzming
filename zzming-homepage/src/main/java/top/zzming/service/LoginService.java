package top.zzming.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.zzming.feign.UserFeignClient;
import top.zzming.model.User;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserFeignClient client;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return client.findByUsername(username);
    }
}
