package tech.gauthier.task;

import java.util.List;

import lombok.Data;

@Data
public class GreetUsersInput {
    private List<String> userNames;
}
