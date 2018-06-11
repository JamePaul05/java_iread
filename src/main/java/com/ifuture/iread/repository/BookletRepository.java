package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Booklet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maofn on 2017/3/27.
 */
@Repository
public interface BookletRepository extends PagingAndSortingRepository<Booklet, String> {

    Page<Booklet> findAll(Specification<Booklet> spec, Pageable pageable);

    List<Booklet> findAll();

    @Query("from Booklet where bianHao = :bianHao")
    Booklet findOneByIndex(@Param("bianHao") String bianHao);

    @Query("from Booklet where shop.id = :shopID")
    List<Booklet> findByShopID(@Param("shopID") String shopID);

    @Query("select count(*) from Booklet where book.id = ?1")
    int countBookletByBookId(String bookId);

    @Query("select count(*) from Booklet where shop.id = ?1")
    int countByShop(String shopId);

    @Query("select count(*) from Booklet where shop.id = ?1 and borrowed = true")
    int countBorrowedByShop(String shopId);
}
