package com.example.food.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.food.Domain.Response.ResponseObject;
import com.example.food.Domain.User;
import com.example.food.network.RetroInstance;
import com.example.food.network.repository.UserRepository;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class AccountViewModel extends ViewModel {

    private UserRepository userRepository;


    public AccountViewModel(){
        userRepository = RetroInstance.getRetrofitClient().create(UserRepository.class);
    }

    public Observable<Response<ResponseObject<User>>> updateInforUser(int id, RequestBody userRequestForUpdate, MultipartBody.Part file){
        return userRepository.updateUser(id, userRequestForUpdate, file);
    }
}
