package com.ifuture.iread.service;

import com.ifuture.iread.entity.CheckCode;

import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */
public interface ICheckCodeService {
    CheckCode getCheckCode(String mobilePhone);

    List<CheckCode> findOneByMobileOrderBySentDate(String mobile);

    void save(String code, String mobile);

    CheckCode findByMobileAndCode(String mobile, String sendCode);

    void update(CheckCode checkCode);
}
