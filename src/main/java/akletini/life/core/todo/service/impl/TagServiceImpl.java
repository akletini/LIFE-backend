package akletini.life.core.todo.service.impl;

import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.todo.repository.api.TagRepository;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class TagServiceImpl extends TagService {

    private TagRepository tagRepository;


    @Override
    public Tag getByName(String name) throws EntityNotFoundException {
        Optional<Tag> tagByName = tagRepository.findByName(name);
        return tagByName.orElseThrow(() -> new EntityNotFoundException("Could not find Tag with " +
                "name " + name));
    }

}
