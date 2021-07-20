package pl.bloniarz.polsource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NoteVersion {

    private String content;
    private int version;
    private LocalDateTime modified;

}
