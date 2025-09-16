package com.practice.TodoApp.Repository;

import com.practice.TodoApp.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
      @Query("SELECT t FROM Task t WHERE lower(t.title) LIKE lower(CONCAT('%', :title, '%'))")
      List<Task> searchTaskByTitle(@Param("title") String title);
}
