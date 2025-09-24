package com.practice.TodoApp.Controller;
import com.practice.TodoApp.Model.Task;
import com.practice.TodoApp.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3001")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

// more specific APIs like create
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestParam String title)
    {
        taskService.createTask(title);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


    @GetMapping("/fetch")
    public ResponseEntity<List<Task>> getTasks()
    {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task updatedTask)
    {
        try{
            taskService.updateTask(id, updatedTask);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    NOTE : To Test Update API,
//    PUT http://localhost:8080/tasks/update/1
//    Content-Type: application/json
//
//    {
//        "title": "My updated task",
//            "status": true
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id)
    {
        try {
            Long deletedId = taskService.deleteTask(id);
            if(deletedId == id)
            return ResponseEntity.ok("deleted successfully");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("delete failed");
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable Long id)
    {
        try{
            taskService.changeStatus(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("change failed");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTask(@RequestParam String title) {
        List<Task> tasks = taskService.searchTask(title);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}

