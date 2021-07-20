package pl.bloniarz.polsource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dto.*;
import pl.bloniarz.polsource.service.NoteService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleResponse createNote(@Valid @RequestBody NoteRequest noteRequest){
        String title = noteService.createNote(noteRequest).getTitle();
        return new SimpleResponse(String.format("Created note with title: %s.", title));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse createNote(@Valid @RequestBody ContentEditRequest contentEditRequest, @PathVariable long id){
        String title = noteService.editNote(contentEditRequest, id).getTitle();
        return new SimpleResponse(String.format("Edited note with title: %s.", title));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponse> getAllNotes(){
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse getNote(@PathVariable long id){
        return noteService.getNote(id);
    }

    @GetMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteHistoryResponse getNoteHistory(@PathVariable long id){
        return noteService.getNoteHistory(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteNote(@PathVariable long id){
        String title = noteService.deleteNote(id).getTitle();
        return new SimpleResponse(String.format("Note with title: %s, succesfully removed.", title));
    }

}
