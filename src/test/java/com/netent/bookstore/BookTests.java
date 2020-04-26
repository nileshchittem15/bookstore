package com.netent.bookstore;

import com.netent.bookstore.model.Book;
import com.netent.bookstore.model.BookInventory;
import com.netent.bookstore.model.BookOrder;
import com.netent.bookstore.model.request.SearchRequest;
import com.netent.bookstore.model.request.StockRequest;
import com.netent.bookstore.repository.BookInventoryRepository;
import com.netent.bookstore.repository.BookOrderRepository;
import com.netent.bookstore.repository.BookRepository;
import com.netent.bookstore.service.BookService;
import com.netent.bookstore.service.InventoryService;
import com.netent.bookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class BookTests {


    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookInventoryRepository bookInventoryRepository;

    @Mock
    private BookOrderRepository bookOrderRepository;

    @InjectMocks
    private BookService bookService;

    @InjectMocks
    private InventoryService inventoryService;

    @InjectMocks
    private OrderService orderService;


    private static Book testBook;

    @BeforeEach
    public void setUpMock(){

        testBook = new Book();
        testBook.setId(UUID.randomUUID().toString());
        testBook.setIsbn("12345");
        testBook.setTitle("title2");
        testBook.setAuthor("author1");
        testBook.setPrice(1.0);

        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookRepository.findBookByIsbn(any(String.class))).thenReturn(testBook);


        BookInventory bookInventory = new BookInventory(testBook.getId(), testBook.getPrice(), 2);
        when(bookInventoryRepository.save(any(BookInventory.class))).thenReturn(bookInventory);
        when(bookInventoryRepository.findBookInventoryByBookId(any(String.class))).thenReturn(bookInventory);

        when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(new BookOrder());

    }

    @Test
    public void addBookTest(){
        Book book1 = bookService.addBook(testBook);
        assertEquals(book1.getIsbn(),testBook.getIsbn());
    }


    @Test
    public void addBookInventoryTest(){

        StockRequest stockRequest = new StockRequest(testBook.getId(),2);
        inventoryService.addBookStock(stockRequest);
    }

    @Test
    public void getBook(){

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setIsbn(testBook.getIsbn());

        List<Book> bookList = bookService.searchBook(searchRequest);
        assertEquals(bookList.get(0).getIsbn(), testBook.getIsbn());
    }


    @Test
    public void orderBook(){

        BookOrder bookOrder = new BookOrder();
        bookOrder.setQuantity(1);
        bookOrder.setBookId(testBook.getId());
        BookOrder bookOrder2 =orderService.orderBook(bookOrder);
        assertEquals(bookOrder2.getBookId(), testBook.getId());

    }





}
