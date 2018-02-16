package ru.genro.hibernate_hw.hibernateBase;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class TransactionMethods {

    protected SessionFactory sessionFactory;

    protected  <T> T inTransaction(Supplier<T> supplier) {
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

    protected void inTransaction(Runnable runnable) {
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
