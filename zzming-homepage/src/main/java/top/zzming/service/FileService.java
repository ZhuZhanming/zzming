package top.zzming.service;

import org.springframework.web.multipart.MultipartFile;
import top.zzming.model.User;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;

public interface FileService {

    void init(User user);

    List<Path> search();

    void upload(MultipartFile[] files, long allSize);

    void downLoad(String fileName, HttpServletResponse response);

    void delete(String fileName);

    void cd(String fileName);

    void mkdir(String fileName);
}
