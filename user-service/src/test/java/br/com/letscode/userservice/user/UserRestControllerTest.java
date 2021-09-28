package br.com.letscode.userservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestControllerTest.class)
class UserRestControllerTest {

    private static final String URL_USERS = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("RestController test for UserDTO POST method.")
    void save() throws Exception {
        when(userService.save(any(UserDTO.class))).thenAnswer(i -> i.getArgument(0));
        UserDTO userDTO = new UserDTO(1L, "user1", "1234");
        String userJSON = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(post(URL_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJSON));
        verify(userService, times(1)).save(userDTO);
    }

    @Test
    @DisplayName("RestController test for UserDTO DELETE method.")
    void delete() throws Exception {
        Long userId = 1L;
        String urlDelete = URL_USERS + "/{userId}";
        mockMvc.perform(MockMvcRequestBuilders.delete(urlDelete, userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    @DisplayName("RestController test for UserDTO PUT method.")
    void update() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "user1", "1234");
        Long userId = userDTO.getUserId();
        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(userDTO);
        String urlUpdate = URL_USERS + "/{userId}";
        String jsonUser = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(put(urlUpdate, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser));
        verify(userService, times(1)).update(userId, userDTO);
    }

    @Test
    @DisplayName("Exception Handler test when Id NOT FOUND for PUT method.")
    void exceptionHandlerWhenUpdateNotFound() throws Exception {
        UserDTO userDTO = new UserDTO();
        Long userId = 2L;
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(NoSuchElementException.class);
        String urlUpdate = URL_USERS + "/{userId}";
        String jsonUser = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(put(urlUpdate, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).update(userId, userDTO);
    }



}
