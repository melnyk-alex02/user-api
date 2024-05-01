package com.alex.clearSolutionsTask.restcontroller;

import com.alex.clearSolutionsTask.ClearSolutionsTaskApplication;
import com.alex.clearSolutionsTask.dto.UserCreateDTO;
import com.alex.clearSolutionsTask.dto.UserPatchDTO;
import com.alex.clearSolutionsTask.dto.UserUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClearSolutionsTaskApplication.class)
public class UserControllerTests extends BaseWebTest {

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test
    public void testSearchUserByDateOfBirth() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("from", "1980-03-01")
                        .param("to", "1988-07-18"))

                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("email_create@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(18),
                "address1",
                "123-123-1234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testCreateUser_WhenAgeUnder18_ShouldThrowException() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("email1@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(10),
                "adress",
                "123-123-1234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser_WhenAgeIsFutureDate_ShouldThrowException() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("email2@example.com",
                "firstName",
                "lastName",
                LocalDate.now().plusYears(2),
                "address",
                "123-123-1234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser_WhenNumberIsNotValid_ShouldThrowException() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "email4@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(18),
                "address",
                "1231231234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser_WhenRequestBodyIsNotValid() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(null,
                null,
                null,
                null,
                null,
                null);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1L,
                "email@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(18),
                "adress",
                "123-123-1234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testUpdateUser_WhenAgeUnder18_ShouldThrowException() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1L,
                "email@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(10),
                "adress",
                "123-123-1234");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_WhenAgeIsFutureDate_ShouldThrowException() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                1L,
                "email3@example.com",
                "firstName",
                "lastName",
                LocalDate.now().plusYears(2),
                "address",
                "123-123-1234"
        );

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_WhenNumberIsNotValid_ShouldThrowException() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                1L,
                "email4@example.com",
                "firstName",
                "lastName",
                LocalDate.now().minusYears(18),
                "address",
                "1231231234");

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_WhenRequestBodyIsInvalid_ShouldThrowException() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                3L,
                null,
                null,
                null,
                null,
                null,
                null);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchUser() throws Exception {
        UserPatchDTO userPatchDTO = new UserPatchDTO(1L, "email_patched@example.com", null, null,
                LocalDate.now().minusYears(18), null, "123-123-1234");

        mockMvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(userPatchDTO.email()));
    }

    @Test
    void testPatchUser_WhenAgeIsFutureDate_ShouldReturnBadRequest() throws Exception {
        UserPatchDTO userPatchDTO = new UserPatchDTO(1L, "test@example.com", null, null,
                LocalDate.now().minusYears(10), null, "555-1234"); // Age under 18

        mockMvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchUser_WhenNumberIsNotValid_ShouldThrowException() throws Exception {
        UserPatchDTO userPatchDTO = new UserPatchDTO(
                1L,
                null,
                null,
                null,
                null,
                null,
                "1231231234");

        mockMvc.perform(patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDTO)))
                .andExpect(status().isBadRequest());
    }
}