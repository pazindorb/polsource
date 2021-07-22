package pl.bloniarz.polsource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public NoteResponse createNote(@Valid @RequestBody NoteRequest noteRequest){
        return noteService.createNote(noteRequest);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse editNote(@RequestBody NoteEditRequest editedNote, @PathVariable long id){
        return noteService.editNote(editedNote, id);
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
    public NoteResponse deleteNote(@PathVariable long id){
        return noteService.deleteNote(id);
    }

}
