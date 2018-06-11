package com.ifuture.iread.util;

import com.ifuture.iread.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by maofn on 2017/3/19.
 */
public class MyQueryUtil<T> {

    private static MyQueryUtil myQueryUtil;

    private MyQueryUtil (){}

    public static MyQueryUtil getMyQueryUtil() {
        if (myQueryUtil == null) {
            myQueryUtil = new MyQueryUtil();
        }
        return myQueryUtil;
    }

    /*private Page<T> find(BaseRepository repository, final Predicate predicate, DataRequest dr) {
        Page<T> page = null;
        String orderField = dr.getOrderField();
        String search = dr.getSearch();
        String direction = dr.getDirection();
        int pageIndex = dr.getPage();
        int rows = dr.getRows();
        Sort sort = null;
        if(StringUtil.isNotNull(orderField)) {
            sort = new Sort(DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC, orderField);
        }
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        if (StringUtil.isNotNull(search)){
            page = repository.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return predicate;
                }
            }, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page;
    }*/
}
