package pl.bloniarz.polsource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    @NotNull
    @NotEmpty
    private String title;
    @NotNull
    @NotEmpty
    private String content;

}
