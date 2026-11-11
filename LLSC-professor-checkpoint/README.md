# LENDING LEDGER AND SAVING CIRCLE

## Item loan checkpoint

This small checkpoint records proposed loans of personal items such as books, umbrellas and chargers. Each request stores the item, lender, borrower, start date and due date in an embedded H2 database. It stays at `PENDING_TERMS` while awaiting the later agreement workflow.

The lender provides the item and the borrower receives it. The demonstration uses fictional names and a desk lamp.

## Run on GitHub

1. Put the contents of this folder at the root of a GitHub repository, including the hidden `.github` folder.
2. Open **Actions > Run checkpoint demo**. A push runs the demo automatically when Actions is enabled.
3. Open the latest run and expand **Save and read a sample request** to see the saved record and the invalid date check.

Repository collaborators with write access can also select **Run workflow** after the workflow is on the default branch. For a private repository, grant the professor access before sharing the link. [GitHub workflow instructions](https://docs.github.com/en/actions/how-tos/manage-workflow-runs/manually-run-a-workflow)

## Run locally

Use JDK 21 and Maven. From the folder containing `pom.xml`, run:

```sh
mvn package
java -jar target/ledger-checkpoint.jar add "Desk lamp" Ana Ben 2026-11-12 2026-11-19
java -jar target/ledger-checkpoint.jar list
```

The second Java process reads the saved request from `data/checkpoint.mv.db`. The program creates the table from `src/main/resources/schema.sql`. H2 is included in the built JAR. The first Maven build downloads the required libraries and build plugins.

## Files to review

- `src/main/java/ph/edu/lendly/LedgerCheckpoint.java` handles command arguments, validation, insert and list
- `src/main/resources/schema.sql` defines the single table
- `.github/workflows/checkpoint.yml` runs the demo with sample data in a temporary directory
