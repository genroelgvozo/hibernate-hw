package ru.genro.hibernate_hw.users;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.genro.hibernate_hw.hibernateBase.TransactionMethods;
import ru.genro.hibernate_hw.summaries.Summary;
import ru.genro.hibernate_hw.summaries.SummaryService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class UserService extends TransactionMethods{

    private final UserDAO userDAO;
    private final SummaryService summaryService;

    public UserService(SessionFactory sessionFactory, UserDAO userDAO, SummaryService summaryService) {
        this.sessionFactory = requireNonNull(sessionFactory);
        this.userDAO = requireNonNull(userDAO);
        this.summaryService = requireNonNull(summaryService);
    }

    public void save(User user) {
        inTransaction(() -> userDAO.save(user));
    }

    public Optional<User> get(int userId) {
        return inTransaction(() -> userDAO.get(userId));
    }

    public Set<User> getAll() {
        return inTransaction(userDAO::getAll);
    }


    public void update(User user) {
        inTransaction(() -> userDAO.update(user));
    }

    public void changeFirstName(int userId, String firstName) {
        inTransaction(() -> {
            Optional<User> optionalUser = userDAO.get(userId);
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("there is no user with id " + userId);
            }
            optionalUser.get().setFirstName(firstName);
            // there is no need to merge: hibernate detects changes and updates on commit
            // there is possibility of deadlock if two transactions get one user and then try to update it
            // to avoid it we can 'select for update' in userDAO.get above
            // also we can implement UserDAO.setFirstName(int userId, String firstName) that does 1 query instead of 2 (get, update on commit)
        });
    }

    public void delete(int userId) {
        inTransaction(() -> {
            Set<Summary> summaries = userDAO.getSummaries(userId);
            for (Summary summary : summaries) {
                summaryService.delete(summary.getId());
            }
            userDAO.delete(userId);

        });
    }

    public void addSummary(User user, Summary summary) {
        inTransaction(()-> {
            user.addSummary(summary);
            summaryService.save(summary);
        });
    }

    public void deleteSummary(User user, Summary summary) {
        inTransaction(()-> {
            user.removeSummary(summary);
            summaryService.delete(summary.getId());
        });
    }

    public Set<Summary> getSummariesByExpirience(int userId, int requiredExperience){
        return inTransaction(() -> userDAO.getSummaries(userId).stream().filter((s) -> s.getExperience() > requiredExperience ).collect(Collectors.toSet()));
    }


}
