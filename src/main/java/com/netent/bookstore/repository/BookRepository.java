package com.netent.bookstore.repository;

import com.netent.bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book findBookByIsbn(String isbn);

    List<Book> findBooksByAuthorContainingIgnoreCase(String author);

    List<Book> findBooksByTitleContainingIgnoreCase(String title);

    void deleteBookById(String id);

}
