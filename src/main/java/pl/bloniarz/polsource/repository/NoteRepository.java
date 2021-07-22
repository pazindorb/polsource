package pl.bloniarz.polsource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.polsource.model.dao.NoteEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    Optional<NoteEntity> findByIdAndActive(long id, boolean active);

    @Query("select distinct n.id, n.active, n.created, v.modified, v.title, v.content, v.versionNumber " +
            "from NoteEntity n " +
            "         left join NoteVersionEntity v " +
            "                   on n.id = v.note.id " +
            "         inner join NoteVersionEntity v2 " +
            "                    on v.versionNumber = " +
            "                       (select max(v.versionNumber) from NoteVersionEntity v where v.note.id = n.id) " +
            "                    where n.active = true")
    List<Object[]> findAllActiveNewestNotes();

}
