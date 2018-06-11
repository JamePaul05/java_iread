package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.Shop;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.BorrowRecordRepository;
import com.ifuture.iread.repository.CityRepository;
import com.ifuture.iread.repository.ShopRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IShopService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maofn on 2017/3/20.
 */
@Service
public class ShopServiceImpl implements IShopService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Override
    public DataTableReturnObject fetchShops(DataRequest dr) {
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
        //如果是名称排序
        if ("shopName".equals(orderField)) {
            orderField = "pinyin";
        }
        if ("cityName".equals(orderField)) {
            orderField = "city.pinyin";
        }
        //排序方向
        Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //空字段排最后
        Sort.Order order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        Page<Shop> page = null;
        final String search = dr.getSearch();
        if (StringUtil.isNotNull(search)){
            page = shopRepository.findAll(new Specification<Shop>() {
                @Override
                public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate like2 = cb.like(root.get("shopName").as(String.class), "%" + search + "%");
                    Predicate like3 = cb.like(root.get("city").get("cityName").as(String.class), "%" + search + "%");
                    Predicate like4 = cb.like(root.get("pinyin").as(String.class), "%" + search + "%");
                    Predicate predicate = cb.or(like2, like3, like4);
                    return predicate;
                }
            }, pageable);
        } else {
            page = shopRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(Shop shop : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", shop.getId());
            j.put("shopName", shop.getShopName());
            j.put("cityName", shop.getCity().getCityName());
            list.add(j);
        }
        dro.setRecordsTotal(shopRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public void saveOrUpdate(Shop shop) {
        try {
            shop.setPinyin(PinyinUtil.hanyuConvertToPinyin(shop.getShopName()));
            shop.setCity(cityRepository.findOne(shop.getCityID()));
            User createrOrUpdater = SecurityUtil.getUser(userRepository);

            if (StringUtil.isNotNull(shop.getId())) {
                Shop old = shopRepository.findOne(shop.getId());
                shop.setCreatedBy(old.getCreatedBy());
                shop.setCreateDate(old.getCreateDate());
            } else {
                shop.setId(UUIDUtil.generateUUID());
                shop.setCreatedBy(createrOrUpdater);
                shop.setCreateDate(new Date());
            }
            shop.setUpdateDate(new Date());
            shop.setUpdatedBy(createrOrUpdater);
            shopRepository.save(shop);
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
                        shopRepository.delete(idList[i]);
                        borrowRecordRepository.deleteByShopId(idList[i]);
                    }
                }else{
                    shopRepository.delete(ids);
                    borrowRecordRepository.deleteByShopId(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Shop findOneById(String id) {
        return shopRepository.findOne(id);
    }

    @Override
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    @Override
    public List<Shop> findAllByShopName(String shopName) {
        StringBuilder sb = new StringBuilder("select * from shop where  shopName like CONCAT('%', :shopName, '%')");
        Query query = em.createNativeQuery(sb.toString(), Shop.class);
        query.setParameter("shopName", shopName);
        return query.getResultList();
    }

    @Override
    public List<Shop> findByCity(String cityID) {
        List<Shop> shops = new ArrayList<>();
        if (StringUtil.isNotNull(cityID)) {
            shops = shopRepository.findByCity(cityID);
        }
        return shops;
    }

    @Override
    public List<Shop> findByCityName(String cityName) {
        List<Shop> shops = shopRepository.findByCityName(cityName);
        if (shops == null) {
            shops = new ArrayList<>();
        }
        return shops;
    }

    /**
     * 获取最近的公益点名称
     * @param lat
     * @param lng
     * @return
     */
    @Override
    public Shop getNearestShop(String lat, String lng) {
        List<Shop> shops = shopRepository.findAll();
        Shop nearestShop = null;
        BigDecimal nearest = null;
        BigDecimal lat1 = new BigDecimal(lat);
        BigDecimal lng1 = new BigDecimal(lng);
        for (Shop shop : shops) {
            BigDecimal shopLat = new BigDecimal(shop.getLatitude());
            BigDecimal shopLng = new BigDecimal(shop.getLongitude());
            BigDecimal distanceQuadratic = getDistanceQuadratic(lat1, lng1, shopLat, shopLng);
            if (nearest != null) {
                if (distanceQuadratic.compareTo(nearest) < 0) {
                    nearest = distanceQuadratic;
                    nearestShop = shop;
                }
            } else {
                nearest = distanceQuadratic;
                nearestShop = shop;
            }
        }
        return nearestShop;
    }

    /**
     * 获取两点之间距离的平方
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    private BigDecimal getDistanceQuadratic(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        BigDecimal edge1 = lat2.subtract(lat1).abs();
        BigDecimal edge2 = lng2.subtract(lng1).abs();
        return edge1.multiply(edge1).add(edge2.multiply(edge2));
    }
}
