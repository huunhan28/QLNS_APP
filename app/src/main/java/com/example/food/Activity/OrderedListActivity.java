package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food.Adapter.OrderListAdapter;
import com.example.food.Api.Api;
import com.example.food.Domain.Order;
import com.example.food.network.Listener.OrdersResponseListener;
import com.example.food.R;
import com.example.food.databinding.ActivityOrderedListBinding;
import com.example.food.feature.adminhome.OrderDetailsFragment;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.OrderViewModel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class OrderedListActivity extends AppCompatActivity implements OrderListAdapter.ClickOrderedListener, OrderDetailsFragment.ClickButton {
    Api api;
    ActivityOrderedListBinding binding;
    OrderListAdapter adapter;
    OrderViewModel orderViewModel;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_list);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_ordered_list);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        binding.rvListOrdered.setHasFixedSize(true);
        binding.rvListOrdered.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OrderListAdapter(new ArrayList<>(), this);
        binding.rvListOrdered.setAdapter(adapter);
        api = new Api(this);
        User user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));

        alertDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();
        alertDialog.show();
        api.getOrdersByUser(ordersResponseListener,user.getId());
        binding.btnBackListOrdered.setOnClickListener(view -> navigationToHome(view));
    }

    private void navigationToHome(View view) {
        finish();
    }

    private  final OrdersResponseListener ordersResponseListener =new OrdersResponseListener() {
        @Override
        public void didFetch(ArrayList<Order> response, String message) {
            if(response!=null){
                alertDialog.dismiss();
                //response.get(0).getId();
                if(response.size()==0) binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else{
                    binding.textViewEmptyList.setVisibility(View.GONE);
                    adapter.setData(response);

                }


            }
        }

        @Override
        public void didError(String message) {

        }
    };

    @Override
    public void onItemClicked(Order order) {
        // SHOW DETAIL FRAGMENT
        OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(order, this);
        orderDetailsFragment.show(getSupportFragmentManager(), orderDetailsFragment.getTag());
    }

    @Override
    public void clickButtonAccept(int idOrder, String state) {
        // change huy order

     
    }

    @SuppressLint("CheckResult")
    @Override
    public void clickButtonAccept2(int idOrder, String state,int userId, int rating, String comment) {
        if(state.equals(AppUtils.orderState[0])){// chưa duyệt => hủy
            callUpdateState(idOrder, AppUtils.orderState[3]);
        }else if(state.equals(AppUtils.orderState[1])){ // đang giao => đã nhận hàng => đã giao
            callUpdateState(idOrder, AppUtils.orderState[2]);
        }else if(state.equals(AppUtils.orderState[2])){ // đã giao => bình luận => add comment to db
            orderViewModel.callInsertCommentForOrder(idOrder,userId, rating,  comment)
                    .subscribe(success -> {
                        if(success.isSuccessful() && success.body().getStatus().equalsIgnoreCase("Ok")){
                            adapter.changeItem(success.body().getData());
                            Log.d("HIEN", "insert comment for order success");
                        }else {
                            Log.d("HIEN", "insert comment for order failed" + success.body().getMessage());
                        }
                    }, error ->  Log.d("HIEN", "insert comment for order failed" +error.getLocalizedMessage()));
        }
    }



    @SuppressLint("CheckResult")
    private void callUpdateState(int orderId, String state){
        orderViewModel.callUpdateStateOrder2(orderId, state)
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        adapter.changeItem(responseObjectResponse.body().getData());
                    }else{
                        Log.d("HIEN", "call update state order failed " + responseObjectResponse.errorBody().string());
                    }
                });
    }
}