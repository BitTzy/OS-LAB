import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class LedgerCheckpoint {
    private static final Path REQUESTS = Path.of("item-requests.txt");

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Lending Ledger and Saving Circle");
            while (true) {
                System.out.println("\n1. Add item request");
                System.out.println("2. List requests");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                if (!input.hasNextLine()) {
                    return;
                }

                String choice = input.nextLine().strip();
                try {
                    switch (choice) {
                        case "1" -> addRequest(input);
                        case "2" -> listRequests();
                        case "0" -> { return; }
                        default -> System.out.println("Choose 1, 2 or 0.");
                    }
                } catch (DateTimeParseException ex) {
                    System.out.println("Enter valid dates in YYYY-MM-DD format.");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Could not read or save requests: " + ex.getMessage());
                }
            }
        }
    }

    private static void addRequest(Scanner input) throws IOException {
        String item = readField(input, "Item", 100);
        String lender = readField(input, "Lender", 80);
        String borrower = readField(input, "Borrower", 80);
        LocalDate start = LocalDate.parse(readField(input, "Start date (YYYY-MM-DD)", 10));
        LocalDate due = LocalDate.parse(readField(input, "Due date (YYYY-MM-DD)", 10));

        if (lender.equalsIgnoreCase(borrower)) {
            throw new IllegalArgumentException("Lender and borrower must be different.");
        }
        if (due.isBefore(start)) {
            throw new IllegalArgumentException("Due date must be on or after the start date.");
        }

        String request = "%s | Lender: %s | Borrower: %s | %s to %s | PENDING_TERMS%n"
                .formatted(item, lender, borrower, start, due);
        Files.writeString(REQUESTS, request, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("Request saved.");
    }

    private static void listRequests() throws IOException {
        List<String> requests;
        try {
            requests = Files.readAllLines(REQUESTS);
        } catch (NoSuchFileException ex) {
            requests = List.of();
        }
        if (requests.isEmpty()) {
            System.out.println("No item loan requests yet.");
            return;
        }
        for (String request : requests) {
            System.out.println(request);
        }
    }

    private static String readField(Scanner input, String label, int maxLength) {
        System.out.print(label + ": ");
        String value = input.hasNextLine() ? input.nextLine().strip() : "";
        if (value.isEmpty() || value.length() > maxLength) {
            throw new IllegalArgumentException(label + " must be 1 to " + maxLength + " characters.");
        }
        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(label + " contains an unsupported character.");
        }
        return value;
    }
}
