package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.Booklet;
import com.ifuture.iread.entity.BorrowRecord;
import com.ifuture.iread.entity.Member;
import com.ifuture.iread.repository.BookletRepository;
import com.ifuture.iread.repository.BorrowRecordRepository;
import com.ifuture.iread.repository.MemberRepository;
import com.ifuture.iread.service.IBorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by maofn on 2017/4/3.
 */
@Service
public class BorrowRecordServiceImpl implements IBorrowRecordService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookletRepository bookletRepository;

    @Override
    public List<BorrowRecord> getAllRecordByOpenId(String openid) {
        Member borrower = memberRepository.findOneByWechatId(openid);
        List<BorrowRecord> records = borrowRecordRepository.findAllByBorrowerId(borrower.getId());
        return records;
    }

    @Override
    public List<BorrowRecord> getRecordByBianhao(String bianhao) {
        Booklet booklet = bookletRepository.findOneByIndex(bianhao);
        List<BorrowRecord> records = borrowRecordRepository.findAllByBookletId(booklet.getId());
        return records;
    }
}
