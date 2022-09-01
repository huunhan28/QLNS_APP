package com.example.food.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Adapter.CardListAdapter;
import com.example.food.Api.Api;
import com.example.food.Domain.AddressShop;
import com.example.food.Domain.Cart;
import com.example.food.Domain.LocationDomain;
import com.example.food.Domain.Product;
import com.example.food.Domain.Response.DiscountResponse;
import com.example.food.Domain.Response.OrderDetailResponse;
import com.example.food.Domain.Response.OrderResponse;
import com.example.food.feature.map.MapViewModel;
import com.example.food.network.Listener.CartResponseListener;
import com.example.food.network.Listener.DeleteCartResponseListener;
import com.example.food.network.Listener.DiscountResponseListener;
import com.example.food.network.Listener.InsertOrderDetailResponseListener;
import com.example.food.network.Listener.InsertOrderResponseListener;
import com.example.food.R;
import com.example.food.dto.CartForOrderDetail;
import com.example.food.dto.DiscountDTO;
import com.example.food.dto.OrdersDTO;
import com.example.food.Domain.User;
import com.example.food.network.repository.CartRepository;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.AddressShopViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.geojson.Point;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CartListActivity extends AppCompatActivity implements CardListAdapter.ChangNumberItemsListener,LocationListener {
    RecyclerView recycleViewCart;
    private CardListAdapter adapterCart;
    Api api;
    TextView ItemTotalFeeTxt, DeliveryFeeTxt,
            txtTotalOrder, btnCheckOut,
            txtValueDiscount, txtSaleOfCart,
            txtTotalItemTemp, txtDeliveryItemTemp,
            txtDeliveryInfo, txtPayment;
    TextView btn_add_discount;
    ArrayList<Cart> carts = new ArrayList<>();
    TextInputEditText edit_discount;
    DiscountDTO discountDTO = new DiscountDTO();
    ImageView btnBack;
    AlertDialog alertDialog;
    MapViewModel mapViewModel;
    AddressShopViewModel addressShopViewModel;
    double lon1=0.0, lat1=0.0, lon2=0.0, lat2=0.0;
    int feeDeliveryPerKm = 1000;
    int paymentType = 0;

    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 200; // 500 meters to update

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));
        setControl();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleViewCart.setLayoutManager(linearLayoutManager);
        api = new Api(CartListActivity.this);
        getMyLocation();
        loadData();
        setEvent();
//        setUpPusher();


    }

//    private void setUpPusher() {
//        PusherOptions options = new PusherOptions();
//        options.setCluster("ap1");
//
//        Pusher pusher = new Pusher("1988f25a6056e9b32057", options);
//
//        pusher.connect(new ConnectionEventListener() {
//            @Override
//            public void onConnectionStateChange(ConnectionStateChange change) {
//                Log.i("Pusher", "State changed from " + change.getPreviousState() +
//                        " to " + change.getCurrentState());
//            }
//
//            @Override
//            public void onError(String message, String code, Exception e) {
//                Log.i("Pusher", "There was a problem connecting! " +
//                        "\ncode: " + code +
//                        "\nmessage: " + message +
//                        "\nException: " + e
//                );
//            }
//        }, ConnectionState.ALL);
//
//        Channel channel = pusher.subscribe("my-channel");
//
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(PusherEvent event) {
//                Log.i("Pusher", "Received event with data: " + event.toString());
//            }
//        });
//
//    }

    @SuppressLint("CheckResult")
    private void loadData() {
        api.getCartsByUserId(cartResponseListener, Integer.parseInt(user.getId() + ""));
        addressShopViewModel.getAddressShopBySTT(1)
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        AddressShop addressShop = responseObjectResponse.body().getData();
                        lon1 = addressShop.getLongitude();
                        lat1 = addressShop.getLatitude();
                        if(lat2!=0 && lon2!=0){
                            Log.d("NHAN", "LOCATION:" + lon1 + "," + lat1 + "-" + lon2 + "," + lat2);

                            mapViewModel.callGetDistanceFromTwoPlace(lon1, lat1, lon2, lat2,getString(R.string.mapbox_access_token));
                        }

                    }
                });
//            double latitude1 = 10.390991;
//            double longitude1 = -254.268241;
//            double latitude2 = 10.395641;
//            double longitude2 = -254.273787;
//
//            float[] distance = new float[2];
//            Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, distance);
        LocationDomain locationDomain = AppUtils.getLocation(this);
        LocationDomain locationDomainShop = AppUtils.getLocationShop(this);
        float[] distance = new float[2];
            Location.distanceBetween(10.84778505, 106.78470671,
                    10.848354, 106.774406, distance);
            int feeTemp = (int) (distance[0] / 1000 * feeDeliveryPerKm);
        DeliveryFeeTxt.setText(feeTemp+"");
    }

    private void getMyLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Location not accept", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
    }

    private void setEvent() {
        mapViewModel.distance.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {

                Log.d("NHAN", "DISTANCE:" + aDouble);
                float feeDelivery = (int)(aDouble/1000) * feeDeliveryPerKm;
                txtDeliveryInfo.setText("Phí vận chuyển: " + feeDelivery/1000 + " km");
                DeliveryFeeTxt.setText(AppUtils.formatCurrency(feeDelivery));
                txtDeliveryItemTemp.setText(feeDelivery + "");
                txtTotalOrder.setText(AppUtils.formatCurrency(
                        feeDelivery + Float.parseFloat(txtTotalItemTemp.getText().toString())));
            }
        });
        adapterCart = new CardListAdapter(new ArrayList<>(), CartListActivity.this, this);
        recycleViewCart.setAdapter(adapterCart);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date myDate = new Date();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(myDate);
                Log.d("date", date);
                System.out.println(date + "date");
                user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));
