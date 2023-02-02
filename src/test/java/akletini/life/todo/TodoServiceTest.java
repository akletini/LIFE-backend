package akletini.life.todo;

import akletini.life.todo.exception.custom.TodoStoreException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Todo todo = TestTodos.getStandardTodo();
        Tag tag = TestTodos.getUniTag();
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

    @Test
    public void modifyCreatedDate() {
        // Given
        Todo todo = TestTodos.getStandardTodo();
        String initialCreationDate = todo.getCreatedAt();
        todo = todoService.store(todo);

        // When
        todo.setCreatedAt("03.02.2023 14:00:00");

        // Then
        Todo finalTodo = todo;
        assertThrows(TodoStoreException.class, () -> todoService.store(finalTodo));
        assertEquals(initialCreationDate, todoService.getById(todo.getId()).getCreatedAt());
    }

    @Test
    public void createdAtFormatCorrect() {
        // Given
        Todo todo = TestTodos.getStandardTodo();

        // When
        todo.setCreatedAt(todo.getDueAt());

        // Then
        assertThrows(TodoStoreException.class, () -> todoService.store(todo));
    }

    @Test
    public void dueAtFormatCorrect() {
        // Given
        Todo todo = TestTodos.getStandardTodo();
        todo.setCreatedAt(todo.getDueAt());

        // When
        todo.setDueAt(todo.getCreatedAt());

        // Then
        assertThrows(TodoStoreException.class, () -> todoService.store(todo));
    }

}
