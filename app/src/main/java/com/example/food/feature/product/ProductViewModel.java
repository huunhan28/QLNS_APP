package com.example.food.feature.product;



import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.food.Domain.Product;
import com.example.food.Domain.ProductReport;
import com.example.food.Domain.Response.ProductReportResponse;
import com.example.food.network.RetroInstance;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {

   private ProductRepository productRepository;


   public ProductViewModel(Application application){
       super(application);
       productRepository = RetroInstance.getRetrofitClient().create(ProductRepository.class);
   }

    public Observable<Response<List<Product>>> getTop10Products(){
        return productRepository.getTop10Products()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<ProductReportResponse>> getProductRevenue(String startDate, String endDate, int limit, int offset){
        return productRepository.getProductRevenue(startDate, endDate, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
