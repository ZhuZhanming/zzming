package top.zzming.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.zzming.exception.SystemException;
import top.zzming.model.FileInfo;
import top.zzming.model.User;
import top.zzming.util.CacheUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private static Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileInfo fileInfo;

    @Autowired
    private CacheUtil cacheUtil;

    @Value("${user.file.dir}")
    private String dirPath;

    @Override
    public void init(User user) {
        Path path = Paths.get(dirPath, user.getUserId().toString());
        try {
            // an exception is not thrown if the directory could not be created because it already exists
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.error("文件夹[{}]：创建失败", path);
            throw new SystemException(e);
        }
        if (fileInfo.getRootPath() == null) {
            fileInfo.setRootPath(path);
        }
        if (fileInfo.getNowPath() == null) {
            fileInfo.setNowPath(path);
        }
        fileInfo.setAlreadySize(cacheUtil.allSize(path));
        if (fileInfo.getAllsize() == 0L) {
            // TODO
            fileInfo.setAllsize(1024 * 1024 * 80);
        }
    }

    @Override
    public List<Path> search() {
        List<Path> paths;
        Path nowPath = fileInfo.getNowPath();
        try (Stream<Path> pathStream = Files.list(nowPath)){
            paths = pathStream.collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("浏览文件夹失败：[{}],", nowPath);
            throw new SystemException(e);
        }
        return paths;
    }

    @Override
    public void upload(MultipartFile[] files,long allSize) {
        if (fileInfo.getAlreadySize() + allSize >= fileInfo.getAllsize()){
            // 大小超过上限
            throw new SystemException("容量不足");
        }
        deleteCache();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Path path = fileInfo.getNowPath().resolve(file.getOriginalFilename());
                cacheUtil.add(file, path);
            }
        }
    }

    @Override
    public void downLoad(String fileName,HttpServletResponse response) {
        Path path = fileInfo.getNowPath().resolve(fileName);
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path));
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-Disposition", "attachment;filename=" + path.getFileName());
            response.setContentType("application/x-msdownload");
            //设置这个，浏览器能看到下载文件的总大小
            response.setContentLengthLong(Files.size(path));
            int data;
            while ((data = bis.read()) != -1) {
                bos.write(data);
            }
        } catch (IOException e) {
            LOGGER.error("文件[{}]:下载失败", path, e);
            throw new SystemException(path.getFileName() + " 下载失败");
        }
    }

    @Override
    public void delete(String fileName) {
        deleteCache();
        Path deletePath = fileInfo.getNowPath().resolve(fileName);
        try {
            if (Files.isDirectory(deletePath)){
                // 只能删除空文件夹
                Files.delete(deletePath);
            }else {
                // 只有删除有大小的文件时，才使用cacheUtil.
                cacheUtil.delete(deletePath);
            }
        }catch (NoSuchFileException e){
            throw new SystemException("文件不存在!");
        }catch (DirectoryNotEmptyException e){
            throw new SystemException("文件夹中存在文件!");
        }catch (IOException e){
            LOGGER.error("文件[{}]:删除失败", deletePath, e);
            throw new SystemException("发生未知删除错误!");
        }

    }

    @Override
    public void cd(String fileName) {
        // normalize：格式化路径
        Path targetPath = fileInfo.getNowPath().resolve(fileName).normalize();
        if (targetPath.startsWith(fileInfo.getRootPath())){
            // 在根目录下面
            fileInfo.setNowPath(targetPath);
        } else {
            // 最多到根目录
            fileInfo.setNowPath(fileInfo.getRootPath());
            throw new SystemException("已经到主目录下了。");
        }
    }

    @Override
    public void mkdir(String fileName) {
        Path dirPath = fileInfo.getNowPath().resolve(fileName);
        if (Files.exists(dirPath)){
            // 该文件夹下的文件名已存在
            throw new SystemException("文件名已存在");
        }
        try {
            cacheUtil.mkdir(dirPath);
        } catch (IOException e) {
            LOGGER.error("文件夹创建失败", dirPath, e);
            throw new SystemException("文件夹创建失败，发生未知错误");
        }
    }

    /**
     * 清空包括当前文件夹的所有缓存
     */
    private void deleteCache(){
        Path cachePath = fileInfo.getNowPath();
        while(cachePath.startsWith(fileInfo.getRootPath())){
            cacheUtil.deleteCache(cachePath);
            cachePath = cachePath.getParent();
        }

    }
}
