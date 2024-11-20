package com.example.movieticket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import jakarta.persistence.FetchType;

@Entity()
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentid;
    private String cardnumber;
    private String cardname;
    private String expirydate;
    private int cvv;
    private int userid;
    private double amount;

    public Payment() {
    }

    public Payment(int paymentid, String cardnumber, String cardname, String expirydate, int cvv, int userid,
            double amount) {
        this.paymentid = paymentid;
        this.cardnumber = cardnumber;
        this.cardname = cardname;
        this.expirydate = expirydate;
        this.cvv = cvv;
        this.userid = userid;
        this.amount = amount;
    }

    public int getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String carnumber) {
        this.cardnumber = carnumber;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
