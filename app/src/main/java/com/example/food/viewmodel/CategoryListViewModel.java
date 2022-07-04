package com.example.food.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



import com.example.food.feature.category.CategoryResponse;

import com.example.food.Domain.Category;
import com.example.food.network.repository.CategoryRepository;

import com.example.food.network.RetroInstance;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListViewModel extends ViewModel {



    private com.example.food.feature.category.CategoryRepository api;
    private Category categoryDomain;

    private MutableLiveData<List<Category>> categories;


    public CategoryListViewModel(){
        api = RetroInstance.getRetrofitClient().create(com.example.food.feature.category.CategoryRepository.class);
    }

    public MutableLiveData<List<Category>> getCategoriesObserver(){
        return categories;
    }


    public void makeApiCall(){
        CategoryRepository api = RetroInstance.getRetrofitClient().create(CategoryRepository.class);
        Call<List<Category>> call = api.getCategoryDomains();
        call.enqueue(new Callback<List<Category>>() {

            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                System.out.println("body:" + response.body());
                try{
                    categories.setValue(response.body());
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("size of categories" + categories.getValue().size());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                System.out.println("throw call api:" + t.getMessage());
                categories.postValue(null);
            }
        });
    }

    public Observable<Response<CategoryResponse>> callGetCategory(int id){
        return api.getCategoryById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
