package com.develop.xdk.df.rechargemachine.entity;

/**
 * 查询订单状态参数
 */

public class QueryOrderParam extends BaseParam{
    private String ordernumber;

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }
}
