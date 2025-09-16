package com.practice.TodoApp.Service;

import com.practice.TodoApp.Model.Task;
import com.practice.TodoApp.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private int test;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public void createTask(String title){
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(false);
        taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void updateTask(Long id, Task task){
        Task task1 = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if(task1!=null){
            if(!(task.getTitle().equals(task1.getTitle()))) task1.setTitle(task.getTitle());
            if(task.isStatus() != task1.isStatus()) task1.setStatus(task.isStatus());
            taskRepository.save(task1);
        }
    }

    public Long deleteTask(Long id){
        taskRepository.deleteById(id);
        return id;
    }

    public List<Task> searchTask(String title){
        return taskRepository.searchTaskByTitle(title);
    }

    public void changeStatus(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID!"));
        task.setStatus(!task.isStatus());
        taskRepository.save(task);
    }
}
