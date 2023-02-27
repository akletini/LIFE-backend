package akletini.life.todo.structure;

import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static akletini.life.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;

public class TestTodos {

    public static Todo getStandardTodo() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        try {
            todo.setCreatedAt(new SimpleDateFormat(DATE_TIME_FORMAT)
                    .parse("01.02.2023 14:00:00"));
            todo.setDueAt(new SimpleDateFormat(DATE_FORMAT).parse("02.02.2023"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        todo.setAssignedUser(getDefaultCredentialUser());
        return todo;
    }

    public static Todo getTodoWithTag() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        try {
            todo.setCreatedAt(new SimpleDateFormat(DATE_TIME_FORMAT)
                    .parse("01.02.2023 14:00:00"));
            todo.setDueAt(new SimpleDateFormat(DATE_FORMAT).parse("02.02.2023"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
