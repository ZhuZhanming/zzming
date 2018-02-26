package top.zzming.service;

import org.springframework.web.multipart.MultipartFile;
import top.zzming.model.User;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;

/**
 * 个人文件模块的业务层
 */
public interface FileService {

    /**
     * 初始化session中FileInfo的信息
     * @param user:登录的用户
     */
    void init(User user);

    /**
     * 查看当前目录文件列表
     */
    List<Path> search();

    /**
     * 上传文件
     * @param files 要上传的文件数组
     * @param allSize 请求的总大小
     */
    void upload(MultipartFile[] files, long allSize);

    /**
     * 下载文件
     * @param fileName 要下载文件的文件名
     * @param response
     */
    void downLoad(String fileName, HttpServletResponse response);

    /**
     * 删除文件
     * @param fileName 要删除文件的文件名
     */
    void delete(String fileName);

    /**
     * 改变当前目录
     * @param fileName 要改变目录的名字
     */
    void cd(String fileName);

    /**
     * 新建文件夹
     * @param fileName 文件夹名
     */
    void mkdir(String fileName);
}
