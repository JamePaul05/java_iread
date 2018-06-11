package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.PasswordModel;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.RoleRepository;
import com.ifuture.iread.repository.SessionRecordRepository;
import com.ifuture.iread.repository.ShopRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IUserService;
import com.ifuture.iread.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maofn on 2017/3/15.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private SessionRecordRepository sessionRecordRepository;

    @Override
    public DataTableReturnObject fetchUsers(DataRequest dr) {
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
        if ("userName".equals(orderField)) {
            orderField = "pinyin";
        }
        if ("shopName".equals(orderField)) {
            orderField = "shop.pinyin";
        }
        //排序方向
        Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //空字段排最后
        Sort.Order order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        String shopName = null;
        User login = SecurityUtil.getUser(userRepository);
        //暂时用于解决公益点管理员只能看到本公益点用户的问题
        List<Role> roles = login.getRoles();
        if (roles != null) {
            if (roles.size() == 1) {
                if (!roles.get(0).getCode().equals("role_admin")) {
                    shopName = login.getShop().getShopName();
                }
            } else if (roles.size() == 2) {
                for (Role role : roles) {
                    if (role.getCode().equals("role_admin")) {
                        shopName = "";
                        break;
                    } else {
                        shopName = login.getShop().getShopName();
                    }
                }
            }
        }
        final String search = dr.getSearch();
        final String shopNameSearch = shopName;
        Page<User> page = null;
        page = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate p = cb.and();
                if (StringUtil.isNotNull(search)){
                    Predicate like1 = cb.like(root.get("userName").as(String.class), "%" + search +"%");
                    Predicate like2 = cb.like(root.get("nickName").as(String.class), "%" + search +"%");
                    Predicate like3 = cb.like(root.get("shop").get("shopName").as(String.class), "%" + search +"%");
                    p = cb.or(like1, like2, like3);
                }
                if (StringUtil.isNotNull(shopNameSearch)) {
                    Predicate shopNameP = cb.equal(root.get("shop").get("shopName").as(String.class), shopNameSearch);
                    p = cb.and(p, shopNameP);
                }
                return p;
            }
        }, pageable);
        List<JSONObject> list=new ArrayList<>();
        List<User> users = page.getContent();
        for(User user : users) {
            JSONObject j = new JSONObject();
            j.put("id", user.getId());
            j.put("userName", user.getUserName());
            j.put("nickName", user.getNickName());
            try {
                j.put("shopName", user.getShop().getShopName());
            } catch (NullPointerException e) {
                j.put("shopName", "无");
            }
            JSONArray ja = new JSONArray();
            for(Role role : user.getRoles()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roleName", role.getRoleName());
                ja.put(jsonObject);
            }
            j.put("roleNames", ja);
            list.add(j);
        }
        dro.setRecordsTotal(userRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public void saveOrUpdate(User user) {
        try {
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            //如果是更新操作
            if (StringUtil.isNotNull(user.getId())) {
                User old = userRepository.findOne(user.getId());
                user.setCreatedBy(old.getCreatedBy());
                user.setCreateDate(old.getCreateDate());
                //如果密码不是空，说明改了密码，更新密码
                if (StringUtil.isNotNull(user.getPassWord())) {
                    user.setPassWord(new Md5PasswordEncoder().encodePassword(user.getPassWord(), ""));
                } else {    //是空的话就设老密码
                    user.setPassWord(old.getPassWord());
                }
            } else {//如果是新增
                user.setId(UUIDUtil.generateUUID());
                user.setCreatedBy(createrOrUpdater);
                user.setCreateDate(new Date());
                user.setPassWord(new Md5PasswordEncoder().encodePassword(user.getPassWord(), ""));
            }

            String[] roleIDs = user.getRoleIDs();
            List<Role> roles = new ArrayList<>();
            if (roleIDs != null) {
                for (String roleID : roleIDs) {
                    roles.add(roleRepository.findOne(roleID));
                }
            }
            user.setRoles(roles);
            user.setShop(shopRepository.findOne(user.getShopID()));
            user.setPinyin(PinyinUtil.hanyuConvertToPinyin(user.getNickName()));

            user.setUpdateDate(new Date());
            user.setUpdatedBy(createrOrUpdater);
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean remove(String ids) {
        try {
            if (StringUtil.isNotNull(ids)) {
                if(ids.contains(",")){
                    String[] idList = ids.split(",");
                    for(int i = 0; i < idList.length; i++){
                        sessionRecordRepository.deleteByUserId(idList[i]);
                        userRepository.delete(idList[i]);
                    }
                }else{
                    sessionRecordRepository.deleteByUserId(ids);
                    userRepository.delete(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public User findOneById(String id) {
        return userRepository.findOne(id);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public String changePsd(PasswordModel passwordModel) {
        JSONObject j = new JSONObject();
        User user = this.getUser(userRepository);
        String psd = passwordModel.getPsd();
        String newpsd = passwordModel.getNewpsd();
        String newpsd1 = passwordModel.getNewpsd1();
        String md5Password = new Md5PasswordEncoder().encodePassword(
                psd, "");
        String oldPsd = user.getPassWord();
        if (md5Password.equalsIgnoreCase(oldPsd)) {
            if (newpsd.equalsIgnoreCase(newpsd1)) {
                String newMd5Password = new Md5PasswordEncoder().encodePassword(
                        newpsd, "");
                if (newMd5Password.equalsIgnoreCase(oldPsd)) {
                    j.put("msg", "新密码不能与旧密码一致！");
                    j.put("success", false);
                    j.put("type", "2");
                } else {
                    user.setPassWord(newMd5Password);
                    userRepository.save(user);
                    j.put("success", true);
                    j.put("msg", "修改成功！请重新登陆");
                    j.put("type", "1");
                }
            } else {
                j.put("msg", "两次新密码输入不一致！");
                j.put("success", false);
                j.put("type", "2");
            }
        } else {
            j.put("msg", "旧密码不正确！");
            j.put("success", false);
            j.put("type", "1");
        }

        return j.toString();
    }
}
