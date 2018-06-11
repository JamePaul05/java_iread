package com.ifuture.iread.repository;

import com.ifuture.iread.entity.CheckCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/2/20.
 */
@Repository
public interface CheckCodeRepository extends CrudRepository<CheckCode, String> {

    @Query("from CheckCode where mobile = ?1 order by sentDate desc")
    List<CheckCode> findOneByMobileOrderBySentDate(String mobile);

    @Query("from CheckCode where mobile = ?1 and code = ?2")
    CheckCode findByMobileAndCode(String mobile, String sendCode);
}
