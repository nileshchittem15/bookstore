package com.netent.bookstore.repository;

import com.netent.bookstore.model.BookOrder;
import org.springframework.data.repository.CrudRepository;

public interface BookOrderRepository extends CrudRepository<BookOrder, Long> {
}
