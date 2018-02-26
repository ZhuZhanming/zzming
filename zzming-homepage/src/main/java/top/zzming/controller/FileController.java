package top.zzming.controller;

import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import top.zzming.exception.SystemException;
import top.zzming.model.AlertMessage;
import top.zzming.model.MsgKind;
import top.zzming.model.User;
import top.zzming.service.FileService;

/**
 * 个人文件界面的控制层
 */
@Controller
@RequestMapping("/files")
public class FileController {

    private static Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    /**
     * 注入业务层
     */
    @Autowired
    FileService fileService;

    /**
     * 每次请求的初始化
     */
    @ModelAttribute
    public void init(@AuthenticationPrincipal User user) {

        fileService.init(user);
    }

    /**
     * 浏览当前目录
     */
    @GetMapping
    public String handleFileList(Model model) {
        List<Path> paths = fileService.search();
        model.addAttribute("paths", paths);
        return "file/files";
    }

    /**
     * 在当前目录上传文件
     */
    @PostMapping(value = "/upload")
    public String handleFileUpload(@RequestParam(value = "fileName") MultipartFile[] files,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try{
            fileService.upload(files, request.getContentLengthLong());
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "上传成功", ""));
        }catch (SystemException e){
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.DANGER, "上传失败", e.getMessage()));
        }
        return "redirect:/files";
    }

    /**
     * 下载文件
     */
    @GetMapping(value = "/download", params = "fileName")
    public String handleFileDownLoad(@RequestParam("fileName") String fileName,
                                     RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            fileService.downLoad(fileName, response);
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "下载完成", ""));
        }catch (SystemException e){
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.DANGER,"下载失败", e.getMessage()));
        }
        return "redirect:/files";
    }

    /**
     * 删除文件
     */
    @GetMapping(value = "/delete", params = "fileName")
    public String handleFileDelete(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes) {
        try {
            fileService.delete(fileName);
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "删除成功", ""));
        } catch (SystemException e) {
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.DANGER, "删除失败!", e.getMessage()));
        }
        return "redirect:/files";
    }

    /**
     * 浏览文件夹
     */
    @GetMapping(value = "/link", params = "fileName")
    public String changDirectory(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes) {
        try{
            fileService.cd(fileName);
        }catch (SystemException e){
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.WARNING, "操作失败!", e.getMessage()));
        }
        return "redirect:/files";
    }

    /**
     * 创建文件夹
     */
    @GetMapping(value = "/mkdir", params = "fileName")
    public String handleMkdir(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes) {
        try {
            fileService.mkdir(fileName);
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.SUCCESS, "文件夹已创建", ""));
        } catch (SystemException e) {
            redirectAttributes.addFlashAttribute(new AlertMessage(MsgKind.DANGER, "新建文件夹失败!", e.getMessage()));
        }
        return "redirect:/files";
    }

}
