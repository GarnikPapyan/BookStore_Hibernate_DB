package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
        Session session = sessionFactory.openSession();
        boolean out = false;
        while (!out) {
            System.out.println("You can choose from these three options what to do ");
            System.out.print("1. Book Management \n");
            System.out.print("2. Customer  Management \n");
            System.out.print("3. Sales Management \n");
            System.out.print("4. Exit program! \n");
            Scanner scanner = new Scanner(System.in);
            String str =  scanner.nextLine();
            switch (str) {
                case "1" -> {
                    boolean out2 = false;
                    while (!out2) {
                        System.out.print("\n");
                        System.out.println("In Book Management you can update books " +
                                "details ,see list of book from genre,add and delete books ");
                        System.out.print("1. Update books details \n");
                        System.out.print("2. Create list from genre \n");
                        System.out.print("3. Add new book \n");
                        System.out.print("4. Delete book \n");
                        System.out.print("5. Out(Back one step ) \n");
                        System.out.print("6. Exit Program \n");
                        String str2 = scanner.nextLine();
                        switch (str2) {
                            case "1" -> Books.updateBookById(session);
                            case "2" -> Books.createListGenre(session);
                            case "3" -> Books.addBook(session);
                            case "4" -> Books.deleteBookById(session);
                            case "5" -> out2 = true;
                            case "6" -> {
                                out = true;
                                out2 = true;
                            }
                            default -> System.out.println("Input 1,2,3,4,5 or 6 ");
                        }
                    }
                }
                case "2" -> {
                    boolean out3 = false;
                    while (!out3) {
                        System.out.print("\n");
                        System.out.println("In Customer Management you can update customer " +
                                "details ,view purchase history,add and delete customer ");
                        System.out.print("1. Update customer details \n");
                        System.out.print("2. View purchase history \n");
                        System.out.print("3. Add new customer \n");
                        System.out.print("4. Delete customer \n");
                        System.out.print("5. Out(Back one step) \n");
                        System.out.print("6. Exit Program \n");
                        String custm = scanner.nextLine();
                        switch (custm) {
                            case "1" -> Customers.customerUpdate(session);
                            case "2" -> Customers.viewPurchaseHistory(session);
                            case "3" -> Customers.addNewCustomer(session);
                            case "4" -> Customers.deleteByCustomerId(session);
                            case "5" -> out3 = true;
                            case "6" -> {
                                out3 = true;
                                out = true;
                            }
                            default -> System.out.println("Input 1,2,3,4,5 or 6 ");
                        }
                    }
                }
                case "3" -> {
                    boolean out4 = false;
                    while (!out4) {
                        System.out.print("\n");
                        System.out.println("In Sales Management you can buying new books, " +
                                "generate a report of all books sold,calculate total revenue by genre ");
                        System.out.print("1. Buying new books \n");
                        System.out.print("2. Report of all sold books \n");
                        System.out.print("3. Total revenue by genre \n");
                        System.out.print("4. Out(Back one step) \n");
                        System.out.print("5. Exit program \n");
                        String sell = scanner.nextLine();
                        switch (sell) {
                            case "1" -> Sales.newSale(session);
                            case "2" -> Sales.reportOfAllBookSold(session);
                            case "3" -> Sales.revenueByGenre(session);
                            case "4" -> out4 = true;
                            case "5" -> {
                                out4 = true;
                                out = true;
                            }
                            default -> System.out.println("Input 1,2,3,4,5 or 6 ");
                        }
                    }
                }
                case "4" -> out = true;
                default -> System.out.println("Plz enter 1 , 2 or 3");
            }
        }
        System.out.println("THE END");
        session.close();
    }
}