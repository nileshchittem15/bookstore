package com.netent.bookstore.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(indexes = {@Index(name = "INDEX_BOOK_ID",  columnList="bookId", unique = true)})
@Check(constraints = "quantity >= 0")
public class BookInventory {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private String bookId;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    @JsonIgnore
    private Timestamp updatedAt;

    public BookInventory(String bookId,
                         Double price,
                         Integer quantity) {
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
    }

    public BookInventory(String bookId,
                         Double price,
                         Integer quantity,
                         Timestamp createdAt,
                         Timestamp updatedAt) {
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BookInventory() {
    }
}
