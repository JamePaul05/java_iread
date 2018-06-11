package com.ifuture.iread.repository;

import com.ifuture.iread.entity.SessionRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by maofn on 2017/5/11.
 */
@Repository
public interface SessionRecordRepository extends CrudRepository<SessionRecord, String> {

    @Transactional
    @Modifying
    @Query("delete from SessionRecord s where s.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") String memberId);

    @Transactional
    @Modifying
    @Query("delete from SessionRecord s where s.user.id = :userId")
    void deleteByUserId(@Param("userId")String userId);
}
