package top.zzming.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户信息
 */
public class User implements UserDetails {

    public interface Register{}

    @Null(groups = Register.class)
    private Integer userId;

    private String nickname;

    @NotBlank(groups = Register.class)
    private String username;

    @NotBlank(groups = Register.class)
    private String password;

    @Email(groups = Register.class)
    private String email;

    @Pattern(regexp = "(|\\d{11})", groups = Register.class)
    private String phone;

    @Null(groups = Register.class)
    private LocalDateTime createTime;

    @Null(groups = Register.class)
    private LocalDateTime lastLoginTime;

    @Null(groups = Register.class)
    private Integer lastLoginIp;

    @Null(groups = Register.class)
    private Integer status;

    @Null(groups = Register.class)
    private String activeCode;

    private Set<Role> roles;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(Integer lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null){
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableSet(roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toSet()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != null && status ==1;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp=" + lastLoginIp +
                ", status=" + status +
                ", roles=" + roles +
                '}';
    }
}