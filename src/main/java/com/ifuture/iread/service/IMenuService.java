package com.ifuture.iread.service;

import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by maofn on 2017/3/13.
 */
public interface IMenuService {

    Set<Menu> showMenus();

    DataTableReturnObject fetchMenus(DataRequest dr);

    void saveOrUpdate(Menu menu);

    boolean remove(String ids);

    Menu findOneById(String id);
}
