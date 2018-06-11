package com.ifuture.iread.repository;

import com.ifuture.iread.entity.City;
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
public interface CityRepository extends PagingAndSortingRepository<City, String> {

    Page<City> findAll(Specification<City> spec, Pageable pageable);

    List<City> findAll();

    @Query("from City where cityName like %:cityName%")
    City findByCityName(@Param("cityName") String cityName);
}
