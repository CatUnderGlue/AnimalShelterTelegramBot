package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.AlreadyExistsException;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.ReportRepo;
import ru.codehunters.zaepestelegrambot.service.ReportService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepo reportRepo;
    private final TrialPeriodService trialPeriodService;
    private final Pattern pattern = Pattern.compile("(Рацион:)(\\s\\W+;)\\n(Самочувствие:)(\\s\\W+;)\\n(Поведение:)(\\s\\W+;)");

    @Override
    public Report create(Report report) {
        return reportRepo.save(report);
    }

    @Override
    public Report getById(Long id) {
        Optional<Report> optionalReport = reportRepo.findById(id);
        if (optionalReport.isEmpty()){
            throw new NotFoundException("Отчёт не найден!");
        }
        return optionalReport.get();
    }

    @Override
    public Report getByDateAndTrialId(LocalDate date, Long id) {
        Optional<Report> optionalReport = reportRepo.findByReceiveDateAndTrialPeriodId(date, id);
        if (optionalReport.isEmpty()){
            throw new NotFoundException("Отчёт не найден!");
        }
        return optionalReport.get();
    }

    @Override
    public List<Report> getAll() {
        List<Report> all = reportRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Отчёты не найдены!");
        }
        return all;
    }

    @Override
    public List<Report> gelAllByTrialPeriodId(Long id) {
        List<Report> allByTrialPeriodId = reportRepo.findAllByTrialPeriodId(id);
        if (allByTrialPeriodId.isEmpty()) {
            throw new NotFoundException("Отчёты не найдены!");
        }
        return allByTrialPeriodId;
    }

    @Override
    public Report update(Report report) {
        Report currentReport = getById(report.getId());
        EntityUtils.copyNonNullFields(report, currentReport);
        return reportRepo.save(currentReport);
    }

    @Override
    public void delete(Report report) {
        reportRepo.delete(getById(report.getId()));
    }

    @Override
    public void deleteById(Long id) {
        reportRepo.deleteById(getById(id).getId());
    }

    @Override
    public void createFromTelegram(String photoId, String caption, Long id) {
        TrialPeriod trialPeriod = trialPeriodService.getAllByOwnerId(id).stream()
                .filter(trialPeriod1 -> trialPeriod1.getResult().equals(TrialPeriod.Result.IN_PROGRESS))
                .findFirst().get();
        if (trialPeriod.getLastReportDate().isEqual(LocalDate.now())) {
            throw new AlreadyExistsException("Вы уже отправляли отчёт сегодня.");
        }
        List<String> captionParts = splitCaption(caption);
        create(new Report(photoId, captionParts.get(0), captionParts.get(1), captionParts.get(2), LocalDate.now(), trialPeriod.getId()));
        trialPeriod.setLastReportDate(LocalDate.now());
        trialPeriodService.update(trialPeriod);
    }

    /**
     * Метод разбивающий описание фотографии на части для создания отчёта
     *
     * @param caption Описание под фотографией
     * @return Список с частями для создания отчёта
     */
    private List<String> splitCaption(String caption) {
        if (caption == null || caption.isBlank()) {
            throw new IllegalArgumentException("Описание под фотографией не должно быть пустым. Отправьте отчёт заново");
        }
        Matcher matcher = pattern.matcher(caption);
        if (matcher.find()) {
            return new ArrayList<>(List.of(matcher.group(2), matcher.group(4), matcher.group(6)));
        } else {
            throw new IllegalArgumentException("Проверьте правильность введённых данных и отправьте отчёт ещё раз.");
        }
    }
}
