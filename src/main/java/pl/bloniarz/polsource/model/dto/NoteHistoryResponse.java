package pl.bloniarz.polsource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteHistoryResponse {

    private LocalDateTime created;
    private List<NoteVersion> noteVersionList;

}
