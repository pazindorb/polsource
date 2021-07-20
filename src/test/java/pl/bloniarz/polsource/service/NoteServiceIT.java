package pl.bloniarz.polsource.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import pl.bloniarz.polsource.PolsourceApplication;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dao.NoteVersionEntity;
import pl.bloniarz.polsource.model.dto.ContentEditRequest;
import pl.bloniarz.polsource.model.dto.NoteHistoryResponse;
import pl.bloniarz.polsource.model.dto.NoteRequest;
import pl.bloniarz.polsource.model.dto.NoteResponse;
import pl.bloniarz.polsource.repository.NoteRepository;
import pl.bloniarz.polsource.repository.NoteVersionRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PolsourceApplication.class)
class NoteServiceIT {

    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteVersionRepository versionRepository;

    LocalDateTime now = LocalDateTime.of(2012, Month.AUGUST, 12, 12, 30);

    @BeforeEach
    void setupDatabase(){
        LocalDateTime now = LocalDateTime.now();

        NoteEntity firstNote = noteRepository.save(
                NoteEntity.builder()
                        .title("First Note")
                        .active(true)
                        .created(now)
                        .versions(new ArrayList<>())
                        .build());
        NoteVersionEntity firstNoteVersionEntity = versionRepository.save(
                NoteVersionEntity.builder()
                        .content("Content of First note")
                        .modified(now)
                        .note(firstNote)
                        .versionNumber(1)
                        .build());
        firstNote.setVersions(Collections.singletonList(firstNoteVersionEntity));

        NoteEntity secondNote = noteRepository.save(
                NoteEntity.builder()
                        .title("Second Note")
                        .active(true)
                        .created(now)
                        .versions(new ArrayList<>())
                        .build());
        NoteVersionEntity secondNoteVersionEntityV1 = versionRepository.save(
                NoteVersionEntity.builder()
                        .content("Content of Second note version 1")
                        .modified(now)
                        .note(secondNote)
                        .versionNumber(1)
                        .build());
        NoteVersionEntity secondNoteVersionEntityV2 = versionRepository.save(
                NoteVersionEntity.builder()
                        .content("Content of Second note version 2")
                        .modified(now)
                        .note(secondNote)
                        .versionNumber(2)
                        .build());
        secondNote.setVersions(Arrays.asList(secondNoteVersionEntityV1, secondNoteVersionEntityV2));
    }

    @Test
    void shouldCreateNewNoteAndSaveItToDatabase(){
        //given
        String title = "Test Title";
        String content = "Test content here";

        NoteRequest noteRequest = NoteRequest.builder()
                .title(title)
                .content(content)
                .build();

        //when
        NoteEntity noteEntity = noteService.createNote(noteRequest);

        //then
        assertEquals(3, noteEntity.getId());
        assertEquals(title, noteEntity.getTitle());
        assertEquals(content, noteEntity.getNewestContent().getContent());
        assertEquals(1, noteEntity.getNewestContent().getVersionNumber());
        assertTrue(noteEntity.isActive());
    }

    @Test
    void shouldEditExistingNoteContentIfExists(){
        //given
        long id = 1;
        String content = "This is new content";

        ContentEditRequest contentEditRequest = ContentEditRequest.builder()
                .content(content)
                .build();

        //when
        NoteEntity noteEntity = noteService.editNote(contentEditRequest, id);

        //then
        assertEquals(id, noteEntity.getId());
        assertEquals("First Note", noteEntity.getTitle());
        assertEquals(content, noteEntity.getNewestContent().getContent());
        assertEquals(2, noteEntity.getNewestContent().getVersionNumber());
    }

    @Test
    void shouldChangeNoteFiledActiveToFalse(){
        //given
        long id = 2;

        //when
        NoteEntity noteEntity = noteService.deleteNote(2);

        //then
        assertFalse(noteEntity.isActive());
        assertEquals(id, noteEntity.getId());

    }

    @Test
    void shouldGetAllActiveNotesWithMostRecentVersions(){
        //when
        List<NoteResponse> noteResponseList = noteService.getAllNotes();

        //then
        assertEquals(2, noteResponseList.size());
        assertEquals("This is new content", noteResponseList.get(0).getContent());
        assertEquals("Test content here", noteResponseList.get(1).getContent());
        assertEquals("First Note", noteResponseList.get(0).getTitle());
        assertEquals("Test Title", noteResponseList.get(1).getTitle());
    }

    @Test
    void shouldGetMostRecentVersionOfNoteWithIdIfExists(){
        //given
        long id = 1;

        //when
        NoteResponse noteResponse = noteService.getNote(id);

        //then
        assertEquals("This is new content", noteResponse.getContent());
        assertEquals("First Note", noteResponse.getTitle());
    }

    @Test
    void shouldGetNoteVersionsHistory(){
        //given
        long id = 2;

        //when
        NoteHistoryResponse noteHistoryResponse = noteService.getNoteHistory(2);

        //then
        assertEquals(2, noteHistoryResponse.getNoteVersionList().size());
        assertEquals("Content of Second note version 1", noteHistoryResponse.getNoteVersionList().get(0).getContent());
        assertEquals("Content of Second note version 2", noteHistoryResponse.getNoteVersionList().get(1).getContent());
        assertEquals(1, noteHistoryResponse.getNoteVersionList().get(0).getVersion());
        assertEquals(2, noteHistoryResponse.getNoteVersionList().get(1).getVersion());
    }

}