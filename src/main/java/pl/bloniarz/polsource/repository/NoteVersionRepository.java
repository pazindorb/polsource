package pl.bloniarz.polsource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.polsource.model.dao.NoteVersionEntity;

@Repository
public interface NoteVersionRepository extends JpaRepository<NoteVersionEntity,Long> {
}
