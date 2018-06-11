package com.ifuture.iread.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by maofn on 2017/3/17.
 * 城市实体类
 */
@Entity
@Table(name = "city")
public class City extends BaseEntity {
    /**
     * 城市名称
     */
    @NotBlank(message = "此字段不能为空！")
    @Column(name = "city_name")
    private String cityName;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 首字母缩写
     */
    @NotBlank(message = "此字段不能为空！")
    private String acronym;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
