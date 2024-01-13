package com.nurmatov.tasktracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nurmatov.tasktracker.entity.Task;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStateDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @JsonProperty("right_task_state_id")
    private Long rightTaskStateId;
    @JsonProperty("left_task_state_id")
    private Long leftTaskStateId;

    private Instant createdAt;


    private List<TaskDto> tasks;
}
