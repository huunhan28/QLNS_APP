package com.example.food.feature.map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.food.Api.Api;
import com.example.food.R;
import com.example.food.network.RetroInstance;
import com.example.food.util.AppUtils;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MapViewModel extends AndroidViewModel {

    MutableLiveData<String> titlePlace;
    MutableLiveData<Float> lat;
    MutableLiveData<Float> lng;
    MutableLiveData<String> message;
    MutableLiveData<Boolean> clickLocation;
    public MutableLiveData<Double> distance;
    int count = 0;


    public MapViewModel(@NonNull Application application) {
        super(application);
        titlePlace = new MutableLiveData<>();
        lat = new MutableLiveData<>();
        lng = new MutableLiveData<>();
        message = new MutableLiveData<>();
        clickLocation = new MutableLiveData<>();
        distance = new MutableLiveData<>();

    }

    public MutableLiveData<String> getTitlePlace() {
        return titlePlace;
    }

    public MutableLiveData<Float> getLat() {
        return lat;
    }

    public MutableLiveData<Float> getLng() {
        return lng;
    }
    public MutableLiveData<Boolean> getClickLocation(){return clickLocation;}

    @SuppressLint("CheckResult")
    public void callGetPlaceFromGeocode(String at, String lang, String apikey){
        Api.getRetrofit(
                "https://revgeocode.search.hereapi.com").create(MapRepository.class)
                .getPlaceFromGeocode(at, lang, apikey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(responsePlaceResponse -> {
            if(responsePlaceResponse.code()==200){
                titlePlace.setValue(responsePlaceResponse.body().items.get(0).title);
                if(count!=0) clickLocation.setValue(true);
                count++;
            }else {
                Log.d("AAA", responsePlaceResponse.errorBody().string());
//                message.setValue();
            }
        }, throwable -> message.setValue(AppUtils.getErrorMessage(throwable.getMessage())));
    }

    @SuppressLint("CheckResult")
    public void callGetDistanceFromTwoPlace(double lon1, double lat1, double lon2, double lat2, String apiKey){
        Api.getRetrofit(
                        "https://api.mapbox.com/").create(MapRepository.class)
                .getDistance(lon1, lat1, lon2, lat2, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response.code()==200){
                        if(response.body().routes.size()>0){
                            distance.postValue(response.body().routes.get(0).distance);
                        }
                    }else {
                        distance.postValue(0.0);
                        Log.d("HIEN", response.errorBody().string());
                    }
                }, throwable -> message.setValue(AppUtils.getErrorMessage(throwable.getMessage())));
    }

    @SuppressLint("CheckResult")
    public Single<Response<ResponsePlace>> callGetPlaceFromGeocode2(String at, String lang, String apikey){
        return Api.getRetrofit(
                "https://revgeocode.search.hereapi.com").create(MapRepository.class)
                .getPlaceFromGeocode(at, lang, apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @SuppressLint("CheckResult")
    public void callGetGeoCodeFromPlace(String q,  String apikey){
        Api.getRetrofit(
                "https://geocode.search.hereapi.com").create(MapRepository.class)
                .getGeoCode(q, apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePlaceResponse -> {
                    if(responsePlaceResponse.code()==200){
                        lat.setValue(responsePlaceResponse.body().items.get(0).position.lat);
                        lng.setValue(responsePlaceResponse.body().items.get(0).position.lng);
                    }else {
                        message.setValue(AppUtils.getErrorMessage(responsePlaceResponse.errorBody().string()));
                    }
                }, throwable -> message.setValue(AppUtils.getErrorMessage(throwable.getMessage())));
    }

    @SuppressLint("CheckResult")
    public  Single<Response<ResponsePlace>>  callGetGeoCodeFromPlace2(String q,  String apikey){
        return Api.getRetrofit(
                "https://geocode.search.hereapi.com").create(MapRepository.class)
                .getGeoCode(q, apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
