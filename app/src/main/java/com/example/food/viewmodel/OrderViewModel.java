package com.example.food.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.food.Domain.Comment;
import com.example.food.Domain.Order;
import com.example.food.Domain.Response.ResponseObject;
import com.example.food.network.RetroInstance;
import com.example.food.network.repository.CommentRepository;
import com.example.food.network.repository.OrderRepository;
import com.example.food.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class OrderViewModel extends AndroidViewModel {

    MutableLiveData<List<Order>> data;
    OrderRepository repository;
    CommentRepository commentRepository;
    Application application;
    MutableLiveData<Order> order;
    MutableLiveData<String> message;
    public OrderViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        data = new MutableLiveData<>();
        order = new MutableLiveData<>();
        message = new MutableLiveData<>();
        repository = RetroInstance.getRetrofitClient(application).create(OrderRepository.class);
        commentRepository = RetroInstance.getRetrofitClient(application).create(CommentRepository.class);
    }

    public MutableLiveData<List<Order>> getData() {
        return data;
    }

    public MutableLiveData<Order> getOrder() {
        return order;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void setData(MutableLiveData<List<Order>> data) {
        this.data = data;
    }

    @SuppressLint("CheckResult")
    public void callOrdersListByState(String state){
        repository.getOrdersByState(state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponseResponse -> {
                    if(orderResponseResponse.code()==200){
                        data.setValue(orderResponseResponse.body().getData());
                    }else{
                        Toast.makeText(application, orderResponseResponse.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> Toast.makeText(application, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("CheckResult")
    public void callOrdersByStateAndCreateAtBetween(String state, String startDate, String endDate){
        repository.getOrdersByStateAndCreateAtBetween(state, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponseResponse -> {
                    if(orderResponseResponse.code()==200){
                        data.setValue(orderResponseResponse.body().getData());
                    }else{
                        Toast.makeText(application, orderResponseResponse.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> Toast.makeText(application, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("CheckResult")
    public void callGetOrderById(int id){
        repository.getOrderById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        order.setValue(responseObjectResponse.body().getData());
                    }else{
                        message.setValue(AppUtils.getErrorMessage(responseObjectResponse.errorBody().string()));
                    }
                }, throwable -> message.setValue(throwable.getMessage()));
    }

    public void updateData(Order order){
        List<Order> orders = data.getValue();

        for(int i=0; i<orders.size(); i++){
            if(order.getId()==orders.get(i).getId()){
                orders.remove(i);
                orders.add(i, order);
                break;
            }
        }
        data.setValue(orders);

    }

    public void deleteData(Order order){
        List<Order> orders = new ArrayList<>(data.getValue());
        for(int i=0; i<orders.size(); i++){
            if(order.getId()==orders.get(i).getId()){
                orders.remove(i);
                break;
            }
        }
        data.setValue(orders);
    }
    @SuppressLint("CheckResult")
    public void callUpdateStateOrder(int id, String state){
        repository.updateStateOrder(id, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        order.setValue(responseObjectResponse.body().getData());
                        deleteData(responseObjectResponse.body().getData());
                    }else{
                        message.setValue(AppUtils.getErrorMessage(responseObjectResponse.errorBody().string()));
                    }
                }, throwable -> message.setValue(throwable.getMessage()));
    }
    public Single<Response<ResponseObject<Order>>> callUpdateStateOrder2(int id, String state){
        return repository.updateStateOrder(id, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<ResponseObject<Comment>>> callGetCommentOfOrder(int orderId) {
        return commentRepository.getCommentsOfOrder(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<ResponseObject<Order>>> callInsertCommentForOrder(int idOrder, int userId, int rating, String comment) {
        return repository.insertCommentOfOrder(idOrder, userId, rating, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
