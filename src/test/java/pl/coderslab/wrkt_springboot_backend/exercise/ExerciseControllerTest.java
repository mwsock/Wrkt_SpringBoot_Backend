package pl.coderslab.wrkt_springboot_backend.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.session.SessionFilter;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExerciseService exerciseService;
    @MockBean
    private InMemorySessionRegistry registry;
    @MockBean
    private UserDetailsService userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_return_401_http_error_for_no_authentication() throws Exception {
        //given
        String sessionId = "1234567890";
        ExerciseDTO exerciseDTOOne = ExerciseDTO.builder()
                .id(1L)
                .name("Test Exercise")
                .build();
        ExerciseDTO exerciseDTOThree = ExerciseDTO.builder()
                .id(3L)
                .name("Third Test Exercise")
                .build();
        List<ExerciseDTO> exerciseDTOList = Arrays.asList(exerciseDTOOne,exerciseDTOThree);
        //when
        when(registry.getUserNameForSession(sessionId)).thenReturn("Test User");
        when(exerciseService.getExercises(sessionId)).thenReturn(exerciseDTOList);
        //then
        mockMvc.perform(get("/exercises"))
                .andExpect(status().is4xxClientError());
    }

}