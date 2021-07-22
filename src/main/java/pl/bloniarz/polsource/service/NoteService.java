package pl.bloniarz.polsource.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dao.NoteVersionEntity;
import pl.bloniarz.polsource.model.dto.*;
import pl.bloniarz.polsource.model.exceptions.AppException;
import pl.bloniarz.polsource.model.exceptions.AppExceptionMessage;
import pl.bloniarz.polsource.repository.NoteRepository;
import pl.bloniarz.polsource.repository.NoteVersionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteVersionRepository versionRepository;

    public NoteResponse createNote(NoteRequest noteRequest) {
        LocalDateTime now = LocalDateTime.now();

        NoteEntity noteEntity = noteRepository.save(
                NoteEntity.builder()
                        .active(true)
                        .created(now)
                        .versions(new ArrayList<>())
                        .build());

        NoteVersionEntity noteVersionEntity = versionRepository.save(
                NoteVersionEntity.builder()
                        .content(noteRequest.getContent())
                        .modified(now)
                        .title(noteRequest.getTitle())
                        .note(noteEntity)
                        .versionNumber(1)
                        .build());

        noteEntity.setVersions(Collections.singletonList(noteVersionEntity));

        return parseNoteEntityToNoteResponse(noteEntity);
    }

    @Transactional
    public NoteResponse editNote(NoteEditRequest editedNote, long id) {
        NoteEntity noteEntity = findNoteByIdOrThrowException(id);
        String newContent = noteEntity.getNewestContent().getContent();
        String newTitle = noteEntity.getNewestContent().getTitle();
        if(editedNote != null  && !editedNote.getContent().equals(""))
            newContent = editedNote.getContent();
        if(editedNote != null  && !editedNote.getTitle().equals(""))
            newTitle = editedNote.getTitle();

        if(noteEntity.getNewestContent().getContent().equals(newContent)
                && noteEntity.getNewestContent().getTitle().equals(newTitle))
            throw new AppException(AppExceptionMessage.NO_CHANGES);

        List<NoteVersionEntity> noteVersionEntityList = noteEntity.getVersions();
        noteVersionEntityList.add(NoteVersionEntity.builder()
                .content(newContent)
                .title(newTitle)
                .modified(LocalDateTime.now())
                .note(noteEntity)
                .versionNumber(noteEntity.getNewestContent().getVersionNumber() + 1)
                .build());
        noteEntity.setVersions(noteVersionEntityList);

        return parseNoteEntityToNoteResponse(noteEntity);
    }

    @Transactional
    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAllActiveNewestNotes().stream()
                .map(this::convertRawTutleToNoteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NoteResponse getNote(long id) {
        return parseNoteEntityToNoteResponse(findNoteByIdOrThrowException(id));
    }

    @Transactional
    public NoteHistoryResponse getNoteHistory(long id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElseThrow(
                () -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND,Long.toString(id))
        );
        return NoteHistoryResponse.builder()
                .created(noteEntity.getCreated())
                .noteVersionList(noteEntity.getVersions().stream()
                        .map(note -> NoteVersion.builder()
                                .version(note.getVersionNumber())
                                .content(note.getContent())
                                .modified(note.getModified())
                                .title(note.getTitle())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }



    @Transactional
    public NoteResponse deleteNote(long id) {
        NoteEntity noteEntity = findNoteByIdOrThrowException(id);
        noteEntity.setActive(false);
        return parseNoteEntityToNoteResponse(noteEntity);
    }

    private NoteEntity findNoteByIdOrThrowException(long id) {
        return noteRepository.findByIdAndActive(id, true).orElseThrow(
                () -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND,Long.toString(id))
        );
    }

    private NoteResponse convertRawTutleToNoteResponse(Object[] tuple) {
        return NoteResponse.builder()
                .created((LocalDateTime) tuple[2])
                .modified((LocalDateTime)tuple[3])
                .title((String) tuple[4])
                .content((String) tuple[5])
                .build();
    }


    private NoteResponse parseNoteEntityToNoteResponse(NoteEntity note) {
        return NoteResponse.builder()
                .content(note.getNewestContent().getContent())
                .title(note.getNewestContent().getTitle())
                .created(note.getCreated())
                .modified(note.getNewestContent().getModified())
                .build();
    }

}
