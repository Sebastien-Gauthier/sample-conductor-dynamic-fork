package tech.gauthier.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.common.metadata.tasks.TaskResult.Status;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class GreetUserWorker {
    private static final String TASK_NAME = "greetUser";
    private static final String PREPARATION_TASK_NAME = "prepareGreetUsersDynamicFork";

    @PostConstruct
    public void initialize() {
        log.info("Initialized {}, {} worker.", PREPARATION_TASK_NAME, TASK_NAME);
    }

    // This task prepares the generation of a dynamic number of tasks by Conductor
    // It generates a task description and a task input for as many tasks as there are user names provided
    @WorkerTask(value = PREPARATION_TASK_NAME, threadCount = 5)
    public TaskResult prepareGreetUserDynamicFork(GreetUsersInput input) {
        log.info("{} task triggered with input {}", PREPARATION_TASK_NAME, input);

        // Generate tasks descriptions
        List<Map<String, Object>> dynamicTasks = input.getUserNames().stream().map(userName -> {
            Map<String, Object> taskDesc = new HashMap<>();
            taskDesc.put("name", TASK_NAME);
            taskDesc.put("taskReferenceName", getTaskReferenceName(userName));
            taskDesc.put("type", "SIMPLE");
            taskDesc.put("inputKeys", List.of("name"));
            taskDesc.put("outputKeys", Collections.emptyList());
            taskDesc.put("retryCount", 1);
            taskDesc.put("responseTimeoutSeconds", 30);
            return taskDesc;
        }).collect(Collectors.toList());

        // Generate tasks input
        Map<String, Map<String, Object>> dynamicTasksInput = input.getUserNames().stream().map(userName -> {
            Map<String, Object> taskInput = new HashMap<>();
            taskInput.put("name", userName);
            return Map.entry(getTaskReferenceName(userName), taskInput);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return TaskResult.newTaskResult(Status.COMPLETED)
            .addOutputData("dynamicTasks", dynamicTasks)
            .addOutputData("dynamicTasksInput", dynamicTasksInput);
    }

    private String getTaskReferenceName(String userName) {
        return TASK_NAME+"-"+userName;
    }


    // This task generates a greeting for a single user 
    @WorkerTask(value = TASK_NAME, threadCount = 5)
    public TaskResult greetuser(GreetUserInput input) {
        log.info("{} task triggered with input {}", TASK_NAME, input);
        String greeting = generateGreeting(input.getUserName());
        return TaskResult.newTaskResult(Status.COMPLETED)
                .addOutputData("greeting", greeting);
    }

    private String generateGreeting(String userName) {
        return "Hello " + userName + "!";
    }
}
