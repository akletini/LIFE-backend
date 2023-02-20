package akletini.life.chore.service;

import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ChoreServiceImpl implements ChoreService {
    @Autowired
    private ChoreRepository choreRepository;

    @Override
    public Page<Chore> getChores(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return choreRepository.findAll(pageRequest);
    }

    @Override
    public Chore store(Chore chore) {

        return null;
    }

    private void computeDueDate(Chore chore) {
        Interval interval = chore.getInterval();
        int intervalValue = interval.getValue();
        Date date = DateUtils.dateStringToDate(chore.getStartDate());
        switch (interval.getUnit()) {
            case DAYS -> DateUtils.addDays(date, intervalValue);
            case WEEKS -> DateUtils.addWeeks(date, intervalValue);
            case MONTHS -> DateUtils.addMonths(date, intervalValue);
        }
        chore.setDueAt(DateUtils.dateToString(date));
    }
}
