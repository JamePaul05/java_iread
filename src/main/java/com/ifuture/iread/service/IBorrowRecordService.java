package com.ifuture.iread.service;

import com.ifuture.iread.entity.BorrowRecord;

import java.util.List;

/**
 * Created by maofn on 2017/4/3.
 */
public interface IBorrowRecordService {
    List<BorrowRecord> getAllRecordByOpenId(String openid);

    List<BorrowRecord> getRecordByBianhao(String bianhao);
}
