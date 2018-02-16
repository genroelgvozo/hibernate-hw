package ru.genro.hibernate_hw.summaries;

import org.hibernate.SessionFactory;
import ru.genro.hibernate_hw.hibernateBase.TransactionMethods;
import ru.genro.hibernate_hw.users.User;

import java.util.Optional;
import java.util.Set;


import static java.util.Objects.requireNonNull;

public class SummaryService extends TransactionMethods {

    private final SummaryDAO summaryDAO;

    public SummaryService(SessionFactory sessionFactory, SummaryDAO summaryDAO){
        this.sessionFactory = requireNonNull(sessionFactory);
        this.summaryDAO = requireNonNull(summaryDAO);
    }

    public void save(Summary summary) { inTransaction(() -> summaryDAO.save(summary)); }

    public Optional<Summary> get(int summaryId) { return inTransaction(() -> summaryDAO.get(summaryId));}

    public Set<Summary> getExperienced(int experience) { return inTransaction(()-> summaryDAO.getExperienced(experience));}

    public Set<Summary> getExperiencedByUser(User user, int experience) { return inTransaction(()-> summaryDAO.getExperiencedByUser(user,experience));}

    public void update(Summary summary) { inTransaction(()->summaryDAO.update(summary));}

    public void delete(int summaryId) { inTransaction(() -> summaryDAO.delete(summaryId));}


}
