package top.zzming.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * 个人文件中的信息
 */
@Component
@SessionScope
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -8580817194868700479L;
    /**
     * 个人文件的根目录
     */
    private Path rootPath;
    /**
     * 当前目录
     */
    private Path nowPath;
    /**
     * 已使用大小
     */
    private long alreadySize;
    /**
     * 可以使用大小
     */
    private long allsize;


    /**
     * @return the rootPath
     */
    public Path getRootPath() {
        return rootPath;
    }
    /**
     * @param rootPath the rootPath to set
     */
    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
    /**
     * @return the nowPath
     */
    public Path getNowPath() {
        return nowPath;
    }
    /**
     * 只允许用户访问到自己的根目录
     */
    public void setNowPath(Path nowPath) {
        if (nowPath.startsWith(rootPath)) {
            this.nowPath = nowPath;
        } else {
            this.nowPath = rootPath;
        }
    }
    /**
     * @return the alreadySize
     */
    public long getAlreadySize() {
        return alreadySize;
    }
    /**
     * @param alreadySize the alreadySize to set
     */
    public void setAlreadySize(long alreadySize) {
        this.alreadySize = alreadySize;
    }
    /**
     * @return the allsize
     */
    public long getAllsize() {
        return allsize;
    }
    /**
     * @param allsize the allsize to set
     */
    public void setAllsize(long allsize) {
        this.allsize = allsize;
    }

}
