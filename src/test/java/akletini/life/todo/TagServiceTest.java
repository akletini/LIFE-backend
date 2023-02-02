package akletini.life.todo;

import akletini.life.todo.exception.custom.TagStoreException;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.service.api.TagService;
import akletini.life.todo.structure.TestTodos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TagServiceTest {

    @Autowired
    TagService tagService;

    @Test
    public void changeNameFailed() {
        // Given
        Tag tag = TestTodos.uniTag;
        tag = tagService.store(tag);

        // When
        Tag newTag = new Tag();
        newTag.setName(tag.getName());
        newTag.setColor("#ffffff");

        // Then
        assertThrows(TagStoreException.class, () -> tagService.store(newTag));
    }

}
