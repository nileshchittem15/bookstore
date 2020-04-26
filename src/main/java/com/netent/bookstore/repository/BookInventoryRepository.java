package com.netent.bookstore.repository;

import com.netent.bookstore.model.BookInventory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;

public interface BookInventoryRepository extends CrudRepository<BookInventory, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE BookInventory bookInventory SET bookInventory.quantity = bookInventory.quantity+ :quantity ,bookInventory.updatedAt=:updatedAt  where bookInventory.bookId =:bookId")
    void updateQuantity(@Param("bookId") String bookId, @Param("quantity") int quantity, @Param("updatedAt")
        Timestamp updatedAt);


    BookInventory findBookInventoryByBookId(String bookId);
}
