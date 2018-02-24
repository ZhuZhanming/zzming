package top.zzming.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zzming.model.NoteCodeList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 笔记本界面
 */
@Controller
@RequestMapping("notes")
public class NoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    @GetMapping("/{dir}")
    public String findNotes(@PathVariable("dir") String dir, Model model){
        Path noteDir = NoteCodeList.noteRoot.resolve(dir);
        try {
            List<String> noteList = Files.list(noteDir).map(p -> p.getFileName().toString()).collect(Collectors.toList());
            model.addAttribute("noteList", noteList);
            model.addAttribute("dir", dir);
        } catch (IOException e) {
            LOGGER.error("浏览笔记失败！", e);
            return "redirect:/home";
        }
        return "note/notes";
    }

    @GetMapping(value = "/{dir}", params = "note")
    @ResponseBody
    public String readNoote(@PathVariable("dir") String dir, @Param("note") String note){
        Path noteFile = NoteCodeList.noteRoot.resolve(dir).resolve(note);
        if (noteFile.relativize(NoteCodeList.noteRoot).getNameCount() <= 0){
            // 防止攻击
            return "服务器繁忙";
        }
        try {
            String info = new String(Files.readAllBytes(noteFile));
            return info;
        } catch (IOException e) {
            LOGGER.error("查询笔记失败！", e);
            return "服务器繁忙";
        }
    }

}
