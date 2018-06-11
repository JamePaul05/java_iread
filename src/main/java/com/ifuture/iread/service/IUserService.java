package com.ifuture.iread.service;

import com.ifuture.iread.entity.PasswordModel;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

/**
 * Created by maofn on 2017/3/15.
 */
public interface IUserService {
    DataTableReturnObject fetchUsers(DataRequest dr);

    void saveOrUpdate(User user);

    boolean remove(String ids);

    User findOneById(String id);

    public User getUserByUserName(String user);

    public String changePsd(PasswordModel passwordModel);
}
