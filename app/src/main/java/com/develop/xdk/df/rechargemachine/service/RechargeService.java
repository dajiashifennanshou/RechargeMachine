package com.develop.xdk.df.rechargemachine.service;


import com.develop.xdk.df.rechargemachine.entity.BaseParam;
import com.develop.xdk.df.rechargemachine.entity.HttpResult;
import com.develop.xdk.df.rechargemachine.entity.MakeOrderParam;
import com.develop.xdk.df.rechargemachine.entity.MakeOrderResult;
import com.develop.xdk.df.rechargemachine.entity.OrderState;
import com.develop.xdk.df.rechargemachine.entity.QueryOrderParam;
import com.develop.xdk.df.rechargemachine.entity.WxUser;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 充值接口
 */
public interface RechargeService {
    //下单接口
    @POST("machinepay/makeorder")
    Observable<HttpResult<MakeOrderResult>> makeOrder(@Body MakeOrderParam param);

    //获取用户信息
    @POST("machinepay/user")
    Observable<HttpResult<WxUser>> getUserInfo(@Body BaseParam param);

    //查询订单状态
    @POST("machinepay/orderquery")
    Observable<HttpResult<OrderState>> queryOrder(@Body QueryOrderParam param);
}
