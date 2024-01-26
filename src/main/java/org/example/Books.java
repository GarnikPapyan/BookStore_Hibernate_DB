package org.example;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookID;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false,length = 50)
    private String author;
    @Column(name = "genre", nullable = false,length = 50)
    private String genre;
    @Column(name = "price", nullable = false,scale = 3)
    private Double price;
    @Column(name = "quantityInStock ", nullable = false)
    private Integer quantityInStock;

    public Books() {
    }

    public Books( String title, String author, String genre, Double price, Integer quantityInStock) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /** you can add a new book */
    public static   void addBook(Session session) {
        Transaction transaction = null;
        Scanner scanner = new Scanner(System.in);
        try {

            transaction = session.beginTransaction();

            System.out.println("Add book Title");
            String title = scanner.nextLine();
            System.out.println("Add book Author");
            String author = scanner.nextLine();
            System.out.println("Add book Genre");
            String genre = scanner.nextLine();
            Double price = null;
            boolean out = false;
            while (!out) {
                try {
                    System.out.println("Add book Price ` x,y ");
                    price = scanner.nextDouble();
                    out = true;
                } catch (InputMismatchException e) {
                    System.out.println("Plz enter only number ");
                    scanner.next();
                }
            }
            out = false;
            Integer quantityInStock =  null;
            while (!out) {
                try {
                    System.out.println("Add book QuantityInStock");
                    quantityInStock = scanner.nextInt();
                    out = true;
                } catch (InputMismatchException e) {
                    System.out.println("Plz enter only number ");
                    scanner.next();
                }
            }
            scanner.nextLine();
            Books book = new Books(title, author, genre, price, quantityInStock);
            session.persist(book);
            transaction.commit();
            System.out.println("Added was successful");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error " + e.getMessage());
        }
    }
    /** you can delete book from bookID */
    public static void deleteBookById(Session session ) {
        Transaction transaction =session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
            boolean out = false;
            while (!out) {
                try {
                    System.out.println("Write the ID of the book you want to delete ");
                    Long bookId = scanner.nextLong();
                    Books book = session.get(Books.class, bookId);
                    if (book != null) {
                        session.remove(book);
                        System.out.println("Deleted was successful.");
                        out = true;
                    } else {
                        System.out.println("Plz enter the valid book ID!!! ");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Plz enter only ID ");
                    scanner.next();
                }
            }
            transaction.commit();
    }
    /** you can update book quantity in stack */
    public static void updateBookById(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
            boolean out = false;
            while (!out) {
                try {
                    System.out.println("Enter the BookId whose quantity you want to change");
                    Integer bookId = scanner.nextInt();
                    Books books = session.get(Books.class,bookId);
                    if (books!=null) {
                        while (!out) {
                            System.out.println("Enter the new quantity in stock ");
                            Integer quantityInStock = scanner.nextInt();
                            if(quantityInStock >= 0) {
                                books.setQuantityInStock(quantityInStock);
                                session.persist(books);
                                System.out.println("Update was successful.");
                                out = true;
                            } else {
                                System.out.println("Not valid QuantityInStock, try again!");
                            }
                        }
                    } else {
                        System.out.println("Not Valid book ID, try again!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Plz enter only number ");
                    scanner.next();
                }
            }
            transaction.commit();
    }
    /**
     * You will be given a list of book genres and by choosing one from them,
     * you will receive existing books from this genre in the warehouse.
     */
    public static void createListGenre(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
        String booksQuery = "from Books where genre = :genre";
        String queries = "select distinct books.genre from Books as books";
        List<String> list = session.createQuery(queries,String.class).getResultList();
        if(list.isEmpty()) {
            System.out.println();
            System.out.print("Empty sales table can't sow sold books report");
        } else {
            System.out.print("We have books fromm this genre \n");
            System.out.println(list);
            System.out.println("Enter your favorite genre");
            String genre = "";
            boolean out = false;
            while (!out) {
                genre = scanner.nextLine();
                if(list.contains(genre)) {
                    out = true;
                } else  {
                    System.out.println("Try again and choose type genre from list");
                }
            }
            List<Books> booksList = session.createQuery(booksQuery, Books.class).setParameter("genre", genre).getResultList();
            if (!booksList.isEmpty()) {
                for (Books book : booksList) {
                    System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                }
            } else {
                System.out.println("No books found for the selected genre.");
            }
        }

        transaction.commit();
    }

}
