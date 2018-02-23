package top.zzming.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.zzming.exception.SystemException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CacheUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(CacheUtil.class);

    @Autowired
    private CacheUtil cacheUtil;

    /**
     * 查看文件或文件夹中所有文件的大小之和，单位字节
     */
    @Cacheable(value = "file", key = "#path.toString()")
    public long allSize(Path path){
        try {
            long size = 0L;
            if (Files.isDirectory(path)) {
                // 文件夹的场合
                // 递归,查询路径下的所有文件和文件夹的大小
                List<Long> sizeList = Files.list(path).parallel().map(p -> cacheUtil.allSize(p)).collect(Collectors.toList());
                // 局部变量不能直接在内部类中使用
                for (long i : sizeList) {
                    size += i;
                }
                return size;
            } else {
                // 文件的场合
                return Files.size(path);
            }
        }catch (AccessDeniedException e){
            return 0L;
        }catch (IOException e){
            LOGGER.error("文件大小读取失败：[{}]", path.toString(), e);
            throw new SystemException("文件大小读取失败!");
        }
    }

    @CachePut(value = "file", key = "#path.toString()")
    public long add(MultipartFile origin, Path path){
        try {
            origin.transferTo(path.toFile());
            return Files.size(path);
        } catch (IOException e) {
            LOGGER.error("文件上传失败:[{}]", origin, e);
            throw new SystemException("网络问题，或者文件名已存在!");
        }
    }

    @CacheEvict(value = "file", key = "#path.toString()")
    public void delete(Path path) throws IOException {
        Files.delete(path);
    }

    @CachePut(value = "file", key = "#path.toString()")
    public long mkdir(Path path) throws IOException {
        Files.createDirectory(path);
        return 0L;
    }

    @CacheEvict(value = "file", key = "#path.toString()")
    public void deleteCache(Path path){
        LOGGER.info("缓存[key={}]已删除。", path);
    }

}
