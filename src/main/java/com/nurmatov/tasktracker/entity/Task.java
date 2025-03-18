package com.nurmatov.tasktracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    private String desctiption;
    @ManyToOne
    private TaskState taskState;

}
