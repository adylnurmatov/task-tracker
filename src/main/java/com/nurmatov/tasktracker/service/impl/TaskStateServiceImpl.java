package com.nurmatov.tasktracker.service.impl;

import com.nurmatov.tasktracker.dto.TaskStateDto;
import com.nurmatov.tasktracker.entity.Project;
import com.nurmatov.tasktracker.entity.TaskState;
import com.nurmatov.tasktracker.exception.BadRequestException;
import com.nurmatov.tasktracker.exception.NotFoundException;
import com.nurmatov.tasktracker.mapper.TaskStateMapper;
import com.nurmatov.tasktracker.repository.TaskStateRepository;
import com.nurmatov.tasktracker.service.TaskStateService;
import com.nurmatov.tasktracker.service.helper.ControllerHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskStateServiceImpl implements TaskStateService {
    private final TaskStateRepository taskStateRepository;
    private final TaskStateMapper taskStateMapper;
    private final ControllerHelper controllerHelper;

    @Override
    public List<TaskStateDto> getTaskStates(Long projectId) {
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        return project.getTaskStates().stream().map(taskStateMapper :: toDto).collect(Collectors.toList());
    }

    @Override
    public TaskStateDto createTaskState(Long projectId, String name) {



        if(name.trim().isEmpty()){
            throw new BadRequestException("cannot be empty");
        }
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        Optional<TaskState> optionalTaskState = Optional.empty();

        for(TaskState taskState: project.getTaskStates()){
            if(taskState.getName().equalsIgnoreCase(name)){
                throw new BadRequestException("task state cannot be the same");
            }
            if(!taskState.getRightTaskState().isPresent()){
                optionalTaskState = Optional.of(taskState);
                break;
            }
        }

        TaskState taskState = taskStateRepository.saveAndFlush(
                TaskState.builder()
                        .name(name)
                        .project(project)
                        .build()
        );


        optionalTaskState
                .ifPresent(anothertaskState -> {
                    taskState.setLeftTaskState(anothertaskState);
                    anothertaskState.setRightTaskState(taskState);
                    taskStateRepository.saveAndFlush(anothertaskState);
                });

        final TaskState savedTaskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateMapper.toDto(savedTaskState);
    }

    @Override
    public TaskStateDto updateTaskStateDto(Long taskStateId, String name) {
        if(name.isBlank()){
            throw new BadRequestException("name cannot be empty");
        }
        TaskState taskState = getTaskStateOrThrowException(taskStateId);
        taskStateRepository
                .findTaskStateByProjectIdAndNameContainsIgnoreCase(taskState.
                        getProject().getId(),
                        name)
                .filter(anotherTaskState -> !anotherTaskState.getId().equals(taskStateId))
                .ifPresent(anotherTaskState ->{
                    throw new BadRequestException("task state is already exist");
                });
        taskState.setName(name);
        taskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateMapper.toDto(taskState);
    }

    @Override
    public void deleteTaskStateDto(Long id) {
        TaskState taskState = getTaskStateOrThrowException(id);
        replaceOldTaskStatePosition(taskState);
        taskStateRepository.delete(taskState);

    }


    private TaskState getTaskStateOrThrowException(Long taskStateId){
        return taskStateRepository
                .findById(taskStateId)
                .orElseThrow(() ->
                        new NotFoundException("doesnt exist"));
    }

    private void replaceOldTaskStatePosition(TaskState changeTaskState) {
        Optional<TaskState> optionalOldLeftTaskState = changeTaskState.getLeftTaskState();
        Optional<TaskState> optionalOldRightTaskState = changeTaskState.getRightTaskState();
        if (optionalOldLeftTaskState.isPresent()) {
            TaskState oldLeftTaskState = optionalOldLeftTaskState.get();
            oldLeftTaskState.setRightTaskState(optionalOldRightTaskState.orElse(null));
            taskStateRepository.saveAndFlush(oldLeftTaskState);
        }

        if (optionalOldRightTaskState.isPresent()) {
            TaskState oldRightTaskState = optionalOldRightTaskState.get();
            oldRightTaskState.setLeftTaskState(optionalOldLeftTaskState.orElse(null));
            taskStateRepository.saveAndFlush(oldRightTaskState);
        }
    }


//    Optional<TaskState> optionalOldLeftTaskState = changeTaskState.getLeftTaskState();
//        Optional<TaskState> optionalOldRightTaskState = changeTaskState.getRightTaskState();
//
//        optionalOldLeftTaskState
//                .ifPresent(it -> {
//
//                    it.setRightTaskState(optionalOldRightTaskState.orElse(null));
//
//                    taskStateRepository.saveAndFlush(it);
//                });
//
//        optionalOldRightTaskState
//                .ifPresent(it -> {
//
//                    it.setLeftTaskState(optionalOldLeftTaskState.orElse(null));
//
//                    taskStateRepository.saveAndFlush(it);
//                });
}
