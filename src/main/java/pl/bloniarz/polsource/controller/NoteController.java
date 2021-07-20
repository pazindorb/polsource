package pl.bloniarz.polsource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dto.ContentEditRequest;
import pl.bloniarz.polsource.model.dto.NoteRequest;
import pl.bloniarz.polsource.model.dto.NoteResponse;
import pl.bloniarz.polsource.service.NoteService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteEntity createNote(@Valid @RequestBody NoteRequest noteRequest){
        return noteService.createNote(noteRequest);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteEntity createNote(@Valid @RequestBody ContentEditRequest contentEditRequest, @PathVariable long id){
        return noteService.editNote(contentEditRequest, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponse> getAllNotes(){
        return noteService.getAllNotes();
    }


}
