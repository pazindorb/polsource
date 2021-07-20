package pl.bloniarz.polsource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.polsource.model.dao.NoteEntity;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

}
