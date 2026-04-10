import Utilities.Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
/*
Author: Justin Tzeng
Abstract: This programs manages books, shelves, and readers, allowing books to be
added, organized, checked out, and returned while enforcing rules like lending limits and data validation.
 */
import static Utilities.Code.SHELF_EXISTS_ERROR;
import static Utilities.Code.SHELF_NUMBER_PARSE_ERROR;

public class Library {
    public static int LENDING_LIMIT = 5;
    private HashMap<Book, Integer> books;
    private int libraryCard;
    private String name;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;


    public Library(String name) {
        this.name = name;
        this.books = new HashMap<>();
        this.readers = new ArrayList<>();
        this.shelves = new HashMap<>();
    }

    public Code addBook(Book newBook) {

        if (books.containsKey(newBook)) {
            int count = books.get(newBook);
            count++;
            books.put(newBook, count);

            System.out.println(count + " copies of " + newBook.getTitle() + " in the stacks");

        } else {
            books.put(newBook, 1);
            System.out.println(newBook.getTitle() + " added to the stack.");

        }

        String subject = newBook.getSubject();
        Shelf shelf = shelves.get(subject);

        if(shelf != null) {
            return shelf.addBook(newBook);
        } else {
            System.out.println("No shelf for " + subject + " books" );
            return SHELF_EXISTS_ERROR;
        }


    }

    private Code addBookToShelf(Book book, Shelf shelf) {
        Code code = returnBook(book);
        if(code == Code.SUCCESS) {
            return Code.SUCCESS;
        }

        if(!shelf.getSubject().equals(book.getSubject())) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;

        }

        Code addBookToShelf = shelf.addBook(book);
        if(addBookToShelf == Code.SUCCESS) {
            System.out.println(book + " added to shelf");
            return Code.SUCCESS;
        }

