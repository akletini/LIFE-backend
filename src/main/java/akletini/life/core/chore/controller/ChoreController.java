package akletini.life.core.chore.controller;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.chore.dto.mapper.ChoreMapper;
import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.chore.service.ChoreService;
import akletini.life.core.shared.response.HttpResponse;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.dto.TodoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/chores")
@AllArgsConstructor
public class ChoreController {

    private ChoreService choreService;

    private final ChoreMapper choreMapper;

    @PostMapping("/add")
    public ResponseEntity<ChoreDto> addChore(@RequestBody ChoreDto choreDto) throws InvalidDataException {
        Chore storedChore = choreService.store(choreMapper.dtoToChore(choreDto));
        return ResponseEntity.status(OK).body(choreMapper.choreToDto(storedChore));
    }

    @PutMapping("/complete")
    public ResponseEntity<ChoreDto> completeChore(@RequestBody ChoreDto choreDto) throws InvalidDataException {
        Chore completedChore = choreService.completeChore(choreMapper.dtoToChore(choreDto));
        return ResponseEntity.status(OK).body(choreMapper.choreToDto(completedChore));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<TodoDto> deleteChore(@RequestBody ChoreDto choreDto) {
        Chore chore = choreMapper.dtoToChore(choreDto);
        choreService.delete(chore);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ChoreDto> updateTodo(@RequestBody ChoreDto choreDto) throws EntityNotFoundException, InvalidDataException {
        Chore chore = choreMapper.dtoToChore(choreDto);
        choreService.getById(chore.getId());
        Chore updatedChore = choreService.store(chore);
        return ResponseEntity.status(HttpStatus.OK).body(choreMapper.choreToDto(updatedChore));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ChoreDto> getById(@PathVariable Long id) throws EntityNotFoundException {
        Chore chore = choreService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(choreMapper.choreToDto(chore));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<HttpResponse> getChores(@RequestParam("page") int page,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("sortBy") Optional<String> sortBy,
                                                  @RequestParam Optional<List<String>> filterBy) {
        Page<Chore> chores = choreService.getChores(page, size, sortBy, filterBy);
        Page<ChoreDto> dtoPage = chores.map(choreMapper::choreToDto);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("page", dtoPage))
                        .message(OK.getReasonPhrase())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
