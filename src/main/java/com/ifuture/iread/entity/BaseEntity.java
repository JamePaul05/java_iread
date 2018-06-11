package com.ifuture.iread.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by maofn on 2017/2/23.
 * 用于其他实体继承的共同属性
 */
@MappedSuperclass
public class BaseEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     *  指定年月日时分秒类型的日期格式
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    /**
     *  多对一，当此类新增或变化时，不影响User
     *  字段createdBy为外键关联到customer表中的id字段
     */
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "createdBy", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "updatedBy", referencedColumnName = "id")
    private User updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }
}
