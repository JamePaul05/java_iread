package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.SessionRecord;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.SessionRecordRepository;
import com.ifuture.iread.service.ISessionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by maofn on 2017/5/11.
 */
@Service
public class SessionRecordServiceImpl implements ISessionRecordService{

    @Autowired
    private SessionRecordRepository sessionRecordRepository;

    @Override
    public SessionRecord findLastRecord(String openId) {
        return sessionRecordRepository.findOne(openId);
    }

    @Override
    public boolean save(SessionRecord sessionRecord) {
        try {
            sessionRecordRepository.save(sessionRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void deleteByMember(Member member) {
        sessionRecordRepository.deleteByMemberId(member.getId());
    }

    @Override
    public void deleteByUser(User user) {
        sessionRecordRepository.deleteByUserId(user.getId());
    }
}
