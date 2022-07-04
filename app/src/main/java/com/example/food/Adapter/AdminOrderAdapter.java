package com.example.food.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Domain.Order;
import com.example.food.R;
import com.example.food.util.AppUtils;

import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;

public class AdminOrderAdapter extends ListAdapter<Order, AdminOrderAdapter.OrderViewHolder> {

    private ClickItem clickItem;
    public static interface ClickItem{
        void showDetailsOrder(Order order);
    }

    public AdminOrderAdapter( @NonNull DiffUtil.ItemCallback<Order> diffCallback, ClickItem clickItem) {
        super(diffCallback);
        this.clickItem = clickItem;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_state_item, null);
        return new OrderViewHolder(
                view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(getItem(position));

    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId, txtOrderState, txtOrderDate, txtOrderUser;
        RelativeLayout relativeLayout;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.text_view_id_order);
            txtOrderState = itemView.findViewById(R.id.text_view_state_order);
            txtOrderDate = itemView.findViewById(R.id.text_view_date_order);
            txtOrderUser = itemView.findViewById(R.id.text_view_user_order);
            relativeLayout = itemView.findViewById(R.id.relative_layout_order_admin_item);
            relativeLayout.setOnClickListener(view -> clickItem.showDetailsOrder(getItem(getAdapterPosition())));


        }

        public void bind(Order item)  {
            txtOrderId.setText("ID: " + item.getId());
            txtOrderState.setText("Trạng thái: " + item.getState());
            txtOrderUser.setText("Người đặt: " + item.getUser().getName());
            txtOrderDate.setText(AppUtils.formatDate(item.getCreateAt(), "dd-MM-yyyy"));
        }
    }

}
