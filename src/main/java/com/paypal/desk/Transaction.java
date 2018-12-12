package com.paypal.desk;


import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction {
    private int id;
    private int user_from;
    private int user_to;
    private double transaction_amount;
    private Date transaction_date;




    public Transaction(int id, int user_from, int user_to, double transaction_amount, Date transaction_date) {
        this.id = id;
        this.user_from = user_from;
        this.user_to = user_to;
        this.transaction_amount = transaction_amount;
        this.transaction_date = transaction_date;
    }

    public int getId() {
        return id;
    }

    public int getUser_from() {
        return user_from;
    }

    public int getUser_to() {
        return user_to;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", user_from=" + user_from +
                ", user_to=" + user_to +
                ", transaction_amount=" + transaction_amount +
                ", transaction_date=" + transaction_date +
                '}';
    }
}
