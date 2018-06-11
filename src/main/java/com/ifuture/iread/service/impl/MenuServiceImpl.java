package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.MenuRepository;
import com.ifuture.iread.repository.RoleRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IMenuService;
import com.ifuture.iread.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

/**
 * Created by maofn on 2017/3/13.
 */
@Service("menuService")
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 获取菜单，TreeSet自动排序
     * @return
     */
    @Override
    public Set<Menu> showMenus() {
        Set<Menu> returnMenus = new TreeSet<Menu>();
        User user = SecurityUtil.getUser(userRepository);
        for (Role role : user.getRoles()) {
            if (role.isEnable()) {
                for (Menu menu : role.getMenus()) {
                    if (menu.isEnable()) {
                        if (menu.getParent()!=null){
                            if (menu.getId()!=menu.getParent().getId()){
                                 menu.getParent().getChildren().add(menu);
                                menu.getParent().setChildren(menu.getParent().getChildren());
                            }
                        }
                        returnMenus.add(menu);
                    }
                }
            }
        }
        return returnMenus;
    }

    @Override
    public DataTableReturnObject fetchMenus(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        Page<Menu> page = null;
        String orderField = dr.getOrderField();
        final String search = dr.getSearch();
        String direction = dr.getDirection();
        int pageIndex = dr.getPage();
        int rows = dr.getRows();
        Sort sort = null;
        if(StringUtil.isNotNull(orderField)) {
            if ("menuName".equalsIgnoreCase(orderField)) {
                orderField = "pinyin";
            }
            sort = new Sort(DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC, orderField);
        }
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        if (StringUtil.isNotNull(search)){
            page = menuRepository.findAll(new Specification<Menu>() {
                @Override
                public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like1 = cb.like(root.get("menuName").as(String.class), "%" + search + "%");
                    Predicate like2 = cb.like(root.get("url").as(String.class), "%" + search + "%");
                    Predicate like3 = cb.like(root.get("code").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like1, like2, like3);
                    return predicate;
                }
            }, pageable);
        } else {
            page = menuRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        List<Menu> menus = page.getContent();
        for(Menu menu : menus) {
            JSONObject j = new JSONObject();
            j.put("id", menu.getId());
            j.put("menuName", menu.getMenuName());
            j.put("url", menu.getUrl());
            j.put("code", menu.getCode());
            JSONArray ja = new JSONArray();
            for (Role role : menu.getRoles()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roleName", role.getRoleName());
                ja.put(jsonObject);
            }
            j.put("roles", ja);
            list.add(j);
        }
        dro.setRecordsTotal(userRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public void saveOrUpdate(Menu menu) {
        String[] roleIDS = menu.getRoleIDS();
        List<Role> roles = new ArrayList<>();
        for(String roleID : roleIDS) {
            roles.add(roleRepository.findOne(roleID));
        }
        menu.setRoles(roles);
        menu.setPinyin(PinyinUtil.hanyuConvertToPinyin(menu.getMenuName()));
        try {
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            if (StringUtil.isNotNull(menu.getId())) {
                Menu old = menuRepository.findOne(menu.getId());
                menu.setCreatedBy(old.getCreatedBy());
                menu.setCreateDate(old.getCreateDate());
            } else {
                menu.setId(UUIDUtil.generateUUID());
                menu.setCreatedBy(createrOrUpdater);
                menu.setCreateDate(new Date());
            }
            menu.setUpdateDate(new Date());
            menu.setUpdatedBy(createrOrUpdater);
            menuRepository.save(menu);
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
                        menuRepository.delete(idList[i]);
                    }
                }else{
                    menuRepository.delete(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Menu findOneById(String id) {
        Menu menu = null;
        if (StringUtil.isNotNull(id)) {
            menu = menuRepository.findOne(id);
            List<Role> roles = menu.getRoles();
            String[] roleIDs = new String[roles.size()];
            for(Role role : roles) {
                int i = 0;
                roleIDs[i++] = role.getId();
            }
            menu.setRoleIDS(roleIDs);
        }
        return menu;
    }



    /*private void findAllChildren(Menu parent, Set<Menu> all, Set<Menu> returnMenus) {
        for (Menu temp : all) {
            if(temp.getParent().getId().equals(parent.getId())) {
                parent.getChildren().add(temp);
            }
        }
    }*/
}
