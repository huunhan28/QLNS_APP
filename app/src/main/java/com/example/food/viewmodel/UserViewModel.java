package com.example.food.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.food.Domain.Response.ResponseObject;
import com.example.food.Domain.request.RequestChangePassword;
import com.example.food.dto.UserDTO;
import com.example.food.network.repository.UserRepository;
import com.example.food.Domain.RequestSignup;
import com.example.food.Domain.User;
import com.example.food.network.RetroInstance;
import com.example.food.util.AppUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    MutableLiveData<User> user;
    MutableLiveData<String> message;
    MutableLiveData<Boolean> existUser;
    MutableLiveData<String> otp;
    MutableLiveData<Boolean> success;

    public UserViewModel(Application application){
        super(application);
        userRepository = RetroInstance.getRetrofitClient(application).create(UserRepository.class);
        user = new MutableLiveData<>();
        message = new MutableLiveData<>();
        existUser = new MutableLiveData<>();
        otp = new MutableLiveData<>();
    }

    public Observable<Response<UserDTO>> makeApiCallSignIn(String username, String password){
       return userRepository.signin(new User(username, password))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public Observable<Response<UserDTO>> makeApiCallSignUp(String username,String phone,  String name, String password, String address){
        return userRepository.signup(new RequestSignup(username,phone, name, password,address))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Response<ResponseObject<User>>> apiUpdateUser(int id,RequestBody userRequestForUpdate, MultipartBody.Part file){
        return userRepository.updateUser(id,userRequestForUpdate, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Response<ResponseObject<User>>> apiUpdateUserNotImage(int id,RequestBody userRequestForUpdate){
        return userRepository.updateUserNotImage(id,userRequestForUpdate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public MutableLiveData<User> getuserMultable(){
        return user;
    }
    public MutableLiveData<String> getMessage(){
        return message;
    }
    public MutableLiveData<Boolean> getExistUser() {
        return existUser;
    }
    public MutableLiveData<String> getOTP() {
        return otp;
    }
    public void setExistUser(boolean existUser) {
        this.existUser.setValue(existUser);
    }
    @SuppressLint("CheckResult")
    public void getUser(int id){
       userRepository.getUserById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Response<UserDTO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(Response<UserDTO> userDTOResponse) {
                        if(userDTOResponse.code()==200){
                            Log.d("call api", "call user success");
                            user.setValue(userDTOResponse.body().getData());
                        }else {
                            try {
                                Log.d("error api", userDTOResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ResponseBody body = ((HttpException) e).response().errorBody();

                            Converter<ResponseBody, Error> errorConverter =
                                    RetroInstance.getRetrofitClient().responseBodyConverter(Error.class, new Annotation[0]);
                            // Convert the error body into our Error type.
                            try {
                                Error error = errorConverter.convert(body);
                                Log.i("","ERROR: " + error.getMessage());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Log.e("", "call get user by id id userviewModel" + e.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void callChangePassword(RequestChangePassword request){
        userRepository.changePassword(request, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200)
                    {
                        user.setValue(responseObjectResponse.body().getData());
                    }else{
//                        JSONObject jsonObject = new JSONObject(responseObjectResponse.errorBody().string());
//                        message.setValue(jsonObject.getString("message"));
                        message.setValue(responseObjectResponse.body().getMessage());
                    }
                },throwable -> message.setValue(throwable.getMessage()));
    }

    @SuppressLint("CheckResult")
    public Single<Response<ResponseObject<User>>> callChangePassword2(RequestChangePassword request){
        return userRepository.changePassword(request, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void callResetPassword(RequestChangePassword request){
        userRepository.changePassword(request, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200)
                    {
                        user.setValue(responseObjectResponse.body().getData());
                    }else{
//                        JSONObject jsonObject = new JSONObject(responseObjectResponse.errorBody().string());
//                        message.setValue(jsonObject.getString("message"));
                        message.setValue("Mật khẩu không đúng");
                    }
                },throwable -> message.setValue(throwable.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void checkUserByPhoneNumber(String phoneNumber){
     userRepository.checkUserByPhonenumber(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        existUser.setValue(true);
                    }else {
                        existUser.setValue(false);
                        message.setValue("Số điện thoại không đúng hoặc chưa đăng ký");
//                        message.setValue(AppUtils.getErrorMessage(
//                                responseObjectResponse.errorBody().string()));
                    }
                }, throwable -> message.setValue(throwable.getLocalizedMessage()))
                ;
    }

    @SuppressLint("CheckResult")
    public void sendOTPToPhoneNumber(String phonenumber){
        phonenumber = "+84" + phonenumber.substring(1, phonenumber.length());
        userRepository.sendSMS(phonenumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        otp.setValue(responseObjectResponse.body().getData());
                    }else{
                        message.setValue(
                                AppUtils.getErrorMessage(responseObjectResponse
                                        .errorBody().string()));
                    }
                }, throwable -> message.setValue(
                        throwable.getLocalizedMessage()
                ));
    }

    public boolean checkOTP(String otp) {
        if(this.otp!=null){
            return this.otp.getValue().equals(otp);
        }else return false;
    }

    @SuppressLint("CheckResult")
    public void callUpdateTokenFireBaseUser(int id, String token){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), token);
        userRepository.updateTokenFireBaseOfUser(id, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        message.setValue(responseObjectResponse.body().getMessage());
                    }else{
                        message.setValue(
                                AppUtils.getErrorMessage(responseObjectResponse
                                        .errorBody().string()));
                    }
                }, throwable -> message.setValue(
                        throwable.getLocalizedMessage()
                ));

    }
}
