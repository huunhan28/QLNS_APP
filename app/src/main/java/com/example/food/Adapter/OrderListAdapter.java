package com.example.food.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Domain.Order;
import com.example.food.Domain.Product;
import com.example.food.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{
    ArrayList<Order> orders;
    ClickOrderedListener mClickListener;
    public OrderListAdapter(ArrayList<Order> orders, ClickOrderedListener mClickListener) {
        this.orders = orders;
        this.mClickListener = mClickListener;
    }

    public void setData(ArrayList<Order> response) {
        orders = response;
        notifyDataSetChanged();
    }

    public void changeItem(Order order){
        for(int i=0;i<orders.size(); i++){
            if(order.getId()== orders.get(i).getId())
            {
                orders.get(i).setState(order.getState());
                orders.get(i).setCommented(order.isCommented());
                notifyDataSetChanged();
            }
        }
    }

    public interface ClickOrderedListener{
        void onItemClicked(Order order);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordered, parent, false);
        return new OrderListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_order_id.setText(orders.get(position).getId()+"");

        //String newstring = orders.get(position).getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.get(position).getCreateAt());
        holder.tv_date.setText(stringDate);
        if(orders.get(position).getDiscount()!=null){
            holder.tv_discount.setText(orders.get(position).getDiscount().getId());
        }
        if(orders.get(position).getState()!=null){
            holder.tv_state.setText(orders.get(position).getState());
        }
        holder.constraintLayout.setOnClickListener(view -> mClickListener.onItemClicked(orders.get(position)));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_date,tv_discount,tv_state;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.contraint_layout_item_ordered);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }
}
