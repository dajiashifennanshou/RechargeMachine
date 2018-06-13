package com.develop.xdk.df.rechargemachine.controller;

import android.content.Context;
import android.util.Log;

import com.develop.xdk.df.rechargemachine.entity.BaseParam;
import com.develop.xdk.df.rechargemachine.entity.HttpResult;
import com.develop.xdk.df.rechargemachine.entity.MakeOrderParam;
import com.develop.xdk.df.rechargemachine.entity.MakeOrderResult;
import com.develop.xdk.df.rechargemachine.entity.OrderState;
import com.develop.xdk.df.rechargemachine.entity.QueryOrderParam;
import com.develop.xdk.df.rechargemachine.entity.WxUser;
import com.develop.xdk.df.rechargemachine.http.ApiException;
import com.develop.xdk.df.rechargemachine.http.HttpMethods;
import com.develop.xdk.df.rechargemachine.service.RechargeService;
import com.develop.xdk.df.rechargemachine.subscribers.ProgressSubscriber;
import com.develop.xdk.df.rechargemachine.subscribers.SubscriberOnNextListener;
import com.develop.xdk.df.rechargemachine.utils.BeanUtil;
import com.develop.xdk.df.rechargemachine.utils.C;
import com.develop.xdk.df.rechargemachine.utils.SharedPreferencesUtils;
import com.develop.xdk.df.rechargemachine.utils.SignUtil;

import java.util.SortedMap;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/11.
 */

public class RechargeController {
    private Retrofit retrofit;
    private RechargeService rechargeService;
    private RechargeController(){
        retrofit = HttpMethods.getInstance().getRetrofit();
        rechargeService = retrofit.create(RechargeService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RechargeController INSTANCE = new RechargeController();
    }

    //获取单例
    public static RechargeController getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     *   下单生成二维码
     */
    public void makeOrder(SubscriberOnNextListener onNextListener, Context context,String money,String cardid){
        ProgressSubscriber subscriber = new ProgressSubscriber(onNextListener, context);
        MakeOrderParam param = new MakeOrderParam();
        param.setMoney(money);
        param.setCardid(cardid);
        param.setClientid((String) SharedPreferencesUtils.getParam(context,C.CLIENTID_NAME,C.CLIENTID));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map,C.SIGN_KEY));
        Observable observable = rechargeService.makeOrder(param)
                .map(new HttpResultFunc<MakeOrderResult>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户信息
     * @param onNextListener
     * @param context
     * @param cardid
     */
    public void getUserinfo(SubscriberOnNextListener onNextListener, Context context,String cardid){
        ProgressSubscriber subscriber = new ProgressSubscriber(onNextListener, context);
        BaseParam param = new BaseParam();
        param.setCardid(cardid);
        param.setClientid((String) SharedPreferencesUtils.getParam(context,C.CLIENTID_NAME,C.CLIENTID));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map,C.SIGN_KEY));
        Observable observable = rechargeService.getUserInfo(param)
                .map(new HttpResultFunc<WxUser>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 查询订单状态
     * @param onNextListener
     * @param context
     * @param cardid
     */
    public void queryOrderState(SubscriberOnNextListener onNextListener, Context context,String cardid,String ordernumber){
        ProgressSubscriber subscriber = new ProgressSubscriber(onNextListener, context);
        QueryOrderParam param = new QueryOrderParam();
        param.setCardid(cardid);
        param.setClientid((String) SharedPreferencesUtils.getParam(context,C.CLIENTID_NAME,C.CLIENTID));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        param.setOrdernumber(ordernumber);
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map,C.SIGN_KEY));
        Observable observable = rechargeService.queryOrder(param)
                .map(new HttpResultFunc<OrderState>());
        toSubscribe(observable, subscriber);
    }

    private   <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.i("result", httpResult.toString());
            if (httpResult.getCode() == 0) {
                throw new ApiException(httpResult.getMsg());
            }
            return httpResult.getData();
        }
    }
}
