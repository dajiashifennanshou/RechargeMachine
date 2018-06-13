package com.develop.xdk.df.rechargemachine.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.develop.xdk.df.rechargemachine.R;
import com.develop.xdk.df.rechargemachine.activity.Base.BaseActivity;
import com.develop.xdk.df.rechargemachine.controller.RechargeController;
import com.develop.xdk.df.rechargemachine.entity.MakeOrderResult;
import com.develop.xdk.df.rechargemachine.entity.OrderState;
import com.develop.xdk.df.rechargemachine.entity.WxUser;
import com.develop.xdk.df.rechargemachine.subscribers.SubscriberOnNextListener;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.result_TV)
    TextView resultTV;

    private SubscriberOnNextListener userInfoListener;
    private SubscriberOnNextListener makeOrderListener;
    private SubscriberOnNextListener queryOrderListener;

    @Override
    public void bindView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initData() {
        //下单
        makeOrderListener = new SubscriberOnNextListener<MakeOrderResult>() {
            @Override
            public void onNext(MakeOrderResult result) {
                Log.e("+++++++++++++++++++",new Gson().toJson(result).toString());
                resultTV.setText(new Gson().toJson(result).toString());
            }
        };
        //获取用户信息
        userInfoListener = new SubscriberOnNextListener<WxUser>() {
            @Override
            public void onNext(WxUser wxUser) {
                resultTV.setText(new Gson().toJson(wxUser).toString());
            }
        };
        //查询订单
        queryOrderListener = new SubscriberOnNextListener<OrderState>() {
            @Override
            public void onNext(OrderState orderState) {
                resultTV.setText(new Gson().toJson(orderState).toString());
            }
        };
    }

    @OnClick(R.id.main_userinfo)
    public void onuClick() {
        RechargeController.getInstance().getUserinfo(userInfoListener,this,"F5BCE228");
    }
    @OnClick(R.id.main_makeorder)
    public void onmClick() {
        RechargeController.getInstance().makeOrder(makeOrderListener,this,"0.01","F5BCE228");
    }
    @OnClick(R.id.click_query)
    public void onqClick() {
        RechargeController.getInstance().queryOrderState(queryOrderListener,this,"F5BCE228","1528776174090wVe7aNnY7wXb");
    }

}
