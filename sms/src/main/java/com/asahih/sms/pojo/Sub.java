package com.asahih.sms.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Asahi Huang on 2020/10/13.
 */
@Entity
@Table(name = "sub")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Sub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;

    private String user;

    private String title;

    private String comment;

    /*
     JPA对于大写的字段需要写明为小写
     */
    @Column(name = "overdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date overDate;
    private String price;
    @Column(name = "payway")
    private String payWay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String username) {
        this.user = username;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
