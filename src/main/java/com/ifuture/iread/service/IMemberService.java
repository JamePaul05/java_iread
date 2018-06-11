package com.ifuture.iread.service;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.Date;

/**
 * Created by maofn on 2017/3/23.
 */
public interface IMemberService {
    DataTableReturnObject fetchMembers(DataRequest dr);

    Member saveOrUpdate(Member member);

    public void updateMember(Member member);

    boolean remove(String ids);

    Member findOneById(String id);

    Member findOneByWechatId(String openid);

    Member findOneByMobile(String mobile);

    public String getCurrentDateMaxPaymentNo(String paymentNo);

    public void updateMemberPaymentInfo(Date payTime, String paymentNo, String wechatId);
}
