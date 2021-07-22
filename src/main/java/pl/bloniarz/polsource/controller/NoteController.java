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
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleResponse createNote(@Valid @RequestBody NoteRequest noteRequest){
        NoteEntity note = noteService.createNote(noteRequest);
        return new SimpleResponse(String.format("Created note with title: %s.", note.getNewestContent().getTitle()));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse updateNote(@Valid @RequestBody ContentEditRequest contentEditRequest, @PathVariable long id){
        NoteEntity note = noteService.editNote(contentEditRequest, id);
        return new SimpleResponse(String.format("Edited note with title: %s.", note.getNewestContent().getTitle()));
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

    @GetMapping("/{id}/history")
    @ResponseStatus(HttpStatus.OK)
    public NoteHistoryResponse getNoteHistory(@PathVariable long id){
        return noteService.getNoteHistory(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteNote(@PathVariable long id){
        NoteEntity noteEntity = noteService.deleteNote(id);
        return new SimpleResponse(String.format("Note with title: %s, succesfully removed.", noteEntity.getNewestContent().getTitle()));
    }

}
