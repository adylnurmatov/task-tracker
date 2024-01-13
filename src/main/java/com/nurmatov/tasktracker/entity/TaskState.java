package com.nurmatov.tasktracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "task_state")
public class TaskState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private TaskState leftTaskState;

    @OneToOne
    private TaskState rightTaskState;



    @Column(name = "name")
    private String name;

    @ManyToOne
    private Project project;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();


    @Builder.Default
    @OneToMany
    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    private List<Task> tasks = new ArrayList<>();




    public Optional<TaskState> getLeftTaskState(){
        return Optional.ofNullable(leftTaskState);
    }



    public Optional<TaskState> getRightTaskState(){
        return Optional.ofNullable(rightTaskState);
    }
}
