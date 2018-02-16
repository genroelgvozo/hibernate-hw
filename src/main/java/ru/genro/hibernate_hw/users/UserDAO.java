package ru.genro.hibernate_hw.users;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.genro.hibernate_hw.summaries.Summary;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    public void save(User user) {

        // session.save inserts new user with different id even if user already has id
        // this is confusing, we'd better throw exception, unfortunately at runtime only
        if (user.id() != null) {
            throw new IllegalArgumentException("can not save " + user + " with assigned id");
        }
        session().save(user); // see also saveOrUpdate and persist
    }

    public Optional<User> get(int userId) {
        User user = (User) session().get(User.class, userId);
        // or session.load, load throws exception if entity with such id not found
        return Optional.ofNullable(user);
    }

    public Set<User> getAll() {
        Criteria criteria = session().createCriteria(User.class); // Criteria query
        // or session().createQuery("FROM User"); // HQL query

        List<User> users = criteria.list();
        return new HashSet<>(users);
    }

    public Set<Summary> getSummaries(int userId) {
        return get(userId).get().getSummaries();
    }

    public void update(User user) {
        session().update(user);
        // session.update throws exception if current session already has User with same id
        // session.merge does not throw exception
    }

    public void delete(int userId) {
        session().createQuery("DELETE User WHERE id = :id") // HQL
                .setInteger("id", userId)
                .executeUpdate();
        // see also session().delete(user);
        // but first you will need to get this user from DB
        // or create fake new User(userId)
        // also be aware that hibernate silently ignores the fact that user may not have id, this most likely an error
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
        // or sessionFactory.openSession(), but do not forget to close it
        // try-with-resource won't work because Session does not implement Autocloseable
    }
}
