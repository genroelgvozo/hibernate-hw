package ru.genro.hibernate_hw;

import org.hibernate.cfg.Configuration;
import ru.genro.hibernate_hw.summaries.Summary;
import ru.genro.hibernate_hw.users.User;

class HibernateConfigFactory {

  public static Configuration prod() {
    return new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(Summary.class);
  }

  private HibernateConfigFactory() {
  }
}
