package ru.genro.hibernate_hw.hibernateBase;

import org.hibernate.cfg.Configuration;
import ru.genro.hibernate_hw.summaries.Summary;
import ru.genro.hibernate_hw.users.User;

public class HibernateConfigFactory {

  public static Configuration prod() {
    return new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(Summary.class);
  }

  private HibernateConfigFactory() {
  }
}
