package ph.edu.lendly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class LedgerCheckpoint {
    private static final String DATABASE = "jdbc:h2:file:./data/checkpoint";

    public static void main(String[] args) {
        boolean add = args.length == 6 && args[0].equals("add");
        boolean list = args.length == 1 && args[0].equals("list");
        if (!add && !list) {
            System.err.println("Use: list OR add <item> <lender> <borrower> <start-date> <due-date>");
            System.exit(2);
        }

        try {
            Files.createDirectories(Path.of("data"));
            try (Connection db = DriverManager.getConnection(DATABASE, "sa", "")) {
                createTable(db);
                if (add) {
                    saveRequest(db, args);
                } else {
                    listRequests(db);
                }
            }
        } catch (DateTimeParseException ex) {
            System.err.println("Use valid dates in YYYY-MM-DD format.");
            System.exit(2);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(2);
        } catch (IOException | SQLException ex) {
            System.err.println("Could not open or update the ledger: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static void createTable(Connection db) throws IOException, SQLException {
        try (var schema = LedgerCheckpoint.class.getResourceAsStream("/schema.sql");
                var statement = db.createStatement()) {
            if (schema == null) {
                throw new IOException("schema.sql is missing");
            }
            statement.execute(new String(schema.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private static void saveRequest(Connection db, String[] args) throws SQLException {
        String item = required(args[1], 100, "Item");
        String lender = required(args[2], 80, "Lender");
        String borrower = required(args[3], 80, "Borrower");
        LocalDate start = LocalDate.parse(args[4]);
        LocalDate due = LocalDate.parse(args[5]);
        if (lender.equalsIgnoreCase(borrower)) {
            throw new IllegalArgumentException("Lender and borrower must be different.");
        }
        if (due.isBefore(start)) {
            throw new IllegalArgumentException("Due date must be on or after the start date.");
        }

        String sql = """
                INSERT INTO item_request (item, lender, borrower, start_date, due_date)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var insert = db.prepareStatement(sql)) {
            insert.setString(1, item);
            insert.setString(2, lender);
            insert.setString(3, borrower);
            insert.setObject(4, start);
            insert.setObject(5, due);
            insert.executeUpdate();
        }
        System.out.println("Request saved as PENDING_TERMS.");
    }

    private static String required(String input, int maxLength, String label) {
        String value = input.strip();
        if (value.isEmpty() || value.length() > maxLength) {
            throw new IllegalArgumentException(label + " must be 1 to " + maxLength + " characters.");
        }
        return value;
    }

    private static void listRequests(Connection db) throws SQLException {
        String sql = """
                SELECT id, item, lender, borrower, start_date, due_date, status
                FROM item_request ORDER BY id
                """;
        try (var statement = db.createStatement(); var rows = statement.executeQuery(sql)) {
            if (!rows.next()) {
                System.out.println("No item loan requests yet.");
                return;
            }
            do {
                System.out.printf("#%d %s | Lender: %s | Borrower: %s | %s to %s | %s%n",
                        rows.getLong("id"), rows.getString("item"), rows.getString("lender"),
                        rows.getString("borrower"), rows.getObject("start_date", LocalDate.class),
                        rows.getObject("due_date", LocalDate.class), rows.getString("status"));
            } while (rows.next());
        }
    }
}
