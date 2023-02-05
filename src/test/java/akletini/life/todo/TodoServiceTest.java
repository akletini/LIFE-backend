package akletini.life.todo;

import akletini.life.todo.exception.custom.TodoStoreException;
import akletini.life.todo.repository.api.TagRepository;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import akletini.life.todo.structure.TestTodos;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static akletini.life.todo.structure.TestTodos.getStandardTodo;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoServiceTest {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TodoService todoService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private TodoRepository todoRepository;

    User user;
    Tag tag;

    @BeforeEach
    public void initDB() {
        user = getDefaultCredentialUser();
        user = userRepository.save(user);

        tag = TestTodos.getUniTag();
        tag = tagRepository.save(tag);
    }

    @Test
    public void removeUsedTag() {
        // Given
        Todo todo = getStandardTodo();
        todo.setTag(tag);
        todo.setAssignedUser(user);
        Todo storedTodo = todoService.store(todo);

        // When
        tagRepository.deleteById(tag.getId());
        storedTodo.setTag(null);
        Todo byId = todoService.getById(todo.getId());

        // Then
        assertEquals(storedTodo, byId);
        assertEquals(Collections.emptyList(), tagRepository.findAll());
    }

    @Test
    public void modifyCreatedDate() {
        // Given
        Todo todo = getStandardTodo();
        todo.setAssignedUser(user);
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
        Todo todo = getStandardTodo();
        todo.setAssignedUser(user);

        // When
        todo.setCreatedAt(todo.getDueAt());

        // Then
        assertThrows(TodoStoreException.class, () -> todoService.store(todo));
    }

    @Test
    public void dueAtFormatCorrect() {
        // Given
        Todo todo = getStandardTodo();
        todo.setAssignedUser(user);
        todo.setCreatedAt(todo.getDueAt());

        // When
        todo.setDueAt(todo.getCreatedAt());

        // Then
        assertThrows(TodoStoreException.class, () -> todoService.store(todo));
    }

    @Test
    public void deleteTodoWithExistingUser() {
        // Given
        Todo todo = getStandardTodo();
        todo.setAssignedUser(user);
        todoRepository.save(todo);

        // When
        todoService.delete(todo);

        // Then
        Optional<Todo> todoById = todoRepository.findById(todo.getId());
        assertFalse(todoById.isPresent());
        Optional<User> userById = userRepository.findById(todo.getAssignedUser().getId());
        assertTrue(userById.isPresent());
    }

    @Test
    public void updateUserAuthInTodo() {
        // Given
        Todo todo = getStandardTodo();
        todo.setAssignedUser(user);
        todoRepository.save(todo);

        // When
        todo.getAssignedUser().getTokenContainer().setAccessToken("A different token");
        Todo storedTodo = todoService.store(todo);
        Optional<User> userByID = userRepository.findById(todo.getAssignedUser().getId());

        // Then
        assertTrue(userByID.isPresent());
        User user = userByID.get();
        assertEquals(storedTodo.getAssignedUser().getTokenContainer().getAccessToken(), user.getTokenContainer().getAccessToken());
    }

}
