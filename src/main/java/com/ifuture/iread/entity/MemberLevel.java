package com.ifuture.iread.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by maofn on 2017/3/23.
 */
@Entity
@Table(name = "member_level")
public class MemberLevel extends BaseEntity {

    /**
     * 会员等级名称
     */
    @NotBlank(message = "此字段不能为空！")
    private String memberLevelName;

    /**
     * 可同时借的最大数量
     */
    @Min(value = 0)
    private int maxBorrowNum;

    /**
     * 备注
     */
    private String remark;


    /**
     * 入会金额
     */
    @Min(value = 0)
    @NotNull(message = "此字段不能为空！")
    private BigDecimal amount;


    private String pinyin;


    private boolean enable = true;

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }

    public int getMaxBorrowNum() {
        return maxBorrowNum;
    }

    public void setMaxBorrowNum(int maxBorrowNum) {
        this.maxBorrowNum = maxBorrowNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
