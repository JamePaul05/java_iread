package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by maofn on 2017/3/20.
 */
public interface ShopRepository extends PagingAndSortingRepository<Shop, String> {

    Page<Shop> findAll(Specification<Shop> spec, Pageable pageable);

    List<Shop> findAll();

    @Query("from Shop where city.id = :cityID")
    List<Shop> findByCity(@Param("cityID") String cityID);

    @Query("from Shop where city.cityName like %:cityName%")
    List<Shop> findByCityName(@Param("cityName") String cityName);
}
