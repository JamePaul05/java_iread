package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by maofn on 2017/3/23.
 */
@Repository
public interface MemberRepository extends PagingAndSortingRepository<Member, String> {

    Page<Member> findAll(Specification<Member> spec, Pageable pageable);

    List<Member> findAll();

    @Query("from Member where wechatId = :openid")
    Member findOneByWechatId(@Param("openid") String openid);

    @Query("from Member where mobile = :mobile")
    Member findOneByMobile(@Param("mobile") String mobile);

    @Query("from Member where paymentNo like CONCAT( :paymentNo, '%') order by paymentNo desc")
    List<Member> getCurrentDateMaxPaymentNo(@Param("paymentNo") String paymentNo);

    @Transactional
    @Modifying
    @Query("update Member set payTime=:payTime, paymentNo=:paymentNo where wechatId=:wechatId")
    public void updateMemberPaymentInfo(@Param("payTime") Date payTime,
                                          @Param("paymentNo") String paymentNo,
                                          @Param("wechatId") String wechatId);
}
