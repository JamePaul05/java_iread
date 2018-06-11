package com.ifuture.iread.service;

import com.ifuture.iread.entity.Role;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.List;

/**
 * Created by maofn on 2017/3/19.
 */
public interface IRoleService {
    List<Role> findAll();

    void saveOrUpdate(Role role);

    boolean remove(String ids);

    Role findOneById(String id);

    DataTableReturnObject fetchRoles(DataRequest dr);

    Role findOneByCode(String code);
}
