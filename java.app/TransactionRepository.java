package com.banking.repository;

import com.banking.model.Transaction;
import java.util.List;

public interface TransactionRepository {

    void create(Transaction transaction);

    Transaction getById(int id);

    List<Transaction> getAll();

    void update(Transaction transaction);


    void delete(int id);
}
