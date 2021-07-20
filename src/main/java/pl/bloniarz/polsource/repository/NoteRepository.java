package pl.bloniarz.polsource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.polsource.model.dao.NoteEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    Optional<NoteEntity> findByIdAndActive(long id, boolean active);

    List<NoteEntity> findAllByActive(boolean active);

}
