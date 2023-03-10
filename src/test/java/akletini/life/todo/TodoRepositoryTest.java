package akletini.life.todo;

import akletini.life.todo.repository.api.TagRepository;
import akletini.life.todo.repository.api.TodoRepository;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.todo.structure.TestTodos;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static akletini.life.todo.repository.entity.Todo.State.DONE;
import static akletini.life.todo.repository.entity.Todo.State.OPEN;
import static akletini.life.todo.structure.TestTodos.getStandardTodo;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TagRepository tagRepository;

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
    public void findAllTodosWithEmptyFiltersAndTags() {
        // GIVEN
        createTestTodos(5, 3);

        // WHEN
        Page<Todo> filtered = todoRepository.findFiltered(PageRequest.
                of(0, 10), OPEN, null, null, null, user.getId());
        // THEN
        assertEquals(8, filtered.toList().size());
    }

    @Test
    public void findAllTodosWithTagFilter() {
        // GIVEN
        List<Tag> testTags = createTestTags(2);
        List<Todo> todos = createTestTodos(4, 5);

        // WHEN
        for (Todo todo : todos) {
            if (todo.getTag() == null) {
                todo.setTag(testTags.get(0));
                todoRepository.save(todo);
            }
        }
        Page<Todo> filtered = todoRepository.findFiltered(PageRequest.
                of(0, 10), OPEN, null, null, List.of(testTags.get(0).getId(), tag.getId()),
                user.getId());
        // THEN
        assertEquals(9, filtered.toList().size());
    }

    @Test
    public void findAllTodosWithTagFilterAndDone() {
        // GIVEN
        List<Tag> testTags = createTestTags(2);
        List<Todo> todos = createTestTodos(4, 5);

        // WHEN
        int doneCount = 2;
        for (Todo todo : todos) {
            if (doneCount > 0) {
                todo.setState(DONE);
                doneCount--;
            }

            if (todo.getTag() == null) {
                todo.setTag(testTags.get(0));
                todoRepository.save(todo);
            }
        }
        Page<Todo> filtered = todoRepository.findFiltered(PageRequest.
                of(0, 10), OPEN, null, null, List.of(testTags.get(0).getId(), tag.getId()), user.getId());
        // THEN
        assertEquals(7, filtered.toList().size());
    }

    private List<Todo> createTestTodos(int simple, int withTag) {
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < simple; i++) {
            Todo todo = getStandardTodo();
            todo.setAssignedUser(user);
            todos.add(todoRepository.save(todo));
        }
        for (int i = 0; i < withTag; i++) {
            Todo todo = getStandardTodo();
            todo.setAssignedUser(user);
            todo.setTag(tag);
            todos.add(todoRepository.save(todo));
        }
        return todos;
    }

    private List<Tag> createTestTags(int count) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setColor("#aaaaaa");
            tag.setName("" + i);
            tags.add(tagRepository.save(tag));
        }
        return tags;
    }
}
