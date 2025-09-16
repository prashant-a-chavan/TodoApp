package com.practice.TodoApp.Service;

import com.practice.TodoApp.Model.Task;
import com.practice.TodoApp.Repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask(){
        Task task = new Task();
        task.setTitle("Test One");
        task.setStatus(true);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.createTask("Test One");

        verify(taskRepository, times(1)).save(any(Task.class));
        assertThat(task.getTitle()).isEqualTo("Test One");
    }

    @Test
    void shouldGetAllTasks(){
        List<Task> tasks = List.of(new Task(1L, "Test 1", false), new Task(2L, "Test 2", true));

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> allTasks = taskService.getAllTasks();

        assertThat(allTasks).hasSize(2);
        assertThat(allTasks.get(0).getId()).isEqualTo(1L);
        assertThat(allTasks.get(0).getTitle()).isEqualTo("Test 1");
        assertThat(allTasks.get(0).isStatus()).isEqualTo(false);
        assertThat(allTasks.get(1).getId()).isEqualTo(2L);
        assertThat(allTasks.get(1).getTitle()).isEqualTo("Test 2");
        assertThat(allTasks.get(1).isStatus()).isEqualTo(true);

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateTask(){
        Task existingTask = new Task(1L, "Test", false);
        Task updatedTask = new Task(1L, "Updated Test", true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        taskService.updateTask(1L, updatedTask);

        assertThat(existingTask.getTitle()).isEqualTo("Updated Test");
        assertThat(existingTask.isStatus()).isEqualTo(true);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask(){
        Task task = new Task(1L, "Test", false);

        doNothing().when(taskRepository).deleteById(1L);

        Long deletedId = taskService.deleteTask(1L);

        assertThat(deletedId).isEqualTo(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldToggleTaskStatus(){
        Task task = new Task(1L, "Test", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

    }

}