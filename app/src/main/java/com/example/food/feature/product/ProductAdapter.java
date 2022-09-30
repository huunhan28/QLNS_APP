package com.example.food.feature.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Activity.ShowDetailActivity;
import com.example.food.Api.Api;
import com.example.food.Domain.Cart;
import com.example.food.Domain.Product;
import com.example.food.Domain.Response.CartResponse;
import com.example.food.network.Listener.InsertCartResponseListener;
import com.example.food.R;
import com.example.food.dto.CartDTO;
import com.example.food.feature.home.HomeViewModel;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductViewHolder> {
    private HomeViewModel homeViewModel;
    private int itemLayout;
    private User user;
    Api api;
    public ProductAdapter(HomeViewModel homeViewModel, @NonNull DiffUtil.ItemCallback<Product> diffCallback, int itemLayout) {
        super(diffCallback);
        this.homeViewModel = homeViewModel;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, null);
        user = AppUtils.getAccount(parent.getContext().getSharedPreferences(AppUtils.ACCOUNT, 0));
        return new ProductViewHolder(
                view,
                homeViewModel);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(getItem(position));

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        AlertDialog alertDialog;
        TextView txtProductName, txtFeeProduct, btnAddProduct, txtProductSale, txtProductPriceSale;
        ImageView imgProduct;
        private HomeViewModel homeViewModel;
        public ProductViewHolder(@NonNull View itemView, HomeViewModel homeViewModel) {
            super(itemView);
            this.homeViewModel
                    = homeViewModel;
            txtProductName = itemView.findViewById(R.id.txt_product_name_item);
            txtFeeProduct = itemView.findViewById(R.id.txt_product_price_item);
            txtProductSale = itemView.findViewById(R.id.txt_product_sale_item);
            imgProduct = itemView.findViewById(R.id.img_product_item);
            btnAddProduct = itemView.findViewById(R.id.btn_add_product_item);
            txtProductPriceSale = itemView.findViewById(R.id.txt_product_price_item_sale);

        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Product item)  {
            txtProductName.setText(item.getName());
            txtFeeProduct.setText(AppUtils.formatCurrency(item.getPrice()));
            txtFeeProduct.setPaintFlags(txtFeeProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            float priceSale = item.getPrice()*(1-item.getDiscount());
            txtProductPriceSale.setText(AppUtils.formatCurrency((float)((int)priceSale)));
            if(item.getImage()!=null)
            Picasso.get().load(item.getImage().getLink()).into(imgProduct);
            txtProductSale.setText("Sale " + item.getDiscount()*100 + "%");
//            imgProduct.setImageBitmap(item.getImageBitmap());
            if(user != null){
                btnAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user = AppUtils.getAccount(itemView.getContext().getSharedPreferences(AppUtils.ACCOUNT, Context.MODE_PRIVATE));
                        Cart cart=new Cart(user,item,1);
                        CartDTO cartDTO=new CartDTO(cart.getUser().getId(),Integer.parseInt(cart.getProductDomain().getProductId()+""),cart.getQuantity());
                        api = new Api(itemView.getContext());
                        alertDialog= new SpotsDialog.Builder().setContext(itemView.getContext()).setTheme(R.style.CustomProgressBarDialog).build();
                        alertDialog.show();
                        api.insertCart(insertCartResponseListener,cartDTO);
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(itemView.getContext(), ShowDetailActivity.class);
                    intent.putExtra("object", item.getProductId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        private final InsertCartResponseListener insertCartResponseListener=new InsertCartResponseListener() {
            @Override
            public void didFetch(CartResponse response, String message) {
                alertDialog.dismiss();
//                AppUtils.showSuccessDialog(itemView.getContext(), "Đã thêm vào giỏ hàng");
                if (response.getStatus().equals("2") == true){
                    Toast.makeText(itemView.getContext(), response.getMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    AppUtils.showSuccessDialog(itemView.getContext(), "Đã thêm vào giỏ hàng");
                }
//                Toast.makeText(itemView.getContext(), "Đã thêm vào giỏ hàng"+message.toString(),Toast.LENGTH_SHORT).show();
                Log.d("success",message.toString());
            }

            @Override
            public void didError(String message) {
//                Toast.makeText(itemView.getContext(),"Call api error"+message.toString(),Toast.LENGTH_SHORT).show();
                Log.d("zzz",message.toString());
            }
        };
    }

    public static class ProductDiff extends DiffUtil.ItemCallback<Product>{
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId()==newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return
//                    oldItem.getName().equals(newItem.getName())&&
//                    oldItem.getDescription().equals(newItem.getDescription()) &&
//                    Double.compare(oldItem.getDiscount(), newItem.getDiscount())==0 &&
                    oldItem.getId()== newItem.getId()
//                    &&
//                    oldItem.getCalculationUnit().equals(newItem.getCalculationUnit())&&
//                    oldItem.getImages().equals(newItem.getImages())
                    ;
        }
    }
}
