package pl.bloniarz.polsource.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.bloniarz.polsource.PolsourceApplication;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest(classes = PolsourceApplication.class)
@RequiredArgsConstructor
@AutoConfigureMockMvc
@Sql("/sql/note-controller-test.sql")
class NoteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateNewInstanceOfNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Title\", \"content\":\"Test Content\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", equalTo("Test Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", equalTo("Test Content")));
    }

    @Test
    void shouldEditNoteAndReturnEditedOne() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/notes/998")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"new\", \"content\":\"edit\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", equalTo("new")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", equalTo("edit")));
    }

    @Test
    void shouldGetAllNotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", equalTo("Content of second note v2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content", equalTo("Content of first note")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", equalTo("Second Note")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", equalTo("First Note")));
    }

    @Test
    void shouldGetNoteWithMatchingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/998"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", equalTo("Content of second note v2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", equalTo("Second Note")));
    }

    @Test
    void shouldGetNoteWithItsHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/998/history"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.noteVersionList[0].content", equalTo("Content of second note v1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.noteVersionList[1].content", equalTo("Content of second note v2")));
    }

    @Test
    void shouldDeleteNoteFromDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/998"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/998"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/998"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenRetrievingNotExistingNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/99999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Note with id: 99999 not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime", notNullValue()));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithNoChanges() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/notes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"First Note\", \"content\":\"Content of first note\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("There is nothing to edit")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime", notNullValue()));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithEmptyFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/notes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"\", \"content\":\"\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("There is nothing to edit")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime", notNullValue()));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingNotExistingNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/notes/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"\", \"content\":\"edit\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Note with id: 99999 not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime", notNullValue()));
    }

    @Test
    void shouldReturnBadRequestWhenGetHistoryForExistingNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/99999/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"\", \"content\":\"edit\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Note with id: 99999 not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime", notNullValue()));
    }


}