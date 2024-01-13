package com.nurmatov.tasktracker.repository;

import com.nurmatov.tasktracker.entity.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskStateRepository extends JpaRepository<TaskState, Long> {

    Optional<TaskState> findTaskStateByProjectIdAndNameContainsIgnoreCase(Long projectId, String taskStateName);
}
