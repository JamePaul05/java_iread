package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.RoleRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IRoleService;
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
 * Created by maofn on 2017/3/19.
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void saveOrUpdate(Role role) {
        try {
            role.setPinyin(PinyinUtil.hanyuConvertToPinyin(role.getRoleName()));
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            if (StringUtil.isNotNull(role.getId())) {
                Role old = roleRepository.findOne(role.getId());
                role.setMenus(old.getMenus());
                role.setCreatedBy(old.getCreatedBy());
                role.setCreateDate(old.getCreateDate());
            } else {
                role.setId(UUIDUtil.generateUUID());
                role.setCreatedBy(createrOrUpdater);
                role.setCreateDate(new Date());
            }
            role.setUpdateDate(new Date());
            role.setUpdatedBy(createrOrUpdater);
            roleRepository.save(role);
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
                        roleRepository.delete(idList[i]);
                    }
                }else{
                    roleRepository.delete(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Role findOneById(String id) {
        Role role = null;
        if (StringUtil.isNotNull(id)) {
            role = roleRepository.findOne(id);
        }
        return role;
        
    }

    @Override
    public DataTableReturnObject fetchRoles(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        Pageable pageable = DataTableUtil.convertToPage(dr, Role.class);
        Page<Role> page = null;
        final String search = dr.getSearch();
        if (StringUtil.isNotNull(search)){
            page = roleRepository.findAll(new Specification<Role>() {
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like2 = cb.like(root.get("roleName").as(String.class), "%" + search + "%");
                    Predicate like1 = cb.like(root.get("code").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like1, like2);
                    return predicate;
                }
            }, pageable);
        } else {
            page = roleRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(Role role : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", role.getId());
            j.put("roleName", role.getRoleName());
            j.put("code", role.getCode());
            list.add(j);
        }
        dro.setRecordsTotal(roleRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public Role findOneByCode(String code) {
        return roleRepository.findOneByCode(code);
    }
}
