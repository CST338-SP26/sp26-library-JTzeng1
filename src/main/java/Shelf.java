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
