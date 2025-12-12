package com.banking.repository.mongo;

import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;
import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoTransactionRepository implements TransactionRepository {

    private final MongoCollection<Document> collection;

    public MongoTransactionRepository() {
        MongoClient client = MongoClients.create("mongodb://127.0.0.1:27017/?directConnection=true");
        MongoDatabase db = client.getDatabase("banking_system");
        this.collection = db.getCollection("transactions");
    }

    @Override
    public void create(Transaction t) {

        Document last = collection.find().sort(new Document("id", -1)).first();
        int newId = (last == null) ? 1 : last.getInteger("id") + 1;
        t.setId(newId);

        Document doc = new Document()
                .append("id", t.getId())
                .append("amount", t.getAmount())
                .append("senderAccount", t.getSenderAccount())
                .append("receiverAccount", t.getReceiverAccount())
                .append("timestamp", t.getTimestamp().toString())
                .append("description", t.getDescription());

        collection.insertOne(doc);
    }

    @Override
    public Transaction getById(int id) {
        Document doc = collection.find(eq("id", id)).first();
        return doc != null ? map(doc) : null;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(map(doc));
        }
        return list;
    }

    @Override
    public void delete(int id) {
        collection.deleteOne(eq("id", id));
    }


    @Override
    public void update(Transaction t) {
        Document update = new Document("$set",
                new Document("amount", t.getAmount())
                        .append("senderAccount", t.getSenderAccount())
                        .append("receiverAccount", t.getReceiverAccount())
                        .append("timestamp", t.getTimestamp().toString())
                        .append("description", t.getDescription())
        );

        collection.updateOne(eq("id", t.getId()), update);
    }

    private Transaction map(Document doc) {
        Transaction t = new Transaction();

        t.setId(doc.getInteger("id"));
        t.setAmount(doc.getDouble("amount"));
        t.setSenderAccount(doc.getString("senderAccount"));
        t.setReceiverAccount(doc.getString("receiverAccount"));
        t.setTimestamp(LocalDateTime.parse(doc.getString("timestamp")));
        t.setDescription(doc.getString("description"));

        return t;
    }
}
