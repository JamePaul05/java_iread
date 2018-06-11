package com.ifuture.iread.service;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.List;

/**
 * Created by maofn on 2017/3/20.
 */
public interface ICityService {
    DataTableReturnObject fetchCities(DataRequest dr);

    void saveOrUpdate(City city);

    boolean remove(String ids);

    City findOneById(String id);

    List<City> findAll();

    City findByCityName(String cityName);
}
