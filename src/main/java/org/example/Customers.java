package org.example;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "customers")
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customers() {
    }

    public Customers(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    /**
     * you can add a new customer
      */
    public static void addNewCustomer(Session session) {
        Transaction transaction = null;
        Scanner scanner = new Scanner(System.in);
        try {
            transaction = session.beginTransaction();
            System.out.println("Enter the new customer Name ");
            String name = scanner.nextLine();
            System.out.println("Enter the new customer Email ");
            String email = scanner.nextLine();
            System.out.println("Enter the new customer Phone number ");
            String phone = scanner.nextLine();
            System.out.println("Added was successful");
            Customers customers = new Customers(name, email, phone);
            session.persist(customers);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error " + e.getMessage());
        }
    }
    /** you can delete customer */
    public static void deleteByCustomerId(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner =new Scanner(System.in);
        boolean out = false;
        while (!out) {
            try {
                System.out.println("Enter the ID of the customer you want to delete ");
                Long customerId = scanner.nextLong();
                Customers customers = session.get(Customers.class, customerId);
                if (customers != null) {
                    session.remove(customers);
                    System.out.println("Deleted was successful.");
                    out = true;
                } else {
                    System.out.println("Plz enter the valid customer ID!!! ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Plz enter only number ");
                scanner.next();
            }
        }
        transaction.commit();
    }
    /** you can change the phone number and Email of customer */
    public static void customerUpdate(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner =new Scanner(System.in);
        boolean out = false;
        Customers customers = null;
        while (!out) {
            try {
                System.out.println("Enter the CustomerID whose phone number and email you want to change");
                Long customerId = scanner.nextLong();
                customers = session.get(Customers.class,customerId);
                if(customers!=null) {
                    out = true;
                } else {
                    System.out.println("Not Valid customer ID, try again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Plz enter only number for customer ID ");
                scanner.next();
            }
        }
        System.out.println("Enter the new customer Email ");
        scanner.nextLine();
        String email = scanner.nextLine();
        System.out.println("Enter the new Phone Number ");
        String phone = scanner.nextLine();
        customers.setEmail(email);
        customers.setPhone(phone);
        session.persist(customers);
        transaction.commit();
        System.out.println("Update was successful.");
    }
    /** it shows how many books he bought when and the total price. */
    public static void viewPurchaseHistory(Session session) {
        Transaction transaction = session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
        boolean out = false;
        Long customerId = null;
        System.out.println("Enter the ID of the buyer whose purchase history you want to see ");
        while (!out) {
            try {
                customerId = scanner.nextLong();
                Customers customers = session.get(Customers.class,customerId);
                if(customers!=null) {
                    out = true;
                } else {
                    System.out.println("Enter valid customer ID");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter only Number(ID for Customer)");
                scanner.next();
            }
        }
        String queryList =  "from Sales s where s.customers.customerId = :customerId";
        List<Sales> list = session.createQuery(queryList,Sales.class).setParameter("customerId",customerId).getResultList();
        for (Sales sale : list) {
            System.out.println("| "+ "BookID -" + sale.getBooks().getBookID() +
                    " | Sold -" + sale.getQuantitySold() +
                    " | Date of Sale-" + sale.getDateOfSale() +
                    " | Total Price -" + sale.getTotalPrice() + " ");
            System.out.println("|___________|_________|_________________________|_________________________|");
        }
        transaction.commit();
    }

}
