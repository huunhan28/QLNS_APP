package com.example.food.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.food.Domain.AddressShop;
import com.example.food.Domain.Response.ResponseObject;
import com.example.food.network.RetroInstance;
import com.example.food.network.repository.AddressShopRepository;
import com.example.food.network.repository.MapBoxRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AddressShopViewModel extends AndroidViewModel {

    AddressShopRepository repository;

    public AddressShopViewModel(@NonNull Application application) {
        super(application);
        repository = RetroInstance.getRetrofitClient(application).create(AddressShopRepository.class);
    }

    public Single<Response<ResponseObject<AddressShop>>> getAddressShopBySTT(int stt){
        return repository.getAddressShopBySTT(stt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
