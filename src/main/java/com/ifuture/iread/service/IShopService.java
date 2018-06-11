package com.ifuture.iread.service;

import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.util.DataRequest;
import com.ifuture.iread.util.DataTableReturnObject;

import java.util.List;

/**
 * Created by maofn on 2017/3/20.
 */
public interface IShopService {
    DataTableReturnObject fetchShops(DataRequest dr);

    void saveOrUpdate(Shop shop);

    boolean remove(String ids);

    Shop findOneById(String id);

    List<Shop> findAll();

    List<Shop> findAllByShopName(String shopName);

    List<Shop> findByCity(String cityID);

    List<Shop> findByCityName(String cityName);

    Shop getNearestShop(String lat, String lng);
}
