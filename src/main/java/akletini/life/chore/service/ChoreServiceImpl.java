package akletini.life.chore.service;

import akletini.life.chore.exception.custom.ChoreNotFoundException;
import akletini.life.chore.exception.custom.ChoreStoreException;
import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.chore.validation.ChoreValidation;
import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static akletini.life.shared.constants.FilterConstants.ACTIVE;
import static akletini.life.shared.constants.FilterConstants.DUE;
import static akletini.life.shared.utils.DateUtils.*;
import static akletini.life.shared.validation.Errors.COULD_NOT_STORE;
import static akletini.life.user.ContextUtils.getCurrentUser;

@Service
@Log4j2
public class ChoreServiceImpl implements ChoreService {
    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    ChoreValidation validation;

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
                    filters.contains(ACTIVE),
                    filters.contains(DUE) ? LocalDate.now() : null,
                    getCurrentUser().getId());
        }
        return choreRepository.findAll(pageRequest);
    }

    @Override
    public Chore completeChore(Chore chore) {
        validate(chore);
        if (chore.isShiftInterval()) {
            chore.setDueAt(dateToLocalDate(computeDueDate(new Date(), chore.getInterval())));
        } else {
            chore.setDueAt(dateToLocalDate(computeDueDate(localDateToDate(chore.getDueAt()), chore.getInterval())));
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
                ChoreStoreException exception = new ChoreStoreException(error.get());
                log.error(exception);
                throw exception;
            }
        });
        return true;
    }

    @Override
    public Chore store(Chore chore) {
        if (chore != null) {
            validate(chore);
            Date dueDate = chore.getDueAt() != null ? computeDueDate(localDateToDate(chore.getStartDate()), chore.getInterval()) : localDateToDate(chore.getStartDate());
            chore.setDueAt(dateToLocalDate(dueDate));
            return choreRepository.save(chore);
        }
        ChoreStoreException choreStoreException =
                new ChoreStoreException(Errors.getError(COULD_NOT_STORE,
                        Chore.class.getSimpleName()));
        log.error(choreStoreException);
        throw choreStoreException;
    }

    @Override
    public Chore getById(Long id) {
        log.info("Searching chore with id {}", id);
        return choreRepository.findById(id).orElseThrow(() -> {
            ChoreNotFoundException exception =
                    new ChoreNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
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

    private Date computeDueDate(Date startDate, Interval interval) {
        int intervalValue = interval.getValue();
        Date date = startDate;
        date = addInterval(interval, intervalValue, date);
        while (DateUtils.truncatedCompareTo(date, new Date(), Calendar.DATE) < 0) {
            date = addInterval(interval, intervalValue, date);
        }
        return date;
    }

}
