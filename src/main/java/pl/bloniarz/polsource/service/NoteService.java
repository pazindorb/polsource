package pl.bloniarz.polsource.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dao.NoteVersionEntity;
import pl.bloniarz.polsource.model.dto.ContentEditRequest;
import pl.bloniarz.polsource.model.dto.NoteRequest;
import pl.bloniarz.polsource.model.dto.NoteResponse;
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

    public NoteEntity createNote(NoteRequest noteRequest) {
        LocalDateTime now = LocalDateTime.now();

        NoteEntity noteEntity = noteRepository.save(
                NoteEntity.builder()
                        .title(noteRequest.getTitle())
                        .active(true)
                        .created(now)
                        .versions(new ArrayList<>())
                        .build());

        NoteVersionEntity noteVersionEntity = versionRepository.save(
                NoteVersionEntity.builder()
                        .content(noteRequest.getContent())
                        .modified(now)
                        .note(noteEntity)
                        .versionNumber(1)
                        .build());

        noteEntity.setVersions(Collections.singletonList(noteVersionEntity));

        return noteEntity;
    }

    @Transactional
    public NoteEntity editNote(ContentEditRequest contentEditRequest, long id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElseThrow(
                () -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND,Long.toString(id))
        );
        List<NoteVersionEntity> noteVersionEntityList = noteEntity.getVersions();
        noteVersionEntityList.add(NoteVersionEntity.builder()
                .content(contentEditRequest.getContent())
                .modified(LocalDateTime.now())
                .note(noteEntity)
                .versionNumber(noteEntity.getNewestContent().getVersionNumber() + 1)
                .build());
        noteEntity.setVersions(noteVersionEntityList);

        return noteEntity;
    }

    @Transactional
    public List<NoteResponse> getAllNotes() {
         return noteRepository.findAll().stream()
                .map(note -> NoteResponse.builder()
                        .title(note.getTitle())
                        .content(note.getNewestContent().getContent())
                        .created(note.getCreated())
                        .modified(note.getNewestContent().getModified())
                        .build())
                .collect(Collectors.toList());
    }
}
