package ru.genro.hibernate_hw.summaries;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class SummaryDAO {

    private final SessionFactory sessionFactory;

    public SummaryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    public void save(Summary summary) {
        if(summary.getId() != null) {
            throw new IllegalArgumentException("Cannot save " + summary + " with assigned id");
        }
        session().save(summary);
    }

    public Optional<Summary> get(int summaryId) {
        Summary summary = (Summary) session().get(Summary.class, summaryId);

        return Optional.ofNullable(summary);
    }

    public void update(Summary summary) {
        session().update(summary);
    }

    public void delete(int summaryId) {
        session().createQuery("DELETE Summary WHERE id = :id")
                .setInteger("id",summaryId)
                .executeUpdate();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
        // or sessionFactory.openSession(), but do not forget to close it
        // try-with-resource won't work because Session does not implement Autocloseable
    }
}
