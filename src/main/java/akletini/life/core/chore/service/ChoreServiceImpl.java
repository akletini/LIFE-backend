package akletini.life.core.chore.service;


import akletini.life.core.chore.repository.api.ChoreRepository;
import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.chore.repository.entity.Interval;
import akletini.life.core.shared.constants.FilterConstants;
import akletini.life.core.shared.utils.DateUtils;
import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class ChoreServiceImpl implements ChoreService {
    private ChoreRepository choreRepository;

    private EntityValidation<Chore> validation;

    @Override
    public Page<Chore> getChores(int page, int pageSize, Optional<String> sortBy,
                                 Optional<List<String>> filterBy) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (sortBy.isPresent()) {
            pageRequest = PageRequest.of(page, pageSize, Sort.by(sortBy.get()));
        }
        if (filterBy.isPresent() && !filterBy.get().isEmpty()) {
            List<String> filters = filterBy.get();
            return choreRepository.findFiltered(pageRequest,
                    filters.contains(FilterConstants.ACTIVE),
                    filters.contains(FilterConstants.DUE) ? LocalDate.now() : null);
        }
        return choreRepository.findAll(pageRequest);
    }

    @Override
    public Chore completeChore(Chore chore) {
        validate(chore);
        if (chore.isShiftInterval()) {
            chore.setDueAt(DateUtils.dateToLocalDate(computeDueDate(chore.getId(), new Date(),
                    chore.getInterval())));
        } else {
            chore.setDueAt(DateUtils.dateToLocalDate(computeDueDate(chore.getId(),
                    DateUtils.localDateToDate(chore.getDueAt()), chore.getInterval())));
        }
        chore.setLastCompleted(LocalDate.now());
        chore = choreRepository.save(chore);
        return chore;
    }

    @Override
    public boolean validate(Chore chore) {
        List<ValidationRule<Chore>> validationRules = validation.getValidationRules();
        validationRules.forEach(rule -> {
            Optional<String> error = rule.validate(chore);
            if (error.isPresent()) {
                InvalidDataException exception = new InvalidDataException(error.get());
                log.error(exception);
                throw exception;
            }
        });
        return true;
    }

    @Override
    public Chore store(Chore chore) {
        validate(chore);
        Date dueDate = chore.getDueAt() != null ? computeDueDate(chore.getId(),
                DateUtils.localDateToDate(chore.getStartDate()), chore.getInterval()) :
                DateUtils.localDateToDate(chore.getStartDate());
        chore.setDueAt(DateUtils.dateToLocalDate(dueDate));
        return choreRepository.save(chore);
    }

    @Override
    public Chore getById(Long id) {
        log.info("Searching chore with id {}", id);
        return choreRepository.findById(id).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            Chore.class.getSimpleName(), id));
            log.error(exception);
            return exception;
        });
    }

    @Override
    public void delete(Chore chore) {
        log.info("Deleting chore with id {}", chore.getId());
        choreRepository.delete(chore);
    }

    private Date computeDueDate(Long choreId, Date startDate, Interval interval) {
        if (intervalUnchanged(choreId, interval)) {
            return startDate;
        }

        int intervalValue = interval.getValue();
        Date date = startDate;
        date = DateUtils.addInterval(interval, intervalValue, date);
        while (DateUtils.truncatedCompareTo(date, new Date(), Calendar.DATE) < 0) {
            date = DateUtils.addInterval(interval, intervalValue, date);
        }
        return date;
    }

    private boolean intervalUnchanged(Long choreId, Interval interval) {
        if (choreId == null) {
            return false;
        }
        Optional<Chore> choreOptional = choreRepository.findById(choreId);
        if (choreOptional.isPresent()) {
            Chore chore = choreOptional.get();
            return Objects.equals(interval, chore.getInterval());
        }
        return false;
    }

}
