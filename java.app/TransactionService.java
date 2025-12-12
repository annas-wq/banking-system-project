package com.banking.service;

import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void create(Transaction t) {
        repository.create(t);
    }

    public Transaction getById(int id) {
        return repository.getById(id);
    }

    public List<Transaction> getAll() {
        return repository.getAll();
    }

    public void update(Transaction t) {
        repository.update(t);
    }



    public Transaction getById(Integer id) {
        return null;
    }

    public void delete(int idDel) {
    }
}
