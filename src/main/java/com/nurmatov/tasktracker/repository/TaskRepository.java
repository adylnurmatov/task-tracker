package com.nurmatov.tasktracker.repository;

import com.nurmatov.tasktracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

     List<Task> findAllByTaskStateId(Long taskStateId);
}
