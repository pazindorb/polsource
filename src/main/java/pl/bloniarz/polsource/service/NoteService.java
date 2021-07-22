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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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

        return parseNoteEntityToNoteResponse(noteEntity, noteVersionEntity);
    }

    public NoteResponse editNote(NoteEditRequest request, long id) {

        NoteEntity noteEntity = findNoteByIdOrThrowException(id);

        String newContent = Optional.ofNullable(request)
                .filter(editRequest -> Objects.nonNull(editRequest.getContent()))
                .map(NoteEditRequest::getContent)
                .filter(content -> !"".equals(content))
                .orElse(null);

        String newTitle = Optional.ofNullable(request)
                .filter(editRequest -> Objects.nonNull(editRequest.getTitle()))
                .map(NoteEditRequest::getTitle)
                .filter(title -> !"".equals(title))
                .orElse(null);

        if(Objects.isNull(newContent) && Objects.isNull(newTitle)){
            throw new AppException(AppExceptionMessage.NO_CHANGES);
        }

        NoteVersionEntity lastNoteVersion = findLastForNoteOrThrowException(noteEntity);

        if (lastNoteVersion.getContent().equals(newContent) && lastNoteVersion.getTitle().equals(newTitle)) {
            throw new AppException(AppExceptionMessage.NO_CHANGES);
        }

        List<NoteVersionEntity> noteVersionEntityList = noteEntity.getVersions();
        NoteVersionEntity newVersion = NoteVersionEntity.builder()
                .content(Optional.ofNullable(newContent).orElse(lastNoteVersion.getContent()))
                .title(Optional.ofNullable(newTitle).orElse(lastNoteVersion.getTitle()))
                .modified(LocalDateTime.now())
                .note(noteEntity)
                .versionNumber(lastNoteVersion.getVersionNumber() + 1)
                .build();
        noteVersionEntityList.add(newVersion);
        noteEntity.setVersions(noteVersionEntityList);

        return parseNoteEntityToNoteResponse(noteEntity, newVersion);
    }

    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAllActiveActualNotes().stream()
                .map(this::convertRawTupleToNoteResponse)
                .collect(Collectors.toList());
    }

    public NoteResponse getNote(long id) {
        return parseNoteEntityToNoteResponse(findNoteByIdOrThrowException(id));
    }

    public NoteHistoryResponse getNoteHistory(long id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElseThrow(
                () -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND, Long.toString(id))
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

    public NoteResponse deleteNote(long id) {
        NoteEntity noteEntity = findNoteByIdOrThrowException(id);
        noteEntity.setActive(false);
        return parseNoteEntityToNoteResponse(noteEntity);
    }

    private NoteResponse convertRawTupleToNoteResponse(Object[] tuple) {
        return NoteResponse.builder()
                .created((LocalDateTime) tuple[2])
                .modified((LocalDateTime) tuple[3])
                .title((String) tuple[4])
                .content((String) tuple[5])
                .build();
    }

    private NoteResponse parseNoteEntityToNoteResponse(NoteEntity note) {
        NoteVersionEntity lastVersion = findLastForNoteOrThrowException(note);
        return parseNoteEntityToNoteResponse(note, lastVersion);
    }

    private NoteResponse parseNoteEntityToNoteResponse(NoteEntity note, NoteVersionEntity lastVersion) {
        return NoteResponse.builder()
                .created(note.getCreated())
                .modified(lastVersion.getModified())
                .title(lastVersion.getTitle())
                .content(lastVersion.getContent())
                .build();
    }

    private NoteVersionEntity findLastForNoteOrThrowException(NoteEntity entity) {
        return versionRepository.findDistinctTopByNoteOrderByVersionNumberDesc(entity)
                .orElseThrow(() -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND, String.valueOf(entity.getId())));
    }

    private NoteEntity findNoteByIdOrThrowException(long id) {
        return noteRepository.findByIdAndActive(id, true).orElseThrow(
                () -> new AppException(AppExceptionMessage.NOTE_NOT_FOUND, Long.toString(id))
        );
    }


}
