package com.example.food.feature.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.food.Domain.Discount;
import com.example.food.Domain.Product;
import com.example.food.feature.category.CategoryDTO;
import com.example.food.feature.category.CategoryResponse;
import com.example.food.feature.category.CategoryRepository;
import com.example.food.feature.discount.DiscountRepository;
import com.example.food.feature.product.ProductRepository;
import com.example.food.network.RetroInstance;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;


    public HomeViewModel(Application application){
        super(application);
        categoryRepository = RetroInstance.getRetrofitClient(application).create(CategoryRepository.class);
        productRepository = RetroInstance.getRetrofitClient(application).create(ProductRepository.class);
        discountRepository = RetroInstance.getRetrofitClient(application).create(DiscountRepository.class);

    }

    public Observable<Response<List<CategoryDTO>>> getCategories(){
      return   categoryRepository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<List<Product>>> getTop10Products(){
        return productRepository.getTop10Products()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<Response<List<Product>>> getProducts(){
        return productRepository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<CategoryResponse>> getCategoryById(int id){
        return categoryRepository.getCategoryById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<List<Product>>> getProductsByCategoryId(int id){
        return productRepository.getProductsByCategoryId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<List<Discount>>> getDiscounts(){
        return discountRepository.getDiscounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
