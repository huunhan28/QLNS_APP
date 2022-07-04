package com.example.food.firebase;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.food.Api.Api;

import java.lang.invoke.MutableCallSite;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudMessageViewModel extends AndroidViewModel {

    public static final String TAG = CloudMessageViewModel.class.getName();
    MutableLiveData<String> message;

    public CloudMessageViewModel(@NonNull Application application) {
        super(application);
        message = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    public void callSendMessageToCloud(String auth, CloudMessageBody body){
        Api.getRetrofit("https://fcm.googleapis.com")
                .create(CloudMessageRepository.class)
                .sendToCloudMessage(auth, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        Log.d(TAG, "send message to cloud success");
                        message.setValue("Send message to cloud success");
                    }else{
                        Log.e(TAG, "send message to cloud failed " + responseObjectResponse.code());
                        message.setValue("Send message to cloud failed " + responseObjectResponse.code());
                    }
                }, throwable -> {
                    Log.e(TAG, throwable.getMessage());
                    message.setValue(throwable.getMessage());
                });
    }
}
