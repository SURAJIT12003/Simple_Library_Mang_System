import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class library {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int LOAN_PERIOD_DAYS = 30;
    
    private Map<String, Integer> bookAvailabilityMap;
    private Map<String, List<LocalDate>> bookLoansMap;
    
    public library() {
        bookAvailabilityMap = new HashMap<>();
        bookLoansMap = new HashMap<>();
    }
    
    public void addBook(String author, String title) {
        String key = author + "|" + title;
        bookAvailabilityMap.put(key, bookAvailabilityMap.getOrDefault(key, 0) + 1);
    }
    
    public void readBook(String author, String title) {
        String key = author + "|" + title;
        if (bookAvailabilityMap.containsKey(key) && bookAvailabilityMap.get(key) > 0) {
            bookAvailabilityMap.put(key, bookAvailabilityMap.get(key) - 1);
            LocalDate currentDate = LocalDate.now();
            LocalDate dueDate = currentDate.plusDays(LOAN_PERIOD_DAYS);
            List<LocalDate> loanDates = bookLoansMap.getOrDefault(key, new ArrayList<>());
            loanDates.add(currentDate);
            loanDates.add(dueDate);
            bookLoansMap.put(key, loanDates);
            System.out.println("You have borrowed the book \"" + title + "\" by " + author + ".");
            System.out.println("The book is due on " + dueDate.format(DATE_FORMAT) + ".");
        } else {
            System.out.println("Sorry, the book \"" + title + "\" by " + author + " is not available.");
        }
    }
    
    public void checkAvailability(String author, String title) {
        String key = author + "|" + title;
        if (bookAvailabilityMap.containsKey(key) && bookAvailabilityMap.get(key) > 0) {
            System.out.println("The book \"" + title + "\" by " + author + " is available.");
        } else {
            System.out.println("Sorry, the book \"" + title + "\" by " + author + " is not available.");
        }
    }
    
    public void checkLoans(String author, String title) {
        String key = author + "|" + title;
        if (bookLoansMap.containsKey(key)) {
            List<LocalDate> loanDates = bookLoansMap.get(key);
            int numLoans = loanDates.size() / 2;
            System.out.println("You have borrowed \"" + title + "\" by " + author + " " + numLoans + " times.");
            for (int i = 0; i < numLoans; i++) {
                LocalDate loanDate = loanDates.get(i * 2);
                LocalDate dueDate = loanDates.get(i * 2 + 1);
                System.out.println("Loan #" + (i + 1) + ": Borrowed on " + loanDate.format(DATE_FORMAT) +
                                   ", due on " + dueDate.format(DATE_FORMAT) + ".");
            }
        } else {
            System.out.println("You have not borrowed the book \"" + title + "\" by " + author + ".");
        }
    }
    
    public void checkLastSubmitDate(String author, String title) {
        String key = author + "|" + title;
        if (bookLoansMap.containsKey(key)) {
            List<LocalDate> loanDates = bookLoansMap.get(key);
            LocalDate lastDueDate = loanDates.get(loanDates.size() - 1);
           
            System.out.println("The last due date for the book \"" + title + "\" by " + author +
                               " was " + lastDueDate.format(DATE_FORMAT) + ".");
        } else {
            System.out.println("You have not borrowed the book \"" + title + "\" by " + author + ".");
        }
    }
    
    public void submitBook(String author, String title) {
        String key = author + "|" + title;
        if (bookLoansMap.containsKey(key)) {
            List<LocalDate> loanDates = bookLoansMap.get(key);
            LocalDate lastDueDate = loanDates.get(loanDates.size() - 1);
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isAfter(lastDueDate)) {
                int overdueDays = (int)lastDueDate.until(currentDate).getDays();
                System.out.println("You have returned the book \"" + title + "\" by " + author + ".");
                System.out.println("The book is " + overdueDays + " days overdue.");
            } else {
                System.out.println("You have returned the book \"" + title + "\" by " + author + ".");
            }
            loanDates.clear();
            bookLoansMap.remove(key);
            bookAvailabilityMap.put(key, bookAvailabilityMap.getOrDefault(key, 0) + 1);
        } else {
            System.out.println("You have not borrowed the book \"" + title + "\" by " + author + ".");
        }
    }
    
    public static void main(String[] args) {
        library library = new library();
        // add some popular books to the library
        library.addBook("William Shakespeare", "Romeo and Juliet");
        library.addBook("William Shakespeare", "Hamlet");
        library.addBook("Rabindranath Tagore", "Gitanjali");
        library.addBook("Nicholas Sparks", "The Notebook");
        library.addBook("Jane Austen", "Pride and Prejudice");
        library.addBook("Emily Bronte", "Wuthering Heights");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 7) {
            System.out.println("Welcome to the library management system. Please choose an option:");
            System.out.println("1. Add book");
            System.out.println("2. Read book");
            System.out.println("3. Check availability");
            System.out.println("4. Check loans");
            System.out.println("5. Check last submit date");
            System.out.println("6. Submit book");
            System.out.println("7. Quit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter the author's name: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter the book's title: ");
                    String title = scanner.nextLine();
                    library.addBook(author, title);
                    break;
                case 2:
                    System.out.print("Enter the author's name or book's title: ");
                    String query = scanner.nextLine();
                    boolean found = false;
                    for (String key : library.bookAvailabilityMap.keySet()) {
                        if (key.toLowerCase().contains(query.toLowerCase())) {
                            String[] parts = key.split("\\|");
                            String authorName = parts[0];
                            String bookTitle = parts[1];
                            library.readBook(authorName, bookTitle);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Sorry, no matching book was found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter the author's name or book's title ");
                    String query1 = scanner.nextLine();
                    boolean found1 = false;
                        for (String key : library.bookAvailabilityMap.keySet()) {
                            if (key.toLowerCase().contains(query1.toLowerCase())) {
                            String[] parts = key.split("\\|");
                            String authorName = parts[0];
                            String bookTitle = parts[1];
                            int availableCopies = library.bookAvailabilityMap.get(key);
                            System.out.println("The book \"" + bookTitle + "\" by " + authorName +
                            " is available in the library. There are " + availableCopies + " copies available.");
                            found1 = true;
                        break;
                        }
                        }
                    if (!found1) {
                       System.out.println("Sorry, no matching book was found.");
                    }
                    break;
                    case 4:
                        System.out.print("Enter your name: ");
                        String borrowerName = scanner.nextLine();
                        System.out.println("enter title  ");
                        String titles = scanner.nextLine(); 
                        library.checkLoans(borrowerName,titles);
                        break;
                    case 5:
                        System.out.print("Enter the author's name: ");
                        String authorName = scanner.nextLine();
                        System.out.print("Enter the book's title: ");
                        String bookTitle = scanner.nextLine();
                        library.checkLastSubmitDate(authorName, bookTitle);
                    break;
                    case 6:
                        System.out.print("Enter the author's name: ");
                        String submitAuthorName = scanner.nextLine();
                        System.out.print("Enter the book's title: ");
                        String submitBookTitle = scanner.nextLine();
                        library.submitBook(submitAuthorName, submitBookTitle);
                        break;
                    case 7:
                        System.out.println("Goodbye!");
                        break;
                    default:
                              System.out.println("Invalid choice. Please try again.");
                    break;
                     }
                    }
                scanner.close();
        }
  }
    
