package pl.bloniarz.polsource.model.dao;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VERSIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private long id;

    private String title;
    private String content;
    private LocalDateTime modified;
    private int versionNumber;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private NoteEntity note;

}
