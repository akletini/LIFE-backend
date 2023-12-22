package akletini.life.core.todo.structure;

import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.repository.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static akletini.life.core.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT;
import static akletini.life.core.user.structure.TestUsers.getDefaultCredentialUser;

public class TestTodos {

    public static Todo getStandardTodo() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt(LocalDateTime.parse("01.02.2023 14:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        todo.setDueAt(LocalDate.parse("02.02.2023", DateTimeFormatter.ofPattern(DATE_FORMAT)));
        todo.setAssignedUser(getDefaultCredentialUser());
        return todo;
    }

    public static Todo getTodoWithTag() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt(LocalDateTime.parse("01.02.2023 14:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        todo.setDueAt(LocalDate.parse("02.02.2023", DateTimeFormatter.ofPattern(DATE_FORMAT)));
        todo.setTag(getUniTag());
        return todo;
    }

    public static Tag getUniTag() {
        Tag tag = new Tag();
        tag.setName("Uni");
        tag.setColor("#ff0000");
        return tag;
    }
}
