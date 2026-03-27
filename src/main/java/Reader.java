import java.util.List;

public class Reader {
    public static final int cardNumber_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ =3;
    public static final int BOOK_START = 4;

    private String cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(String cardNumber, String name, String phone, List<Book> books) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        this.books = books;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
