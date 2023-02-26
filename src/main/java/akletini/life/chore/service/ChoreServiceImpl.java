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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static akletini.life.shared.constants.FilterConstants.ACTIVE;
import static akletini.life.shared.constants.FilterConstants.DUE;

@Service
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
                    filters.contains(DUE) ? new Date() : null);
        }
        return choreRepository.findAll(pageRequest);
    }

    @Override
    public Chore completeChore(Chore chore) {
        validate(chore);
        if (chore.isShiftInterval()) {
            chore.setDueAt(computeDueDate(new Date(), chore.getInterval()));
        } else {
            chore.setDueAt(computeDueDate(chore.getDueAt(), chore.getInterval()));
        }
        chore = choreRepository.save(chore);
        return chore;
    }

    @Override
    public boolean validate(Chore chore) {
        List<ValidationRule<Chore>> validationRules = validation.getValidationRules();
        validationRules.forEach(rule -> {
            Optional<String> error = rule.validate(chore);
            if (error.isPresent()) {
                throw new ChoreStoreException(error.get());
            }
        });
        return true;
    }

    @Override
    public Chore store(Chore chore) {
        validate(chore);
        Date dueDate = computeDueDate(chore.getStartDate(), chore.getInterval());
        chore.setDueAt(dueDate);
        return choreRepository.save(chore);
    }

    @Override
    public Chore getById(Long id) {
        return choreRepository.findById(id).orElseThrow(() -> new ChoreNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND, Chore.class.getSimpleName(), id)));
    }

    @Override
    public void delete(Chore chore) {
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

    private static Date addInterval(Interval interval, int intervalValue, Date date) {
        switch (interval.getUnit()) {
            case DAYS -> date = DateUtils.addDays(date, intervalValue);
            case WEEKS -> date = DateUtils.addWeeks(date, intervalValue);
            case MONTHS -> date = DateUtils.addMonths(date, intervalValue);
        }
        return date;
    }
}
