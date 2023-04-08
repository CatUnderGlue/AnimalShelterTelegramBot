package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
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
        if (!optionalReport.isPresent()){
            return null;
        }
        return optionalReport.get();
    }

    @Override
    public Report getByDateAndTrialId(LocalDate date, Long id) {
        Optional<Report> optionalReport = reportRepo.findByReceiveDateAndTrialPeriodId(date, id);
        if (!optionalReport.isPresent()){
            return null;
        }
        return optionalReport.get();
    }

    @Override
    public List<Report> getAll() {
        return reportRepo.findAll();
    }

    @Override
    public List<Report> gelAllByTrialPeriodId(Long id) {
        return reportRepo.findAllByTrialPeriodId(id);
    }

    @Override
    public void delete(Report report) {
        reportRepo.delete(report);
    }

    @Override
    public void deleteById(Long id) {
        reportRepo.deleteById(id);
    }
}
