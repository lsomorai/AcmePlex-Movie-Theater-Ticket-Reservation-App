/*
 * Payment.java
 * Author: Cory Wu
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import jakarta.persistence.FetchType;
import lombok.NonNull;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Column;

@Entity()
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid")
    private Long id;
    @Column(name = "cardnumber")
    private String cardnumber;
    private String cardname;
    private String expirydate;
    private String cvv;
    private double amount;
    private int userid;
    private String note;

    public Payment() {
    }

    public Payment(Long id, String cardnumber, String cardname, String expirydate, String cvv, int userid,
            double amount, String note) {
        this.id = id;
        this.cardnumber = cardnumber;
        this.cardname = cardname;
        this.expirydate = expirydate;
        this.cvv = cvv;
        this.userid = userid;
        this.amount = amount;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