//                Order order = new Order(0,user,null,null,"Chua duyet");
                if(user!=null) {
                    OrdersDTO ordersDTO = new OrdersDTO(user.getId(), date, discountDTO.getId(), AppUtils.orderState[0]);// chưa duyệt
                    System.out.println(ordersDTO.toString());
//
                    if(paymentType==0){
                        alertDialog.show();
                        api.insertOrder2(insertOrderResponseListener, ordersDTO);
                    }else{
                        // navigate to vnpay
                        startActivity(new Intent(CartListActivity.this, VnPayActivity.class));
                    }

                }




            }
        });

        btn_add_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idDiscount = edit_discount.getText().toString().trim();
                api.getDiscountById(discountResponseListener, idDiscount);
            }
        });
        btnBack.setOnClickListener(view -> onBackPressed());

        txtPayment.setOnClickListener(view -> clickPayment(view));
    }

    private void clickPayment(View view) {
        showDialogPayment(view);
    }

    private void showDialogPayment(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set dialog to bottom of screen
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        // set view for dialog to show

        dialog.setContentView(R.layout.dialog_payment_type);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );





        // set view
        TextView btnCloseDialog = dialog.findViewById(R.id.text_view_close_dialog);
        RadioButton rdbCash = dialog.findViewById(R.id.rdb_cash);
        RadioButton rdbVnPay = dialog.findViewById(R.id.rdb_vn_pay);

        // set event
        if(paymentType==0){
            rdbCash.setChecked(true);
        }else{
            rdbVnPay.setChecked(true);
        }
        rdbCash.setOnClickListener(view1 -> {
            txtPayment.setText("Tiền mặt");
            paymentType = 0;
        });

        rdbVnPay.setOnClickListener(view1 -> {
            txtPayment.setText("VNPay");
            paymentType = 1;
        });
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void setControl() {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        addressShopViewModel = new ViewModelProvider(this).get(AddressShopViewModel.class);
        alertDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();

        txtTotalItemTemp = findViewById(R.id.text_view_total_item_temp);
        txtDeliveryItemTemp = findViewById(R.id.DeliveryFeeTxt);
        btnBack = findViewById(R.id.backBtn_your_cart);
        btn_add_discount = findViewById(R.id.btn_add_discount);
        edit_discount = findViewById(R.id.edit_discount);
        recycleViewCart = findViewById(R.id.recycleViewCart);
        ItemTotalFeeTxt = findViewById(R.id.ItemTotalFeeTxt);
        DeliveryFeeTxt = findViewById(R.id.DeliveryFeeTxt);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        txtValueDiscount = findViewById(R.id.txtValueDiscount);
        txtTotalOrder = findViewById(R.id.text_view_total_order_carts);
        txtSaleOfCart = findViewById(R.id.text_view_sale_off_cart);
        txtDeliveryInfo = findViewById(R.id.txtDeliveryInfo);
        txtPayment = findViewById(R.id.text_view_payment);

    }

    private final DeleteCartResponseListener deleteCartResponseListener = new DeleteCartResponseListener() {
        @Override
        public void didFetch(String response, String message) {
//            Toast.makeText(CartListActivity.this, "Call api delete cart success"+message.toString(),Toast.LENGTH_SHORT).show();

            Log.d("success", message.toString());
        }

        @Override
        public void didError(String message) {
//            Toast.makeText(CartListActivity.this,"Call api delete cart error"+message.toString(),Toast.LENGTH_SHORT).show();

            Log.d("zzz", message.toString());
        }
    };
    private final InsertOrderResponseListener insertOrderResponseListener = new InsertOrderResponseListener() {
        @SuppressLint("CheckResult")
        @Override
        public void didFetch(OrderResponse response, String message) {
//            Toast.makeText(CartListActivity.this, "Call api insert order success" + message.toString(), Toast.LENGTH_SHORT).show();
            Log.d("success", message.toString());
            if(response.getStatus().equalsIgnoreCase("OK")) {
                alertDialog.dismiss();
                api.deleteCartByUserId(deleteCartResponseListener, Integer.parseInt(user.getId() + ""));
                alertDialog.dismiss();
                AppUtils.showSuccessDialog(CartListActivity.this,
                        "Đặt hàng thành công");
            }else{
                alertDialog.dismiss();
                AppUtils.showSuccessDialog(CartListActivity.this,
                        "Đặt hàng thất bại");
                Toast.makeText(CartListActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void didError(String message) {
//            Toast.makeText(CartListActivity.this,"Call api insert order error"+message.toString(),Toast.LENGTH_SHORT).show();
            Log.d("API", message.toString());
        }
    };
    private final InsertOrderDetailResponseListener insertOrderDetailResponseListener = new InsertOrderDetailResponseListener() {
        @Override
        public void didFetch(OrderDetailResponse response, String message) {
//            Toast.makeText(CartListActivity.this, "Call api insert order detail success"+message.toString(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void didError(String message) {
//            Toast.makeText(CartListActivity.this,"Call api insert order detail error"+message.toString(),Toast.LENGTH_SHORT).show();
            Log.d("zzz", message.toString());
        }
    };

    private final DiscountResponseListener discountResponseListener = new DiscountResponseListener() {

        @Override
        public void didFetch(DiscountResponse response, String message) {
//            Toast.makeText(CartListActivity.this, "Call api discount success"+message.toString(),Toast.LENGTH_SHORT).show();
            discountDTO = response.getData();
            Date today = new Date();
            if (edit_discount.getText().toString().trim() == "") {
                txtValueDiscount.setText("0");
                return;
            }
            if (discountDTO.getStartDate().before(today) && discountDTO.getEndDate().after(today)) {
                txtValueDiscount.setVisibility(View.VISIBLE);
                txtSaleOfCart.setText("Giảm giá -  ");
                txtValueDiscount.setText((int) (discountDTO.getPercent() * 100) + "");
                Float totalItem = Float.parseFloat(txtTotalItemTemp.getText().toString());
                txtTotalOrder.setText(totalItem * (1 - discountDTO.getPercent()) + "");
                return;
            }else{
                txtSaleOfCart.setText("Mã giảm giá quá hạn -");
            }

            if(discountDTO.getQuantity()<0){
                txtSaleOfCart.setText("Hết lượt sử dụng -");
            }

            txtValueDiscount.setText("0");
            return;


        }

        @Override
        public void didError(String message) {
//            Toast.makeText(CartListActivity.this,"Call api discount error"+message.toString(),Toast.LENGTH_SHORT).show();
            if (edit_discount.getText().toString().trim() == "") {
                txtValueDiscount.setText("0");
                return;
            }
//            txtValueDiscount.setText("This discount no available");
            return;
        }
    };

    private final CartResponseListener cartResponseListener = new CartResponseListener() {
        @Override
        public void didFetch(ArrayList<Cart> response, String message) {
            if (response != null) {
                if (response.size() == 0) btnCheckOut.setEnabled(false);
                else btnCheckOut.setEnabled(true);
                adapterCart.setValue(response);
                float itemTotalFee = 0;
                int deliveryFee = 0;
                int sl = 0;
                for (int i = 0; i < response.size(); i++) {
                    Product productTemp = response.get(i).getProductDomain();
                    float priceTotalProduct = productTemp.getPrice() * (1 - productTemp.getDiscount()) * response.get(i).getQuantity();
                    itemTotalFee += priceTotalProduct;
                    sl += response.get(i).getQuantity();
                    carts.add(response.get(i));
                }
                ItemTotalFeeTxt.setText(AppUtils.formatCurrency(itemTotalFee));
                txtTotalItemTemp.setText(itemTotalFee + "");
//                if (response.size() != 0)
//                    DeliveryFeeTxt.setText(AppUtils.formatCurrency(30000f));
//                txtDeliveryItemTemp.setText("30000");
                deliveryFee = Integer.parseInt(txtDeliveryItemTemp.getText().toString());
                float all = itemTotalFee + deliveryFee;
                txtTotalOrder.setText(AppUtils.formatCurrency(all));
            }
        }

        @Override
        public void didError(String message) {
            Log.d("cart", message);
        }
    };

    @Override
    public void changeQuantity(boolean isPlus, float price) {
        float totalItem = Float.parseFloat(txtTotalItemTemp.getText().toString());
        if (isPlus) {
            totalItem += price;
        } else {
            totalItem -= price;
        }

        float discount = Float.parseFloat(txtValueDiscount.getText().toString());
        float delivery = Float.parseFloat(txtDeliveryItemTemp.getText().toString());
        float allFree = totalItem * (1 - discount / 100) + delivery;
        txtTotalItemTemp.setText(totalItem + "");
        ItemTotalFeeTxt.setText(AppUtils.formatCurrency(totalItem));

        txtTotalOrder.setText(AppUtils.formatCurrency(allFree));


    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat2 = location.getLatitude();
        lon2 = location.getLongitude();
        if(lat1!=0 && lon1!=0) {
            Log.d("NHAN", "LOCATION:" + lon1 + "," + lat1 + "-" + lon2 + "," + lat2);
            mapViewModel.callGetDistanceFromTwoPlace(lon1, lat1, lon2, lat2, getString(R.string.mapbox_access_token));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}