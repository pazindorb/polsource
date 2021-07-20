package pl.bloniarz.polsource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoteResponse {

    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
