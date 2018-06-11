package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.CityRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.ICityService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.awt.SystemColor.menu;

/**
 * Created by maofn on 2017/3/20.
 */
@Service
public class CityServiceImpl implements ICityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataTableReturnObject fetchCities(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        Pageable pageable = DataTableUtil.convertToPage(dr, City.class);
        Page<City> page = null;
        final String search = dr.getSearch();
        if (StringUtil.isNotNull(search)){
            page = cityRepository.findAll(new Specification<City>() {
                @Override
                public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like2 = cb.like(root.get("cityName").as(String.class), "%" + search + "%");
                    Predicate like1 = cb.like(root.get("acronym").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like1, like2);
                    return predicate;
                }
            }, pageable);
        } else {
            page = cityRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(City city : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", city.getId());
            j.put("cityName", city.getCityName());
            j.put("acronym", city.getAcronym());
            list.add(j);
        }
        dro.setRecordsTotal(cityRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public void saveOrUpdate(City city) {
        city.setPinyin(PinyinUtil.hanyuConvertToPinyin(city.getCityName()));
        try {
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            if (StringUtil.isNotNull(city.getId())) {
                City old = cityRepository.findOne(city.getId());
                city.setCreatedBy(old.getCreatedBy());
                city.setCreateDate(old.getCreateDate());
            } else {
                city.setId(UUIDUtil.generateUUID());
                city.setCreatedBy(createrOrUpdater);
                city.setCreateDate(new Date());
            }
            city.setUpdateDate(new Date());
            city.setUpdatedBy(createrOrUpdater);
            cityRepository.save(city);
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
                        cityRepository.delete(idList[i]);
                    }
                }else{
                    cityRepository.delete(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public City findOneById(String id) {
        City city = null;
        if (StringUtil.isNotNull(id)) {
            city = cityRepository.findOne(id);
        }
        return city;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findByCityName(String cityName) {
        return cityRepository.findByCityName(cityName);
    }
}
