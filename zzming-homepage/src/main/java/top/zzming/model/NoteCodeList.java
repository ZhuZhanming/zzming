package top.zzming.model;

import org.springframework.stereotype.Component;
import top.zzming.exception.SystemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 站长笔记模块的下拉列表信息
 */
@Component
public class NoteCodeList {

    /**
     * 笔记文件的根目录
     */
    private String noteDir;

    /**
     * 笔记文件的根目录的Path类型
     */
    private static Path noteRoot;

    /**
     * 笔记文件夹的名字列表
     */
    private final List<String> notes;

    public NoteCodeList() {
        try {
            noteDir = "/usr/zzming/notes";
            noteRoot = Paths.get(noteDir);
            notes = Files.list(noteRoot).map(p -> p.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public List<String> getNotes() {
        return notes;
    }

    public static Path getNoteRoot() {
        return noteRoot;
    }
}
