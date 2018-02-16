package ru.genro.hibernate_hw.users;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import ru.genro.hibernate_hw.HibernateTestBase;
import ru.genro.hibernate_hw.summaries.Summary;
import ru.genro.hibernate_hw.summaries.SummaryDAO;
import ru.genro.hibernate_hw.summaries.SummaryService;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserServiceTest extends HibernateTestBase {

    private static final SummaryDAO summaryDAO = new SummaryDAO(sessionFactory);
    private static final SummaryService summaryService = new SummaryService(sessionFactory, summaryDAO);
    private static final UserDAO userDAO = new UserDAO(sessionFactory);
    private static final UserService userService = new UserService(sessionFactory, userDAO, summaryService);

    @Test
    public void saveShouldInsertUserInDBAndReturnUserWithId() {

        User user1 = new User("first name 1", "last name 1");
        User user2 = new User("first name 2", "last name 2");

        userService.save(user1);
        userService.save(user2);

        assertEquals("first name 1", user1.firstName());
        assertEquals("last name 1", user1.lastName());
        assertEquals(user1, userService.get(user1.id()).get());

        assertEquals("first name 2", user2.firstName());
        assertEquals("last name 2", user2.lastName());
        assertEquals(user2, userService.get(user2.id()).get());
    }

    @Test
    public void getShouldReturnUserById() {

        User user = new User("first name 1", "last name 1");
        userService.save(user);

        Optional<User> userFromDB = userService.get(user.id());

        assertEquals(user, userFromDB.get());
    }

    @Test
    public void getShouldReturnEmptyOptionalIfNoUserWithSuchId() {

        int nonExistentUserId = 123;

        Optional<User> user = userService.get(nonExistentUserId);

        assertFalse(user.isPresent());
    }

    @Test
    public void getAllShouldReturnAllUsers() {

        User user1 = new User("first name 1", "last name 1");
        User user2 = new User("first name 2", "last name 2");
        userService.save(user1);
        userService.save(user2);

        Set<User> users = userService.getAll();

        assertEquals(ImmutableSet.of(user1, user2), users);
    }

    @Test
    public void changeFirstNameShouldChangeFirstName() {

        User user = new User("first name", "last name");
        userService.save(user);

        userService.changeFirstName(user.id(), "new first name");

        User userFromDB = userService.get(user.id()).get();
        assertEquals("new first name", userFromDB.firstName());
        assertEquals(user.lastName(), userFromDB.lastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeFirstNameShouldThrowIllegalArgumentExceptionIfNoUserWithSuchId() {

        int nonExistentUserId = 123;
        assertFalse(userService.get(nonExistentUserId).isPresent());

        userService.changeFirstName(nonExistentUserId, "new first name");

        fail();
    }

    @Test
    public void deleteShouldDeleteUserById() throws Exception {

        User user1 = new User("first name 1", "last name 1");
        User user2 = new User("first name 2", "last name 2");
        userService.save(user1);
        userService.save(user2);

        userService.delete(user1.id());

        assertFalse(userService.get(user1.id()).isPresent());
        assertTrue(userService.get(user2.id()).isPresent());
    }


    @Test
    public void deleteUserShouldCauseDeleteSummaries() throws Exception {
        User user = new User("name","surname");
        userService.save(user);

        Summary summary = new Summary("Programming",3);
        userService.addSummary(user,summary);
        userService.delete(user.id());

        assertFalse(summaryService.get(summary.getId()).isPresent());
    }
}
