package akletini.life.todo;

import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TagService;
import akletini.life.todo.service.api.TodoService;
import akletini.life.todo.structure.TestTodos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    TodoService todoService;

    @Test
    public void removeUsedTag() {
        // Given
        Todo todo = TestTodos.standardTodo;
        Tag tag = TestTodos.uniTag;
        tag = tagService.store(tag);
        todo.setTag(tag);
        Todo storedTodo = todoService.store(todo);

        // When
        tagService.delete(tag.getId());
        storedTodo.setTag(null);
        Todo byId = todoService.getById(todo.getId());

        // Then
        assertEquals(storedTodo, byId);
        assertEquals(Collections.emptyList(), tagService.getAll());
    }

}
