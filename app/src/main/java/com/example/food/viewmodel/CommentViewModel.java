package com.example.food.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.food.Domain.Comment;
import com.example.food.network.RetroInstance;
import com.example.food.network.repository.CommentRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CommentViewModel extends AndroidViewModel {

    private CommentRepository commentRepository;
    private MutableLiveData<List<Comment>> comments;
    private MutableLiveData<Boolean> saveCommentSuccess;
    private MutableLiveData<Comment> commentOfOrder;


    public CommentViewModel(Application application) {
        super(application);
        this.commentRepository = RetroInstance.getRetrofitClient().create(CommentRepository.class);
        comments = new MutableLiveData<>();
        commentOfOrder = new MutableLiveData<>();
        saveCommentSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<List<Comment>> getComments() {
        return comments;
    }

    @SuppressLint("CheckResult")
    public Single<Response<List<Comment>>> callGetCommentOfProudct(int productId) {
        return commentRepository.getCommentsOfProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void callGetCommentOfOrder(int orderId) {
        commentRepository.getCommentsOfOrder(orderId)
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        commentOfOrder.postValue(response.body().getData());
                    } else {
                        commentOfOrder.postValue(null);
                    }
                });
    }



    @SuppressLint("CheckResult")
    public void callSaveComment(Comment comment) {
        commentRepository.saveComment(comment)
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
            if (response.isSuccessful()) {
                saveCommentSuccess.postValue(true);
            } else {
                comments.postValue(null);
            }
        });
    }
}
