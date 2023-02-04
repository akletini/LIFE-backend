package akletini.life.todo.structure;

import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;

import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;

public class TestTodos {

    public static Todo getStandardTodo() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt("01.02.2023 14:00:00");
        todo.setDueAt("02.02.2023");
        todo.setAssignedUser(getDefaultCredentialUser());
        return todo;
    }

    public static Todo getTodoWithTag() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt("01.02.2023 14:00:00");
        todo.setDueAt("02.02.2023");
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
