package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.repository.ReportRepo;
import ru.codehunters.zaepestelegrambot.service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepo reportRepo;

    public ReportServiceImpl(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    @Override
    public Long create(Report report) {
        return reportRepo.save(report).getId();
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
}
