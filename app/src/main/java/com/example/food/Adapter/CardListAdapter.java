package com.example.food.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Api.Api;
import com.example.food.Domain.Cart;
import com.example.food.Domain.Product;
import com.example.food.Domain.Response.CartResponse;
import com.example.food.network.Listener.CartResponseListener;
import com.example.food.network.Listener.DeleteCartResponseListener;
import com.example.food.network.Listener.InsertCartResponseListener;
import com.example.food.R;
import com.example.food.dto.CartDTO;
import com.example.food.Domain.User;
import com.example.food.network.repository.CartRepository;
import com.example.food.util.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    ArrayList<Cart> carts=new ArrayList<>();
    Api api;
    Context context;

    private ChangNumberItemsListener changNumberItemsListener;

    public void setValue(ArrayList<Cart> list){
        this.carts = list;
        notifyDataSetChanged();
    }

    public interface ChangNumberItemsListener{
        void changeQuantity(boolean isPlus, float price);
    }

    public CardListAdapter(ArrayList<Cart> carts, Context context, ChangNumberItemsListener changNumberItemsListener) {
        this.carts = carts;
        this.context=context;
        this.changNumberItemsListener = changNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_card, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product productTemp = carts.get(position).getProductDomain();
        holder.title.setText(carts.get(position).getProductDomain().getName());
        holder.feeEachItem.setText(AppUtils.formatCurrency(carts.get(position).getProductDomain().getPrice()));
        holder.feeEachItem.setPaintFlags(holder.feeEachItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        float priceSale = productTemp.getPrice()*(1-productTemp.getDiscount());
        holder.feeSaleEachItem.setText(AppUtils.formatCurrency(priceSale));
        holder.totalEachItem.setText(AppUtils.formatCurrency(Math.round(priceSale)*carts.get(position).getQuantity()));
        Picasso.get().load(carts.get(position).getProductDomain().getImage().getLink()).into(holder.pic);
        holder.num.setText(String.valueOf(carts.get(position).getQuantity()));


        api = new Api(context);
        User user = AppUtils.getAccount(context.getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                Product product = carts.get(position).getProductDomain();
                // update ui
                int quantity=Integer.parseInt(holder.num.getText().toString());
                holder.num.setText(String.valueOf(++quantity));
                holder.totalEachItem.setText(quantity*product.getPrice()*(1-product.getDiscount()) + "");

                //update cart data
                CartDTO cartDTO=new CartDTO(user.getId()
                        , Math.toIntExact(product.getProductId())
                        ,1);
                //
                Api.getRetrofit(AppUtils.BASE_URL)
                        .create(CartRepository.class)
                        .plusQuantity(cartDTO)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseObjectResponse -> {
                         if(responseObjectResponse.code()!=200){
                        Toast.makeText(context,
                                AppUtils.getErrorMessage(responseObjectResponse.errorBody().string()), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show() );
                changNumberItemsListener.changeQuantity(true, product.getPrice()*(1-product.getDiscount()));
            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                // call minus quantity
                Product product = carts.get(position).getProductDomain();
                CartDTO cartDTO=new CartDTO(user.getId()
                        , Math.toIntExact(product.getProductId())
                        ,1);
                Api.getRetrofit(AppUtils.BASE_URL)
                        .create(CartRepository.class)
                        .minusQuantity(cartDTO)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseObjectResponse -> {
                            if(responseObjectResponse.code()!=200){
                                Toast.makeText(context,
                                        AppUtils.getErrorMessage(responseObjectResponse.errorBody().string()), Toast.LENGTH_SHORT).show();
                            }
                        } , throwable -> Log.d("APIminus", throwable.getMessage()));

                // update ui
                int quantity=Integer.parseInt(holder.num.getText().toString());
                if(quantity==1) {
                    carts.remove(position);
                    notifyDataSetChanged();
                    return;
                }
                holder.num.setText(String.valueOf(--quantity));
                holder.totalEachItem.setText(quantity*product.getPrice()*(1-product.getDiscount()) + "");
                changNumberItemsListener.changeQuantity(false, product.getPrice()*(1-product.getDiscount()));
                holder.totalEachItem.setText(quantity*product.getPrice()*(1-product.getDiscount()) + "");
            }
        });
    }

    private void setData() {
        User user = AppUtils.getAccount(context.getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));
        api.getCartsByUserId(cartResponseListener,user.getId());

    }

    private final CartResponseListener cartResponseListener = new CartResponseListener() {
        @Override
        public void didFetch(ArrayList<Cart> response, String message) {
            ArrayList<Cart> carts1=new ArrayList<>();
            if (response != null) {
                for (int i = 0; i < response.size(); i++) {
                    carts1.add(response.get(i));
                }
            }
            carts=carts1;
            for (int i = 0; i < carts.size(); i++) {
                System.out.println(carts.get(i).toString());
            }
            notifyDataSetChanged();
        }

        @Override
        public void didError(String message) {
            Log.d("cart",message);
        }
    };

    private final InsertCartResponseListener updateCartResponseListener=new InsertCartResponseListener() {
        @Override
        public void didFetch(CartResponse response, String message) {
            Toast.makeText(context, "Call api update cart success"+message.toString(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(context,"Call api error"+message.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    private final DeleteCartResponseListener deleteCartResponseListener=new DeleteCartResponseListener() {
        @Override
        public void didFetch(String response, String message) {
            Toast.makeText(context, "Call api delete cart success"+message.toString(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(context,"Call api error"+message.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,feeEachItem,totalEachItem,addBtn,num, feeSaleEachItem;
        ImageView pic,plusItem,minusItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title2Txt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            feeSaleEachItem = itemView.findViewById(R.id.feeSaleEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            pic = itemView.findViewById(R.id.picCard);
            num=itemView.findViewById(R.id.numberItemTxt);
            plusItem=itemView.findViewById(R.id.plusItem);
            minusItem=itemView.findViewById(R.id.minusItem);

        }
    }
}

