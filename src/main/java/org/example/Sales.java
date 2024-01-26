package org.example;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salesID")
    private Long salesID;
    @ManyToOne
    @JoinColumn(name = "bookID", foreignKey = @ForeignKey(name = "fk_book"))
    private Books books;
    @ManyToOne
    @JoinColumn(name = "customerId", foreignKey = @ForeignKey(name = "fk_customer"))
    private Customers customers;
    @Column(name = "dateOfSale")
    private String dateOfSale;
    @Column(name = "quantitySold", nullable = false)
    private Integer quantitySold;
    @Column(name = "totalPrice", nullable = false,scale = 3)
    private Double totalPrice;

    public Sales() {
    }

    public Sales(Books books, Customers customers, String dateOfSale, Integer quantitySold, Double totalPrice) {
        this.books = books;
        this.customers = customers;
        this.dateOfSale = dateOfSale;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public Long getSalesID() {
        return salesID;
    }

    public void setSalesID(Long salesID) {
        this.salesID = salesID;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public String getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(String dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    /**
     * you can add a new sales
      */
    public static void newSale(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
        boolean out = false;
        System.out.println("If you want buy enter ` BookId");
        while (!out) {
            try {
                Integer booksId = scanner.nextInt();
                Books book = session.get(Books.class, booksId);
                if (book != null) {
                    while (!out) {
                        try {
                            System.out.println("Enter customer ID ` ");
                            Integer customerId = scanner.nextInt();
                            Customers customer = session.get(Customers.class, customerId);
                            if (customer != null) {
                                scanner.nextLine();
                                System.out.println("Enter buying day in this format ` YYYY-MM-DD ");
                                String dateOfSale = scanner.nextLine();
                                while (!out) {
                                    System.out.println("Enter how many pieces of this book you want");
                                    try {
                                        Integer quantitySold = scanner.nextInt();
                                        if (quantitySold > 0 && quantitySold < book.getQuantityInStock()) {
                                            Double totalPrice = quantitySold * book.getPrice();
                                            book.setQuantityInStock(book.getQuantityInStock()-quantitySold);
                                            Sales sales = new Sales(book,customer,dateOfSale,quantitySold,totalPrice);
                                            session.persist(sales);
                                            out = true;
                                            transaction.commit();
                                            System.out.println("Buying was successful");
                                        } else {
                                            System.out.println("There are so many books out of stock, try again");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Plz enter only NUMBER how much you want buy");
                                        scanner.next();
                                    }
                                }
                            } else {
                                System.out.println("Plz enter valid customer ID");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Plz enter Number for Customer ID");
                            scanner.next();
                        }
                    }
                } else {
                    System.out.println("Plz enter valid book ID");
                }

            } catch (InputMismatchException e) {
                System.out.println("Enter only Number from ID");
                scanner.next();
            }
        }

    }
    /** Total revenue by genre.Report on how many books sold in total by genre. */
    public static void revenueByGenre(Session session) {
        Transaction transaction = session.beginTransaction();
        String report = "SELECT b.genre, SUM(s.totalPrice) FROM Sales s JOIN s.books b GROUP BY b.genre" ;
        List<Object[]> lists = session.createQuery(report,Object[].class).getResultList();
        if(lists.isEmpty()) {
            System.out.println();
            System.out.print("Empty sales table can't sow sold books report");
        }else  {
            System.out.println("Here are the books sold by genre");
            for (Object[] list : lists) {
                System.out.println("Book Genre: " + list[0] + " = " + list[1] + "$");
            }
        }
        transaction.commit();
    }
    /** This is a report on books being purchased and who bought them and when.*/
    public static void reportOfAllBookSold(Session session) {
        Transaction transaction = session.beginTransaction();
        String report = "select b.title,c.name, s.dateOfSale from Sales s join s.books b join s.customers c";
        List<Object[]> lists = session.createQuery(report,Object[].class).getResultList();
        if(lists.isEmpty()) {
            System.out.println();
            System.out.print("Empty sales table can't sow sold books report");
        }
        for (Object [] list: lists) {
            System.out.println("Book: " + list[0] +" " + "Customer Name: " + list[1] + " "
                    + "Date of Sale: " + list[2]);
            System.out.println();
        }
        transaction.commit();
    }

}