package com.netent.bookstore;

import com.netent.bookstore.constants.ResponseMessages;
import com.netent.bookstore.model.Book;
import com.netent.bookstore.model.BookInventory;
import com.netent.bookstore.model.BookOrder;
import com.netent.bookstore.repository.BookInventoryRepository;
import com.netent.bookstore.repository.BookOrderRepository;
import com.netent.bookstore.repository.BookRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class BookDataTest {

    private static Book testBook;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private BookOrderRepository bookOrderRepository;



    @BeforeClass
    public static void setUp(){

        testBook = new Book();
        testBook.setId(UUID.randomUUID().toString());
        testBook.setIsbn("12345");
        testBook.setTitle("title2");
        testBook.setAuthor("author1");
        testBook.setPrice(1.0);
    }

    @Test
    public void addBook() {
        Book book1 = bookRepository.save(testBook);
        assertThat(book1.getIsbn()).isEqualTo(testBook.getIsbn());
    }

    @Test
    public void addInventory() {
        BookInventory bookInventory = new BookInventory(testBook.getId(), testBook.getPrice(), 2);
        BookInventory bookInventory1 = bookInventoryRepository.save(bookInventory);
        assertThat(bookInventory1.getBookId()).isEqualTo(bookInventory.getBookId());
    }

    @Test
    public void searchBook() {
        bookRepository.save(testBook);
        Book book2 = bookRepository.findBookByIsbn(testBook.getIsbn());
        assertThat(book2.getIsbn()).isEqualTo(testBook.getIsbn());
    }

    @Test
    public void updateInventory() {
        BookInventory bookInventory = new BookInventory(testBook.getId(), testBook.getPrice(), 2);
        bookInventory.setId(UUID.randomUUID().toString());
        bookInventoryRepository.save(bookInventory);
        bookInventoryRepository.updateQuantity(testBook.getId(),1,new Timestamp(System.currentTimeMillis()));
        BookInventory bookInventory2 = bookInventoryRepository.findBookInventoryByBookId(testBook.getId());
        assertThat(bookInventory2.getQuantity()).isEqualTo(3);
    }

    @Test
    public void orderBook() {

        BookOrder bookOrder = new BookOrder();
        bookOrder.setBookId(testBook.getId());
        bookOrder.setPrice(testBook.getPrice());
        bookOrder.setStatus(ResponseMessages.SUCCESS);
        bookOrder.setTotalPrice(testBook.getPrice());
        bookOrder.setQuantity(1);
        BookOrder bookOrder1 = bookOrderRepository.save(bookOrder);
        assertThat(bookOrder1.getId()).isEqualTo(bookOrder.getId());

    }

}
