package com.example.food.feature.discount;

import androidx.lifecycle.ViewModel;

import com.example.food.Domain.Discount;
import com.example.food.network.RetroInstance;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DiscountViewModel extends ViewModel {

    private DiscountRepository discountRepository;
    public DiscountViewModel(){
        discountRepository = RetroInstance.getRetrofitClient().create(DiscountRepository.class);
    }


    public Observable<Response<List<Discount>>> getDiscounts(){
        return discountRepository.getDiscounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
