package com.ifuture.iread.service;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.SessionRecord;
import com.ifuture.iread.entity.User;

/**
 * Created by maofn on 2017/5/11.
 */
public interface ISessionRecordService {
    SessionRecord findLastRecord(String openId);

    boolean save(SessionRecord sessionRecord);

    void deleteByMember(Member member);

    void deleteByUser(User user);
}
