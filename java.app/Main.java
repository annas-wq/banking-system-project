package com.banking.app;

import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;
import com.banking.repository.mongo.MongoTransactionRepository;
import com.banking.repository.postgres.PostgresTransactionRepository;
import com.banking.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Choose Database ===");
        System.out.println("1 - PostgreSQL");
        System.out.println("2 - MongoDB");
        System.out.print("Your choice: ");

        int dbChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        TransactionRepository repo;

        if (dbChoice == 1) {
            repo = new PostgresTransactionRepository();
            System.out.println("Using PostgreSQL\n");
        } else {
            repo = new MongoTransactionRepository();
            System.out.println("Using MongoDB\n");
        }

        TransactionService service = new TransactionService(repo);

        while (true) {

            System.out.println("\n=== CRUD MENU ===");
            System.out.println("1 - Create (POST)");
            System.out.println("2 - Get by ID (GET)");
            System.out.println("3 - Get all (GET ALL)");
            System.out.println("4 - Update by ID (PUT)");
            System.out.println("5 - Delete by ID (DELETE)");
            System.out.println("0 - Exit");

            System.out.print("Choose action: ");
            int action = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (action == 0) {
                System.out.println("Exiting program...");
                break;
            }

            switch (action) {

                case 1: // CREATE
                    Transaction t = new Transaction();

                    System.out.print("Amount: ");
                    t.setAmount(scanner.nextDouble());
                    scanner.nextLine();

                    System.out.print("Sender account: ");
                    t.setSenderAccount(scanner.nextLine());

                    System.out.print("Receiver account: ");
                    t.setReceiverAccount(scanner.nextLine());

                    System.out.print("Description: ");
                    t.setDescription(scanner.nextLine());

                    t.setTimestamp(LocalDateTime.now());
                    service.create(t);

                    System.out.println("Created: " + t);
                    break;

                case 2: // GET BY ID
                    System.out.print("Enter ID: ");
                    int idGet = scanner.nextInt();

                    Transaction result = service.getById(idGet);
                    System.out.println("Result: " + result);
                    break;

                case 3: // GET ALL
                    List<Transaction> list = service.getAll();
                    System.out.println("All transactions:");
                    list.forEach(System.out::println);
                    break;

                case 4: // UPDATE
                    System.out.print("Enter ID to update: ");
                    int idUpd = scanner.nextInt();
                    scanner.nextLine();

                    Transaction toUpdate = service.getById(idUpd);

                    if (toUpdate == null) {
                        System.out.println("Transaction not found.");
                        break;
                    }

                    System.out.print("New amount: ");
                    toUpdate.setAmount(scanner.nextDouble());
                    scanner.nextLine();

                    System.out.print("New description: ");
                    toUpdate.setDescription(scanner.nextLine());

                    toUpdate.setTimestamp(LocalDateTime.now());
                    service.update(toUpdate);

                    System.out.println("Updated: " + toUpdate);
                    break;

                case 5: // DELETE
                    System.out.print("Enter ID to delete: ");
                    int idDel = scanner.nextInt();

                    service.delete(idDel);
                    System.out.println("Deleted.");
                    break;

                default:
                    System.out.println("Unknown command.");
            }
        }

        scanner.close();
    }
}