        System.out.println("Could not add " + book + " to shelf");
        return addBookToShelf;
    }

    public Code addReader(Reader reader) {
        for (int i = 0; i < readers.size(); i++) {
            Reader numReaders = readers.get(i);

            if(numReaders.equals(reader)) {
                System.out.println(reader.getName() + " already has an account!");
                return Code.READER_ALREADY_EXISTS_ERROR;
            }

            if(reader.getCardNumber() == numReaders.getCardNumber()) {
                System.out.println(reader.getName() +  " and " + reader.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }

        }
        readers.add(reader);
        System.out.println(reader.getName() + " added to the library");

        if(reader.getCardNumber() > libraryCard){
            libraryCard = reader.getCardNumber();

        }
        return Code.SUCCESS;


    }

    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;

        }
        List<Book> allBooks = new ArrayList<>(books.keySet());

        for (int i = 0; i < allBooks.size(); i++) {
            Book currentBook = allBooks.get(i);

            if (currentBook.getSubject().equals(shelf.getSubject())) {
                int count = books.get(currentBook);

                for (int j = 0; j < count; j++) {
                    shelf.addBook(currentBook);


                }
            }

        }
        shelves.put(shelf.getSubject(), shelf);
        return Code.SUCCESS;


    }

    public Code addShelf(String shelfSubject)  {
        int newNumber = shelves.size() + 1;
        Shelf shelf = new Shelf(newNumber, shelfSubject);
        return addShelf(shelf);
    }

    public Code checkOutBook(Reader  reader, Book book) {
        if(!readers.contains(reader)) {
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;

        }

        if(reader.getBookCount() >= LENDING_LIMIT) {
            System.out.println(reader.getName() + " has reached the leading limit (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }

        if (!books.containsKey(book)) {
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Shelf shelf = shelves.get(book.getSubject());
        if (shelf == null) {
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }

        if (shelf.getBookCount(book) < 1) {
            System.out.println("ERROR: no copies of " + book + " remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Code addCode = reader.addBook(book);
        if (addCode != Code.SUCCESS) {
            System.out.println("Couldn't checkout " + book);
            return addCode;
        }
        Code removeCode = shelf.removeBook(book);

        if (removeCode == Code.SUCCESS) {
            System.out.println(book + " checked out successfully");
        }

        return removeCode;


    }

    public static LocalDate convertDate(String date, Code  errorCode) {
        if(date.equals("000")) {
            return LocalDate.of(1970, 1,1);

        }
        String[] dateParts = date.split("-");

        if(dateParts.length != 3){
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1,1);
        }

        try {
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);

            if (year < 0 || month < 0 || day < 0) {

                System.out.println("Error converting date: Year " + year);
                System.out.println("Error converting date: Month " + month);
                System.out.println("Error converting date: Day " + day);
                System.out.println("Using default date (01-jan-1970)");

                return LocalDate.of(1970, 1, 1);
            }

            return LocalDate.of(year, month, day);

        } catch (NumberFormatException e) {

            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");

            return LocalDate.of(1970, 1, 1);
        }



    }

    public static int convertInt (String recordCountString, Code  code) {
        try {
            return Integer.parseInt(recordCountString);
        }
        catch (NumberFormatException exception) {

            System.out.println("Value which caused the error: " + recordCountString);

            if (code == Code.BOOK_COUNT_ERROR) {
                System.out.println("Error message: Error: Could not read number of books");
            }

            else if (code == Code.PAGE_COUNT_ERROR) {
                System.out.println("Error message: Error: could not parse page count");
            }


            else if (code == Code.DATE_CONVERSION_ERROR) {
                System.out.println("Error message: Error: Could not parse date component");
            }


            else {
                System.out.println("Error message: Error: Unknown conversion error");
            }

            return code.getCode();
        }


    }

    private Code errorCode(int codeNumber) {
        Code[] allCodes = Code.values();

        for (int i = 0; i < allCodes.length; i++) {
            Code currentCode = allCodes[i];

            if (currentCode.getCode() == codeNumber) {
                return currentCode;

            }
        }
        return Code.UNKNOWN_ERROR;

    }

    public Book getBookByISBN(String isbn) {
        List<Book> book = new ArrayList<>(books.keySet());
        for(int i = 0; i < book.size(); i++) {
            Book currentBook = book.get(i);
            if(currentBook.getISBN().equals(isbn)) {
                return currentBook;

            }

        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public int getLibraryCardNumber() {
        return libraryCard + 1;
    }

    public String getName() {
        return name;
    }

    public Reader getReaderByCard(int cardNumber) {
        for(int i = 0; i < readers.size(); i++) {
            Reader reader = readers.get(i);

            if(reader.getCardNumber() == cardNumber) {
                return reader;
            }

        }
        System.out.println("Could not find a reader with card #" + cardNumber);
        return null;
    }

    public Shelf getShelf(String subject) {
        if(shelves.containsKey(subject)) {
            return shelves.get(subject);
        }
        System.out.println("No shelf for " + subject + " books");
        return null;

    }

    public Shelf getShelf(Integer shelfNumber) {
        List<Shelf> shelfList = new ArrayList<>(shelves.values());

        for(int i = 0; i < shelfList.size(); i++) {
            Shelf currentShelf = shelfList.get(i);
            int currentShelfNumber = currentShelf.getShelfNumber();

            if(currentShelfNumber == shelfNumber){
                return currentShelf;
            }
        }
        System.out.println("No shelf number " + shelfNumber + " found");
        return null;



    }

    public Code init (String filename) {
        Scanner scnr;
        try {
            scnr = new Scanner(new File(filename));
        } catch (FileNotFoundException e){
            return Code.FILE_NOT_FOUND_ERROR;
        }

        int bookCount = convertInt(scnr.nextLine(), Code.BOOK_COUNT_ERROR);

        if(bookCount < 0) {
            return Code.BOOK_COUNT_ERROR;
        }

        Code check = initBooks(bookCount, scnr);
        if(check !=  Code.SUCCESS) {
            return check;
        }

        listBooks();

        int shelfCount = convertInt(scnr.nextLine(), Code.BOOK_COUNT_ERROR);

        if(shelfCount < 0) {
            return Code.BOOK_COUNT_ERROR;
        }

        check = initShelf(shelfCount, scnr);
        if(check !=  Code.SUCCESS) {
            return check;
        }

        listShelves();

        int readerCount = convertInt(scnr.nextLine(), Code.BOOK_COUNT_ERROR);

        if(readerCount < 0) {
            return Code.BOOK_COUNT_ERROR;
        }

        check = initReader(readerCount, scnr);
        if(check !=  Code.SUCCESS) {
            return check;
        }

        listReaders();
        return Code.SUCCESS;


    }


    private Code initBooks(int bookCount, Scanner scan) {
        if (bookCount < 1) {
            return Code.LIBRARY_ERROR;
        }

        String[] parseBook = null;
        for (int i = 0; i < bookCount; i++) {
            String bookInfo = scan.nextLine();
            parseBook = bookInfo.split(" ");


            if (parseBook.length <= Book.DUE_DATE_) {
                return Code.BOOK_RECORD_COUNT_ERROR;
            }

            String isbn = parseBook[Book.ISBN_];
            String title = parseBook[Book.TITLE_];
            String subject = parseBook[Book.SUBJECT_];
            String author = parseBook[Book.AUTHOR_];

            int pageNumber = convertInt(parseBook[Book.PAGE_COUNT_], Code.BOOK_COUNT_ERROR);

            if (pageNumber <= 0) {
                return Code.BOOK_COUNT_ERROR;
            }

            LocalDate dueDate = convertDate(parseBook[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if (dueDate == null) {
                return Code.DATE_CONVERSION_ERROR;
            }

            Book book = new Book(isbn, title, subject, pageNumber, author, dueDate);
            addBook(book);
        }

        return Code.SUCCESS;




    }

    private Code initReader(int readerCount, Scanner scan) {
        if (readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }

        for (int i = 0; i < readerCount; i++) {
            String readerInfo = scan.nextLine();
            String[] parseReader = readerInfo.split(",");

            int cardNumber = convertInt(parseReader[Reader.cardNumber_], Code.READER_CARD_NUMBER_ERROR);

            String readerName = parseReader[Reader.NAME_];
            String phone = parseReader[Reader.PHONE_];
            Reader reader = new Reader(cardNumber, readerName, phone);

            readers.add(reader);

            int bookCount = convertInt(parseReader[Reader.BOOK_COUNT_], Code.BOOK_COUNT_ERROR);
            int counter = Reader.BOOK_START_;

            for (int j = 0; j < bookCount; j++) {
                String isbn =  parseReader[counter];
                counter++;

                LocalDate dueDate = convertDate(parseReader[counter], Code.DATE_CONVERSION_ERROR);
                counter++;

                Book book = getBookByISBN(isbn);
                if(book == null) {
                    System.out.println("ERROR");
                    continue;
                }

                book.setDueDate(dueDate);
                checkOutBook(reader, book);
            }

        }
        return Code.SUCCESS;




    }

    private Code initShelf(int shelfCount, Scanner scanner) {
        if(shelfCount < 1) {
            return Code.SHELF_COUNT_ERROR;
        }

        for (int i = 0; i < shelfCount; i++) {
            String shelfInfo = scanner.nextLine();
            String[] parseShelf = shelfInfo.split(" ");

            int shelfNumber = convertInt(parseShelf[Shelf.SHELF_NUMBER_], SHELF_NUMBER_PARSE_ERROR);
            if(shelfNumber <0) {
                return SHELF_NUMBER_PARSE_ERROR;

            }
            String subject = parseShelf[1];
            Shelf shelf = new Shelf(shelfNumber, subject);
            Code result  = addShelf(shelf);
            if(result != Code.SUCCESS) {
                return result;
            }
        }

        if(shelves.size() < shelfCount) {
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }
        return Code.SUCCESS;


    }

    public int listBooks() {
        int numBooks  = 0;
        List<Book> keys = new ArrayList<>(books.keySet());
        for(int i = 0; i < keys.size(); i++) {
            Book book = keys.get(i);
            int count = books.get(book);

            System.out.println(count + " copies of " + book);
            numBooks += count;
        }
        return numBooks;



    }

    public int listReaders() {
        for(int i = 0; i < readers.size(); i++) {
            System.out.println(readers.get(i).toString());

        }
        return readers.size();

    }

    public int listReaders(boolean showBooks) {
        for(int i = 0; i < readers.size(); i++) {
            Reader reader = readers.get(i);

            if(showBooks) {
                System.out.println(reader.getName() + "(#" + reader.getCardNumber() + ") has the following books: ");
                System.out.println(reader.getBooks());


            } else {
                System.out.println(reader.toString());
            }
        }
        return readers.size();


    }

    public int listShelves(boolean showBooks) {
        List<Shelf> shelfList = new ArrayList<>(shelves.values());
        for(int i = 0; i < shelfList.size(); i++) {
            Shelf shelf = shelfList.get(i);

            if(showBooks) {
                shelf.listBooks();
            } else {
                System.out.println(shelf);

            }
        }
        return shelfList.size();
    }

    public int listShelves() {
        return listShelves(false);

    }

    public Code removeReader(Reader reader) {
        for (int i = 0; i < readers.size(); i++) {
            Reader r = readers.get(i);

            if (r.equals(reader)) {
                if (r.getBookCount() > 0) {
                    System.out.println(r.getName() + " must return all books!");
                    return Code.READER_STILL_HAS_BOOKS_ERROR;
                }
                readers.remove(i);
                return Code.SUCCESS;


            }
        }

        System.out.println(reader.getName() + " is not part of this Library");
        return Code.READER_NOT_IN_LIBRARY_ERROR;


    }

    public Code returnBook(Reader reader, Book book) {
        if (!reader.getBooks().contains(book)) {
            System.out.println(reader.getName() + " doesn't have " + book.getTitle());
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }

        if(!books.containsKey(book)) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;

        }

        System.out.println(reader.getName() + " is returning " + book);
        Code code = reader.removeBook(book);

        if(code != Code.SUCCESS) {
            return returnBook(book);

        }
        System.out.println("Could not return " + book);
        return code;

    }

    public Code returnBook(Book book) {
        String subject = book.getSubject();
        Shelf shelf = shelves.get(subject);

        if(shelf == null) {
            System.out.println("No shelf for " + book);
            return SHELF_EXISTS_ERROR;

        }
        return shelf.addBook(book);



    }



}
