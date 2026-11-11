# MILESTONE 1 DEMONSTRATION

The demo shows one part of **Lending Ledger and Saving Circle**. Enter a pending item loan request, exit the program, then reopen it and list the saved request. It uses a console and a text file. All steps run without a web server, SQL setup or administrator rights.

## NetBeans

Use a computer that already has NetBeans and JDK 21, or use the GitHub route below.

1. Create a **Java with Ant > Java Application** project in a writable folder such as Documents.
2. Under **Source Packages**, create a Java class named `LedgerCheckpoint`.
3. Keep the generated `package` line if present. Replace the rest of the class file with the contents of `LedgerCheckpoint.java` from this folder.
4. Right-click the class and select **Run File**. Enter the demo values in the **Output** window. [NetBeans Java quick start](https://netbeans.apache.org/tutorial/main/kb/docs/java/quickstart/)

You can also run `java LedgerCheckpoint.java` in a terminal from this folder with JDK 21.

## What to show

1. Choose **1. Add item request**.
2. Enter `Desk lamp`, `Ana` as lender, `Ben` as borrower, `2026-11-12` as start date and `2026-11-19` as due date.
3. Choose **0. Exit**. Run the same class again and choose **2. List requests**.
4. Point out the saved `PENDING_TERMS` request. The console saves it in `item-requests.txt` in the working folder.

## GitHub Actions

1. Copy the extracted files to the root of a GitHub repository where you have write access and Actions is enabled. Include `.github/workflows/milestone-1-demo.yml`.
2. Commit the files. The push starts **Milestone 1 demo**. It uses GitHub's runner and fictional data.
3. Open **Actions > Milestone 1 demo**, select the completed run and expand **Save and reopen an item request**. Share that run link with your professor.

Your professor needs a signed-in GitHub account and read access to view the logs. Repository admin rights are unnecessary for viewing them. The repository must already allow Actions. [GitHub workflow logs](https://docs.github.com/en/actions/how-tos/monitor-workflows/use-workflow-run-logs)
