package pl.bloniarz.polsource.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pl.bloniarz.polsource.model.dto.NoteEditRequest;
import pl.bloniarz.polsource.model.dto.NoteRequest;
import pl.bloniarz.polsource.service.NoteService;

@RequiredArgsConstructor
@Component
public class DatabaseSetup implements CommandLineRunner {

    private final NoteService noteService;

    @Override
    public void run(String... args) throws Exception {
        populateDatabaseWithNotes();
    }

    private void populateDatabaseWithNotes() {
        noteService.createNote(NoteRequest.builder()
                .title("First Note")
                .content("Content of First note")
                .build());
        noteService.createNote(NoteRequest.builder()
                .title("Second note")
                .content("Content of second note")
                .build());
        noteService.editNote(NoteEditRequest.builder()
                .content("Second note, edited content")
                .title("Second Note")
                .build(), 2);
        noteService.createNote(NoteRequest.builder()
                .title("Deleted Note")
                .content("This note will be deleted")
                .build());
        noteService.editNote(NoteEditRequest.builder()
                .content("I will delete this note soon")
                .build(), 3);
        noteService.deleteNote(3);
    }

}
