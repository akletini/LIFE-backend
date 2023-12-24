package akletini.life.core.todo;

import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import akletini.life.core.todo.structure.TestTodos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TagServiceTest {

    @Autowired
    TagService tagService;

    @Test
    public void changeNameFailed() throws EntityNotFoundException, InvalidDataException {
        // Given
        Tag tag = TestTodos.getUniTag();
        tag = tagService.store(tag);

        // When
        Tag newTag = new Tag();
        newTag.setName(tag.getName());
        newTag.setColor("#ffffff");

        // Then
        assertThrows(InvalidDataException.class, () -> tagService.store(newTag));
        assertEquals(tag.getName(), tagService.getById(tag.getId()).getName());
    }

}
