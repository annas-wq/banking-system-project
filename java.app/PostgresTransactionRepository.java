package com.banking.repository.postgres;

import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTransactionRepository implements TransactionRepository {

    private final Connection connection;

    public PostgresTransactionRepository() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/banking_system",
                    "postgres",
                    "12345"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }

    @Override
    public void create(Transaction t) {
        String sql = "INSERT INTO transactions (amount, sender_account, receiver_account, timestamp, description) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, t.getAmount());
            stmt.setString(2, t.getSenderAccount());
            stmt.setString(3, t.getReceiverAccount());
            stmt.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
            stmt.setString(5, t.getDescription());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting transaction", e);
        }
    }

    @Override
    public Transaction getById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transaction", e);
        }
        return null;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY id";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all transactions", e);
        }

        return list;
    }

    @Override
    public void update(Transaction t) {
        String sql = "UPDATE transactions SET amount=?, sender_account=?, receiver_account=?, timestamp=?, description=? WHERE id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, t.getAmount());
            stmt.setString(2, t.getSenderAccount());
            stmt.setString(3, t.getReceiverAccount());
            stmt.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
            stmt.setString(5, t.getDescription());
            stmt.setInt(6, t.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }


    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getDouble("amount"),
                rs.getString("sender_account"),
                rs.getString("receiver_account"),
                rs.getTimestamp("timestamp").toLocalDateTime(),
                rs.getString("description")
        );
    }
}
