package com.netent.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.netent.bookstore.constants.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(indexes = {@Index(name = "INDEX_ISBN",  columnList="isbn", unique = true)})
public class Book {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotEmpty
    @Column(nullable = false)
    private String isbn;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @NotEmpty
    @Column(nullable = false)
    private String author;

    @JsonIgnore
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @JsonIgnore
    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @DecimalMin("0.1")
    @Transient
    private Double price;

}
