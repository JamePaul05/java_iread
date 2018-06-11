package com.ifuture.iread.service;

import com.ifuture.iread.entity.Book;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.List;

/**
 * Created by maofn on 2017/3/26.
 */
public interface IBookService {
    DataTableReturnObject fetchBooks(DataRequest dr);

    boolean remove(String ids);

    Book findOneById(String id);

    boolean create(Book book);

    boolean exist(String isbn);

    void update(Book book);

    List<Book> listBooksByShopID(String shopID, String bookName);

    List<Book> sortByBorrowTimes(List<Book> books, String shopId);

    Book findOneByIsbn(String s);
}
