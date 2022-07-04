package com.example.food.feature.discount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.example.food.Domain.Discount;
import com.example.food.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiscountAdapter extends SliderViewAdapter<DiscountAdapter.DiscountViewHolder> {

    List<Discount> discounts;
    ClickItem clickItem;

    public static interface ClickItem{
        void showDiscountDetails(String id);
        void showDiscountDetails(Discount discount);

    }

    public DiscountAdapter(List<Discount> discounts, ClickItem clickItem){
        this.discounts = discounts;
        this.clickItem = clickItem;
    }
    @Override
    public DiscountAdapter.DiscountViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    public void setDiscounts(List<Discount> discounts){
        this.discounts = discounts;
        notifyDataSetChanged();
    }

    /**
     * Bind data at position into viewHolder
     *
     * @param viewHolder item view holder
     * @param position   item position
     */
    @Override
    public void onBindViewHolder(DiscountAdapter.DiscountViewHolder viewHolder, int position) {
//        Picasso.get().load(discounts.get(position).getImageDiscount().getLink()).into(viewHolder.imgDiscount);
        Glide.with(viewHolder.itemView)
                .load(discounts.get(position).getImageDiscount().getLink())
                .fitCenter()
                .into(viewHolder.imgDiscount);

        viewHolder.imgDiscount.setOnClickListener(view -> {
            clickItem.showDiscountDetails(discounts.get(position));
        });
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return discounts.size();
    }

    public class DiscountViewHolder extends  SliderViewAdapter.ViewHolder {
        ImageView imgDiscount;

        public DiscountViewHolder(View itemView) {
            super(itemView);
            imgDiscount = itemView.findViewById(R.id.img_discount_item);

        }
    }
}
