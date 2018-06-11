package com.ifuture.iread.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by maofn on 2017/3/17.
 * 公益借阅点
 */
@Entity
@Table(name = "shop")
public class Shop extends BaseEntity {

    @NotBlank(message = "此字段不能为空！")
    private String shopName;

    private String address;

    private String pinyin;

    @NotBlank(message = "此字段不能为空！")
    private String longitude;   //经度

    @NotBlank(message = "此字段不能为空！")
    private String latitude;    //纬度

    @Transient
    private String cityID;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "city_id")
    private City city;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
