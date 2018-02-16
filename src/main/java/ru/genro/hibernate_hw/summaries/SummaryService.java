package ru.genro.hibernate_hw.summaries;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.genro.hibernate_hw.summaries.SummaryDAO;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SummaryService {

    private final SessionFactory sessionFactory;
    private final SummaryDAO summaryDAO;

    public SummaryService(SessionFactory sessionFactory, SummaryDAO summaryDAO){
        this.sessionFactory = requireNonNull(sessionFactory);
        this.summaryDAO = requireNonNull(summaryDAO);
    }

    public void save(Summary summary) { inTransaction(() -> summaryDAO.save(summary)); }

    public Optional<Summary> get(int summaryId) { return inTransaction(() -> summaryDAO.get(summaryId));}

    public void update(Summary summary) { inTransaction(()->summaryDAO.update(summary));}

    public void delete(int summaryId) { inTransaction(() -> summaryDAO.delete(summaryId));}

    private <T> T inTransaction(Supplier<T> supplier) {
        Optional<Transaction> transaction = beginTransaction();
        try {
            T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        } catch (RuntimeException e) {
            transaction.ifPresent(Transaction::rollback);
            throw e;
        }
    }

    private void inTransaction(Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    private Optional<Transaction> beginTransaction() {
        Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }
}
