package com.ifuture.iread.service;

import com.ifuture.iread.entity.Booklet;
import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by maofn on 2017/3/27.
 */
public interface IBookletService {
    boolean save(String isbn, String start, String end);

    DataTableReturnObject fetchBooklets(DataRequest dr, HttpServletRequest request);

    boolean remove(String ids);

    Boolean allocate(String ids, String shopID);

    List<Booklet> listBookletsByShopID(String shopID);

    int borrowBook(String bianhao, Member borrower);


    int returnBook(String bianhao, User returner);

    Booklet findOneById(String bookletId);

    Booklet findOneByIndex(String bookletId);

    int countByShop(Shop shop);

    int countBorrowedByShop(Shop shop);
}
