package akletini.life.todo;

import akletini.life.chore.TestChores;
import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.todo.exception.TodoStoreException;
import akletini.life.todo.repository.api.TagRepository;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.service.api.TodoService;
import akletini.life.todo.structure.TestTodos;
import akletini.life.user.ContextUtils;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static akletini.life.shared.utils.DateUtils.dateToLocalDateTime;
import static akletini.life.shared.utils.DateUtils.localDateTimeToDate;
import static akletini.life.shared.validation.Errors.TASK.CREATED_DATE_UNCHANGED;
import static akletini.life.todo.structure.TestTodos.getStandardTodo;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;
import static org.apache.commons.lang3.time.DateUtils.addDays;
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
    ChoreRepository choreRepository;
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
        try (MockedStatic<ContextUtils> utils = Mockito.mockStatic(ContextUtils.class)) {
            utils.when(ContextUtils::getCurrentUser).thenReturn(user);
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
    }

    @Test
    public void modifyCreatedDate() {
        try (MockedStatic<ContextUtils> utils = Mockito.mockStatic(ContextUtils.class)) {
            utils.when(ContextUtils::getCurrentUser).thenReturn(user);
            // Given
            Chore newChore = TestChores.getNewChore();
            newChore.setAssignedUser(user);
            choreRepository.save(newChore);
            Todo todo = getStandardTodo();
            todo.setAssignedUser(user);
            LocalDateTime initialCreationDate = todo.getCreatedAt();
            todo = todoService.store(todo);

            // When
            todo.setCreatedAt(dateToLocalDateTime(addDays(localDateTimeToDate(todo.getCreatedAt()), 3)));

            // Then
            Todo finalTodo = todo;
            TodoStoreException todoStoreException = assertThrows(TodoStoreException.class,
                    () -> todoService.store(finalTodo));
            assertEquals(todoStoreException.getMessage(), Errors.getError(CREATED_DATE_UNCHANGED));
            assertTrue(DateUtils.isSameInstant(localDateTimeToDate(initialCreationDate),
                    localDateTimeToDate(todoService.getById(todo.getId()).getCreatedAt())));
        }
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
        try (MockedStatic<ContextUtils> utils = Mockito.mockStatic(ContextUtils.class)) {
            utils.when(ContextUtils::getCurrentUser).thenReturn(user);
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

}
