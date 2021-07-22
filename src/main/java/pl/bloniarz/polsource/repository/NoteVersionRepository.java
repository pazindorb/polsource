package pl.bloniarz.polsource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.polsource.model.dao.NoteEntity;
import pl.bloniarz.polsource.model.dao.NoteVersionEntity;

import java.util.Optional;

@Repository
public interface NoteVersionRepository extends JpaRepository<NoteVersionEntity,Long> {
    Optional<NoteVersionEntity> findDistinctTopByNoteOrderByVersionNumberDesc(NoteEntity noteEntity);
}
