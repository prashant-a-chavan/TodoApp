package com.practice.TodoApp.Controller;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.TodoApp.Model.Task;
import com.practice.TodoApp.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTask() throws Exception {
        String title = "Test One";

        doNothing().when(taskService).createTask(title);

        mockMvc.perform(post("/tasks/create")
                        .param("title", title))
                .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(title);     // To ensure the api is calling only once
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        List<Task> mockTasks = List.of(
                new Task(1L, "Task One", false),
                new Task(2L, "Task Two", false)
        );

        when(taskService.getAllTasks()).thenReturn(mockTasks);

        mockMvc.perform(get("/tasks/fetch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task One"))
                .andExpect(jsonPath("$[0].status").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task Two"))
                .andExpect(jsonPath("$[1].status").value(false));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task updatedTask = new Task(1L, "Updated Title", false);

        doNothing().when(taskService).updateTask(1L, updatedTask);

        mockMvc.perform(put("/tasks/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateTask(1L, updatedTask);
    }

    @Test
    void shouldHandleExceptionDuringUpdateTask() throws Exception {
        Task updatedTask = new Task(1L, "Updated Title", false);

        doThrow(RuntimeException.class).when(taskService).updateTask(1L, updatedTask);

        mockMvc.perform(put("/tasks/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isInternalServerError());

        verify(taskService, times(1)).updateTask(1L, updatedTask);
    }

    @Test
    void shouldDeleteTask() throws Exception {
//        doNothing().when(taskService).deleteTask(1L);
        when(taskService.deleteTask(1L)).thenReturn(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted successfully"));

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void shouldHandleExceptionDuringDeleteTask() throws Exception {
        doThrow(RuntimeException.class).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("delete failed"));

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void shouldChangeTaskStatus() throws Exception {
        doNothing().when(taskService).changeStatus(1L);

        mockMvc.perform(put("/tasks/status/1", 1L))
                .andExpect(status().isOk());

        verify(taskService, times(1)).changeStatus(1L);
    }

    @Test
    void shouldHandleExceptionDuringChangeTaskStatus() throws Exception {
        doThrow(RuntimeException.class).when(taskService).changeStatus(1L);

        mockMvc.perform(put("/tasks/status/1", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("change failed"));
        verify(taskService, times(1)).changeStatus(1L);
    }

    @Test
    void shouldSearchTaskByTitle() throws Exception {
        List<Task> mockTasks = List.of(
                new Task(1L, "Task One", false),
                new Task(2L, "Task Two", false)
        );
        when(taskService.searchTask(anyString())).thenReturn(mockTasks);
        mockMvc.perform(get("/tasks/search?title=Task One"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task One"))
                .andExpect(jsonPath("$[0].status").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task Two"))
                .andExpect(jsonPath("$[1].status").value(false));

        verify(taskService, times(1)).searchTask("Task One");
    }
}