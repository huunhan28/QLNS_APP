package com.example.food.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.food.Activity.ShowDetailActivity;
import com.example.food.Api.Api;
import com.example.food.Domain.Cart;
import com.example.food.Domain.Product;
import com.example.food.Domain.Response.CartResponse;
import com.example.food.network.Listener.InsertCartResponseListener;
import com.example.food.R;
import com.example.food.dto.CartDTO;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;

import java.io.Serializable;
import java.util.List;

public class CustomProductGridAdapter extends BaseAdapter implements Serializable {
    private List<Product> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    Api api;
    public CustomProductGridAdapter(Context aContext, List<Product> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.product_grid_item_layout, null);
            holder = new GridViewHolder();
            holder.image_product = convertView.findViewById(R.id.image_product);
            holder.name_product = (TextView) convertView.findViewById(R.id.name_product);
            holder.price_product = (TextView) convertView.findViewById(R.id.price_product);
            holder.calculationUnit_product = (TextView) convertView.findViewById(R.id.calculationUnit_product);
            holder.addBtn_product = (TextView) convertView.findViewById(R.id.addBtn_product);
            holder.cardViewProduct = convertView.findViewById(R.id.cardViewProduct);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

        Product gridItem = this.listData.get(position);
        holder.name_product.setText(gridItem.getName());
        holder.price_product.setText(gridItem.getPrice()+"");
        holder.calculationUnit_product.setText(gridItem.getCalculationUnit());

//        int imageId =0;
//        if(gridItem.getImages()!=null){
//            this.getMipmapResIdByName(gridItem.getImages().get(0).getLink());
//        }
//
//
//        holder.image_product.setImageResource(imageId);
        View finalConvertView = convertView;
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(finalConvertView.getContext(), ShowDetailActivity.class);
                intent.putExtra("object", listData.get(position));
                finalConvertView.getContext().startActivity(intent);
            }
        });
        holder.addBtn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = AppUtils.getAccount(context.getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));
                Cart cart=new Cart(user,gridItem,1);
                CartDTO cartDTO=new CartDTO(Integer.parseInt(cart.getUser().getId()+""),Integer.parseInt(cart.getProductDomain().getProductId()+""),cart.getQuantity());
                api = new Api(context);
                api.insertCart(insertCartResponseListener,cartDTO);
            }
        });
        return convertView;
    }

    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();

        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName, "drawable", pkgName);
        Log.i("CustomGridView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }
    private final InsertCartResponseListener insertCartResponseListener=new InsertCartResponseListener() {
        @Override
        public void didFetch(CartResponse response, String message) {
            Toast.makeText(context,"Call api success"+message.toString(),Toast.LENGTH_SHORT).show();
            Log.d("success",message.toString());
        }

        @Override
        public void didError(String message) {
            Toast.makeText(context,"Call api error"+message.toString(),Toast.LENGTH_SHORT).show();
            Log.d("zzz",message.toString());
        }
    };
}

class GridViewHolder {
    ImageView image_product;
    TextView name_product;
    TextView price_product;
    TextView calculationUnit_product;
    TextView addBtn_product;


    CardView cardViewProduct;
}
