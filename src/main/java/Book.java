import java.time.LocalDate;


/**
 * @author Justin Tzeng
 * This is a plain old java object (POJO) that represent a Book
 * It will be used in the larger Library project
 */


public class Book {
    public static final int ISBN_ = 0;
    public static final int TITLE_ = 1;
    public static final int SUBJECT_ = 2;
    public static final int PAGE_COUNT_ =3;
    public static final int AUTHOR_ = 4;
    public static final int DUE_DATE_ = 5;


    private String isbn;
    private String title;
    private String subject;
    private int pageCount;
    private String author;
    private LocalDate dueDate;




    public Book(String isbn, String title, String subject, int pageCount, String author, LocalDate dueDate) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.pageCount = pageCount;
        this.author = author;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return title + " by" + author + " ISBN:" + isbn;
    }



    public boolean equals(Object object) {
        if (this == object) {
            return false;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Book book = (Book) object;


        if(getPageCount() != book.getPageCount()) {
            return false;
        }


        if(getISBN() != null ? !getISBN().equals(book.getISBN()) : book.getISBN() != null) {
            return false;
        }


        if(getTitle() != null ? !getTitle().equals(book.getTitle()) : book.getTitle() != null) {
            return false;
        }
        if(getSubject() != null ? !getSubject().equals(book.getSubject()) : book.getSubject() != null) {
            return false;
        }


        return getAuthor() != null ? getAuthor().equals(book.getAuthor()) : book.getAuthor() == null;


    }


    public int hashCode() {
        int result = getISBN() != null ? getISBN().hashCode() : 0;
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + getPageCount();
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        return result;
    }


    public String getISBN() {
        return isbn;
    }


    public void setISBN(String isbn) {
        this.isbn = isbn;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public int getPageCount() {
        return pageCount;
    }


    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public LocalDate getDueDate() {
        return dueDate;
    }


    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}







