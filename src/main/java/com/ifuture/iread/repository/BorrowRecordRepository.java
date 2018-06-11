package com.ifuture.iread.repository;

import com.ifuture.iread.entity.BorrowRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by maofn on 2017/4/2.
 */
@Repository
public interface BorrowRecordRepository extends CrudRepository<BorrowRecord, String> {

    @Transactional
    @Modifying
    @Query(" delete from BorrowRecord where bookletId = :bookletId")
    void deleteByBookletId(@Param("bookletId") String bookletId);

    @Transactional
    @Modifying
    @Query("delete from BorrowRecord where borrowerId = :borrowerId")
    void deleteByBorrowerId(@Param("borrowerId") String borrowerId);

    @Transactional
    @Modifying
    @Query("delete from BorrowRecord where borrowShopId = :shopId or returnShopId = :shopId")
    void deleteByShopId(@Param("shopId") String shopId);


    @Query("from BorrowRecord where borrowerId = :borrowerId order by borrowTime desc")
    List<BorrowRecord> findAllByBorrowerId(@Param("borrowerId") String borrowerId);

    @Query("from BorrowRecord where bookletId = :bookletId")
    List<BorrowRecord> findAllByBookletId(@Param("bookletId") String bookletId);

    @Query("from BorrowRecord where bookletId = ?1 and borrowerId = ?2 and returnTime is null ")
    BorrowRecord findOneByBookletIdAndBorrowerId(String bookletId, String borrowerId);
}
