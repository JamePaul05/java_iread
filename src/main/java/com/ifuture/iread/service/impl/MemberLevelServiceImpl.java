package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.MemberLevel;
import com.ifuture.iread.entity.MemberLevel;
import com.ifuture.iread.entity.MemberLevel;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.MemberLevelRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IMemberLevelService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
public class MemberLevelServiceImpl implements IMemberLevelService {
    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataTableReturnObject fetchMemberLevels(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        Pageable pageable = DataTableUtil.convertToPage(dr, MemberLevel.class);
        Page<MemberLevel> page = null;
        final String search = dr.getSearch();
        if (StringUtil.isNotNull(search)){
            page = memberLevelRepository.findAll(new Specification<MemberLevel>() {
                @Override
                public Predicate toPredicate(Root<MemberLevel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like2 = cb.like(root.get("memberLevelName").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like2);
                    return predicate;
                }
            }, pageable);
        } else {
            page = memberLevelRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(MemberLevel memberLevel : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", memberLevel.getId());
            j.put("memberLevelName", memberLevel.getMemberLevelName());
            j.put("maxBorrowNum", memberLevel.getMaxBorrowNum());
            j.put("amount", memberLevel.getAmount());
            j.put("enable", memberLevel.isEnable() ? "启用" : "禁用");
            list.add(j);
        }
        dro.setRecordsTotal(memberLevelRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public void saveOrUpdate(MemberLevel memberLevel) {
        memberLevel.setPinyin(PinyinUtil.hanyuConvertToPinyin(memberLevel.getMemberLevelName()));
        try {
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            if (StringUtil.isNotNull(memberLevel.getId())) {
                MemberLevel old = memberLevelRepository.findOne(memberLevel.getId());
                memberLevel.setCreatedBy(old.getCreatedBy());
                memberLevel.setCreateDate(old.getCreateDate());
            } else {
                memberLevel.setId(UUIDUtil.generateUUID());
                memberLevel.setCreatedBy(createrOrUpdater);
                memberLevel.setCreateDate(new Date());
            }
            memberLevel.setUpdateDate(new Date());
            memberLevel.setUpdatedBy(createrOrUpdater);
            memberLevelRepository.save(memberLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean remove(String ids) {
        try {
            if (StringUtil.isNotNull(ids)) {
                if(ids.contains(",")){
                    String[] idList = ids.split(",");
                    for(int i = 0; i < idList.length; i++){
                        memberLevelRepository.delete(idList[i]);
                    }
                }else{
                    memberLevelRepository.delete(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public MemberLevel findOneById(String id) {
        return memberLevelRepository.findOne(id);
    }

    @Override
    public List<MemberLevel> findAll() {
        return memberLevelRepository.findAll();
    }

    @Override
    public List<MemberLevel> findAllEnable() {
        return memberLevelRepository.findAllEnable();
    }
}
