package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.*;
import com.ifuture.iread.entity.Member;
import com.ifuture.iread.repository.*;
import com.ifuture.iread.service.IMemberService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maofn on 2017/3/23.
 */
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private SessionRecordRepository sessionRecordRepository;
    
    @Override
    public DataTableReturnObject fetchMembers(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        String orderField = dr.getOrderField();
        String direction = dr.getDirection();
        int pageIndex = dr.getPage();
        int rows = dr.getRows();
        //如果是会员名称排序
        if ("memberName".equals(orderField)) {
            orderField = "pinyin";
        }
        //如果是会员等级名称排序
        if ("memberLevel".equals(orderField)) {
            orderField = "memberLevel.pinyin";
        }
        //如果是会员金额排序
        if ("amount".equals(orderField)) {
            orderField = "memberLevel.amount";
        }
        //排序方向
        Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //空字段排最后
        Sort.Order order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        Page<Member> page = null;
        final String search = dr.getSearch();
        if (StringUtil.isNotNull(search)){
            page = memberRepository.findAll(new Specification<Member>() {
                @Override
                public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like2 = cb.like(root.get("memberName").as(String.class), "%" + search + "%");
                    Predicate like1 = cb.like(root.get("mobile").as(String.class), "%" + search + "%");
                    Predicate like3 = cb.like(root.get("memberLevel").get("memberLevelName").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like1, like2, like3);
                    return predicate;
                }
            }, pageable);
        } else {
            page = memberRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(Member member : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", member.getId());
            j.put("memberName", member.getMemberName());
            j.put("sex", "1".equals(member.getSex()) ? "男" : "女");
            j.put("mobile", member.getMobile());
            j.put("memberLevel", member.getMemberLevel() == null ? "" : member.getMemberLevel().getMemberLevelName());
            j.put("amount", member.getMemberLevel() == null ? "" : member.getMemberLevel().getAmount());
            j.put("registerTime", DateUtil.format(member.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            j.put("payTime", DateUtil.format(member.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(j);
        }
        dro.setRecordsTotal(memberRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    @Transactional
    public Member saveOrUpdate(Member member) {
        try {
            Member old = null;
            if (StringUtil.isNotNull(member.getId())) {
                old = memberRepository.findOne(member.getId());
            }
            // 如果是更新操作
            if (old != null) {
                old.setMemberName(member.getMemberName());
                old.setSex(member.getSex());
                old.setMobile(member.getMobile());
                old.setIdNumber(member.getIdNumber());
                old.setEmail(member.getEmail());
                if (!StringUtils.isEmpty(member.getCityID())) {
                    City city = cityRepository.findOne(member.getCityID());
                    old.setCity(city);
                }
                if (!StringUtils.isEmpty(member.getMemberLevelID())) {
                    MemberLevel memberLevel = memberLevelRepository.findOne(member.getMemberLevelID());
                    old.setMemberLevel(memberLevel);
                }
                old.setAddress(member.getAddress());
                old.setEnable(member.isEnable());
                return memberRepository.save(old);
            } else {
                if (!StringUtils.isEmpty(member.getCityID())) {
                    City city = cityRepository.findOne(member.getCityID());
                    member.setCity(city);
                }
                if (!StringUtils.isEmpty(member.getMemberLevelID())) {
                    MemberLevel memberLevel = memberLevelRepository.findOne(member.getMemberLevelID());
                    member.setMemberLevel(memberLevel);
                }
                member.setPinyin(PinyinUtil.hanyuConvertToPinyin(member.getMemberName()));
                member.setId(UUIDUtil.generateUUID());
                member.setUpdateDate(new Date());
                member.setCreateDate(new Date());
                member.setRegisterTime(new Date());
                String password = member.getMemberPassword();
                member.setMemberPassword(new Md5PasswordEncoder().encodePassword(password, ""));
                return memberRepository.save(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean remove(String ids) {
        try {
            if (StringUtil.isNotNull(ids)) {
                if(ids.contains(",")){
                    String[] idList = ids.split(",");
                    for(int i = 0; i < idList.length; i++){
                        sessionRecordRepository.deleteByMemberId(idList[i]);
                        memberRepository.delete(idList[i]);
                        borrowRecordRepository.deleteByBorrowerId(idList[i]);
                    }
                }else{
                    sessionRecordRepository.deleteByMemberId(ids);
                    memberRepository.delete(ids);
                    borrowRecordRepository.deleteByBorrowerId(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Member findOneById(String id) {
        return memberRepository.findOne(id);
    }

    @Override
    public Member findOneByWechatId(String openid) {
        return memberRepository.findOneByWechatId(openid);
    }

    @Override
    public Member findOneByMobile(String mobile) {
        return memberRepository.findOneByMobile(mobile);
    }

    public synchronized String getCurrentDateMaxPaymentNo(String paymentNo) {
        List<Member> members = memberRepository.getCurrentDateMaxPaymentNo(paymentNo);
        if (members != null && members.size() > 0) {
            if (!StringUtil.isNotNull(members.get(0).getPaymentNo())) {
                return paymentNo + "0001";
            }
            Long longPaymentNo = Long.valueOf(members.get(0).getPaymentNo());
            return String.valueOf(longPaymentNo + 1);
        } else {
            return paymentNo + "0001";
        }
    }


    public void updateMemberPaymentInfo(Date payTime, String paymentNo, String wechatId) {
        memberRepository.updateMemberPaymentInfo(payTime, paymentNo, wechatId);
    }
}
