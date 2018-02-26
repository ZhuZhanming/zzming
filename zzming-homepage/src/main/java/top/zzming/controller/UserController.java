package top.zzming.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.zzming.model.AlertMessage;
import top.zzming.model.MsgKind;
import top.zzming.model.User;
import top.zzming.service.UserService;

/**
 * 用户模块的controller
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * 管理员权限下的加密密码功能
     */
    @GetMapping("/{password}")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public String encryptPassword(@PathVariable("password") String password){
        return userService.encrypt(password);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public String registerAction(@Validated(User.Register.class) User user, RedirectAttributes redirectAttributes){
        try{
            userService.register(user);
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "注册成功", "请查看你的邮箱进行激活"));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "注册失败", "服务器繁忙"));
            LOGGER.error("注册失败：user:[{}]", user, e);
        }
        return "redirect:/login";
    }

    /**
     * 激活
     */
    @GetMapping("/active/{activeCode}")
    public String activeDo(@PathVariable("activeCode") String activeCode,RedirectAttributes redirectAttributes){
        if (userService.active(activeCode) != 1){
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "激活失败", "服务器繁忙"));
        }else {
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "激活成功", "现在可以登录了"));
        }
        return "redirect:/login";
    }
}
