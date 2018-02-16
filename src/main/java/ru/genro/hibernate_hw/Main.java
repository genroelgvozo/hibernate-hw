package ru.genro.hibernate_hw;

import org.hibernate.SessionFactory;
import ru.genro.hibernate_hw.summaries.Summary;
import ru.genro.hibernate_hw.summaries.SummaryDAO;
import ru.genro.hibernate_hw.summaries.SummaryService;
import ru.genro.hibernate_hw.users.User;
import ru.genro.hibernate_hw.users.UserDAO;
import ru.genro.hibernate_hw.users.UserService;


class Main {

    public static void main(final String[] args) {

        SessionFactory sessionFactory = createSessionFactory();
        try {


            SummaryService summaryService = createSummaryService(sessionFactory);
            UserService userService = createUserService(sessionFactory, summaryService);

            User headHunterz = new User("Head", "Hunterz");
            System.out.println("persisting " + headHunterz);
            userService.save(headHunterz);
            System.out.println("users in db: " + userService.getAll());
            System.out.println();

            System.out.println("persisting summaries");
            Summary summary1 = new Summary("Skills");
            Summary summary2 = new Summary("Skills++");
            Summary summary3 = new Summary("Skills++");
            headHunterz.addSummary(summary1);
            headHunterz.addSummary(summary2);
            headHunterz.addSummary(summary3);
            summaryService.save(summary1);
            summaryService.save(summary2);
            summaryService.save(summary3);

            summaryService.delete(summary3.getId());
            for(Summary summary : headHunterz.getSummaries())
            {
                System.out.println(summary);
            }

            System.out.println("deleting " + headHunterz);
            userService.delete(headHunterz.id());
            System.out.println("users in db: " + userService.getAll());



        } finally {
            sessionFactory.close();
        }
    }

    private static SessionFactory createSessionFactory() {
        return HibernateConfigFactory.prod().buildSessionFactory();
    }

    private static UserService createUserService(final SessionFactory sessionFactory, SummaryService summaryService) {
        UserDAO userDAO = new UserDAO(sessionFactory);
        return new UserService(sessionFactory, userDAO, summaryService);
    }

    private static SummaryService createSummaryService(final SessionFactory sessionFactory) {
        SummaryDAO summaryDAO = new SummaryDAO(sessionFactory);
        return new SummaryService(sessionFactory, summaryDAO);
    }

    private Main() {
    }
}
