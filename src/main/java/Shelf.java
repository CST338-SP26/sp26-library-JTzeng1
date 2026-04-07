import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public static final int SHELF_NUMBER_ =0;
    public static final int SUBJECT_ = 1;
    private HashMap<Book, Integer> books;
    private int shelfNumber;
    private String subject;

    /*So I had an issue when running. The console says Books was never initialized in the default constructor,
    so it stayed null, and calling books.containsKey(book) caused a crash,
    so I added books = new HashMap<>() which fixes it by creating the HashMap before it’s used.
    I know that the default constructor needed to empty but this is the only way I knew how to fix this
     */

    public Shelf() {
        books = new HashMap<>();

    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Shelf shelf)) return false;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public int getBookCount(Book book) {
        if(!books.containsKey(book)) {
            return -1;
        } else {
            return books.get(book);
        }
    }

    public Code addBook(Book book) {
        if(books.containsKey(book)) {
            int count = books.getOrDefault(book, 0);
            books.put(book, count + 1);
            System.out.println(book.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;

        } else if(book.getSubject().equals(this.subject)) {
            int count = books.getOrDefault(book, 0);
            books.put(book, count + 1);
            System.out.println(book.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;

        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;

        }


    }

    public Code  removeBook(Book book) {
        if(!books.containsKey(book)) {
            System.out.println(book.getTitle() + " is not on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        int count = books.get(book);
        if(count == 0) {
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        if(count >= 1) {
            books.put(book, count - 1);
        } else {
            books.put(book, 0);
        }

        System.out.println(book.getTitle() + " successfully removed from shelf " + subject);
        return Code.SUCCESS;

    }

    public String listBooks() {
        int totalBooks = 0;

        for(int i = 0; i < books.size(); i++) {
            totalBooks += books.get(i);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(totalBooks).append(" books on shelf: ").append(this.toString());

        if(books.isEmpty()) {
            return sb.toString();
        }

        for (int i = 0; i < books.size(); i++) {
            Integer book = books.get(i);

            sb.append("\n");
            sb.append(book.toString());
            sb.append(" ");
            sb.append(books.get(book));

        }
        return sb.toString();


    }


    public void setBooks(HashMap<Book, Integer> books) {
            this.books = books;
        }

        public int getShelfNumber() {
            return shelfNumber;
        }

        public void setShelfNumber(int shelfNumber) {
            this.shelfNumber = shelfNumber;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
}
