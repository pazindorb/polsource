package pl.bloniarz.polsource.model.dao;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "NOTES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private long id;
    private LocalDateTime created;
    private boolean active;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<NoteVersionEntity> versions;

}
