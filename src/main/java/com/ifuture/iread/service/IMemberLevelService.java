package com.ifuture.iread.service;

import com.ifuture.iread.entity.MemberLevel;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.List;

/**
 * Created by maofn on 2017/3/23.
 */
public interface IMemberLevelService {
    DataTableReturnObject fetchMemberLevels(DataRequest dr);

    void saveOrUpdate(MemberLevel memberLevel);

    boolean remove(String ids);

    MemberLevel findOneById(String id);

    List<MemberLevel> findAll();

    public List<MemberLevel> findAllEnable();
}
