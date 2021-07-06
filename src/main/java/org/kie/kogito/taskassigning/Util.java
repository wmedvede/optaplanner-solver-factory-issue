package org.kie.kogito.taskassigning;

import java.util.Arrays;
import java.util.Collections;

import org.kie.kogito.taskassigning.core.model.Group;
import org.kie.kogito.taskassigning.core.model.Task;
import org.kie.kogito.taskassigning.core.model.TaskAssigningSolution;
import org.kie.kogito.taskassigning.core.model.TaskAssignment;
import org.kie.kogito.taskassigning.core.model.User;

import static org.kie.kogito.taskassigning.core.model.ModelConstants.DUMMY_TASK_ASSIGNMENT;
import static org.kie.kogito.taskassigning.core.model.ModelConstants.PLANNING_USER;

public class Util {

    public static TaskAssigningSolution createSolution() {
        Task task1 = Task.newBuilder()
                .id("Task1")
                .potentialGroups(Collections.singleton("HR"))
                .build();

        Task task2 = Task.newBuilder()
                .id("Task2")
                .potentialGroups(Collections.singleton("HR"))
                .build();

        TaskAssignment taskAssignment1 = new TaskAssignment(task1);
        taskAssignment1.setPinned(false);

        TaskAssignment taskAssignment2 = new TaskAssignment(task2);
        taskAssignment2.setPinned(false);

        User user1 = new User("User1", true);
        user1.getGroups().add(new Group("HR"));
        user1.setNextElement(taskAssignment1);

        taskAssignment1.setUser(user1);
        taskAssignment1.setPreviousElement(user1);
        taskAssignment1.setNextElement(taskAssignment2);
        taskAssignment1.setStartTimeInMinutes(0);
        taskAssignment1.setEndTimeInMinutes(1);

        taskAssignment2.setUser(user1);
        taskAssignment2.setPreviousElement(taskAssignment1);
        taskAssignment2.setStartTimeInMinutes(1);
        taskAssignment2.setEndTimeInMinutes(2);

        return new TaskAssigningSolution("1",
                                         Arrays.asList(PLANNING_USER, user1),
                                         Arrays.asList(taskAssignment1, taskAssignment2, DUMMY_TASK_ASSIGNMENT));
    }
}
