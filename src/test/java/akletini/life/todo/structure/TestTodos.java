package akletini.life.todo.structure;

import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;

public class TestTodos {

    public static Tag uniTag = getUniTag();
    public static Todo emptyTodo = new Todo();
    public static Todo standardTodo = getStandardTodo();
    public static Todo todoWithTag = getTodoWithTag();

    private static Todo getStandardTodo() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt("01.02.2023 14:00:00");
        todo.setDueAt("02.02.2023");
        return todo;
    }

    private static Todo getTodoWithTag() {
        Todo todo = new Todo();
        todo.setTitle("TestTodo");
        todo.setDescription("This is a description");
        todo.setState(Todo.State.OPEN);
        todo.setCreatedAt("01.02.2023 14:00:00");
        todo.setDueAt("02.02.2023");
        todo.setTag(uniTag);
        return todo;
    }

    private static Tag getUniTag() {
        Tag tag = new Tag();
        tag.setName("Uni");
        tag.setColor("#ff0000");
        return tag;
    }
}
