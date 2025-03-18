package com.nurmatov.tasktracker.dto;

public class TaskRequest {
    private String name;
    private String description;

    public TaskRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
