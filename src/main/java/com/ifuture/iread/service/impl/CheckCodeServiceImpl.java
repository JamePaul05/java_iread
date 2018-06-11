package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.CheckCode;
import com.ifuture.iread.repository.CheckCodeRepository;
import com.ifuture.iread.service.ICheckCodeService;
import com.ifuture.iread.util.DateUtil;
import com.ifuture.iread.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */
@Service("checkCodeService")
public class CheckCodeServiceImpl implements ICheckCodeService {

    @Autowired
    private CheckCodeRepository checkCodeRepository;

    public CheckCode getCheckCode(String mobilePhone) {
        return checkCodeRepository.findOne(mobilePhone);
    }

    @Override
    public List<CheckCode> findOneByMobileOrderBySentDate(String mobile) {
        return checkCodeRepository.findOneByMobileOrderBySentDate(mobile);
    }

    @Override
    public void save(String code, String mobile) {
        CheckCode validationCode = new CheckCode();
        validationCode.setCode(code);
        validationCode.setId(UUIDUtil.generateUUID());
        validationCode.setMobile(mobile);
        Date sentDate = DateUtil.format(DateUtil.format(new Date()), "yyyy-MM-dd HH:mm:ss");
        validationCode.setSentDate(sentDate);
        //七分钟后失效
        Date expirationDate = DateUtil.format(DateUtil.format(new Date(sentDate.getTime() + 300000)), "yyyy-MM-dd HH:mm:ss");
        validationCode.setExpirationDate(expirationDate);
        checkCodeRepository.save(validationCode);
    }

    @Override
    public CheckCode findByMobileAndCode(String mobile, String sendCode) {
        return checkCodeRepository.findByMobileAndCode(mobile, sendCode);
    }

    @Override
    public void update(CheckCode checkCode) {
        checkCodeRepository.save(checkCode);
    }
}
