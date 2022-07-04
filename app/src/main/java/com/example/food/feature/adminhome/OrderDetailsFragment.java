package com.example.food.feature.adminhome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.food.Domain.Comment;
import com.example.food.Domain.Order;
import com.example.food.R;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.OrderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OrderDetailsFragment extends BottomSheetDialogFragment {
    private static final String KEY_ORDER_DETAILS = "order_details";
    private static ClickButton mClickButton;
    private Order mOrder;
    private TextView txtTotal, txtProductName, txtDeliveryAddress, txtOrderId, txtComment;
    private RatingBar rating;
    private ConstraintLayout layoutCommentOfOrder;
    private Button btnCancel, btnAccept;
    private OrderViewModel orderViewModel;
    private View view;
    private String titleAccept = "Chấp nhận";
    private String titleCancel = "Đóng";
    private boolean visibleAccept = true;


    public interface ClickButton {
        void clickButtonAccept(int idOrder, String state);

        void clickButtonAccept2(int idOrder, String state, int userId, int rating, String comment);
    }

    public void setVisibleAccept(boolean visibleAccept) {
        this.visibleAccept = visibleAccept;
    }

    public void setTitleButton(String titleCancel, String titleAccept) {
        btnCancel.setText(titleCancel);
        btnAccept.setText(titleAccept);
    }

    public static OrderDetailsFragment newInstance(Order order, ClickButton clickButton) {
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER_DETAILS, order);
        orderDetailsFragment.setArguments(bundle);
        mClickButton = clickButton;
        return orderDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            mOrder = (Order) bundleReceive.getSerializable(KEY_ORDER_DETAILS);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_order_details, null);
        bottomSheetDialog.setContentView(view);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        initView(view);
        setDataOrder();
        btnCancel.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
        btnAccept.setOnClickListener(view1 -> {
            // check if admin then call click with no comment
            if (AppUtils.isAdmin(requireContext()))
                mClickButton.clickButtonAccept(mOrder.getId(), mOrder.getState());
            else
                mClickButton.clickButtonAccept2(mOrder.getId(), mOrder.getState(),AppUtils.getAccount2(requireContext()).getId(),  (int) rating.getRating(), txtComment.getText().toString());
            bottomSheetDialog.dismiss();
        });// dda giao
        return bottomSheetDialog;
    }


    public View getView() {
        return view;
    }

    private void initView(View view) {
        txtTotal = view.findViewById(R.id.text_view_total_order);
        txtProductName = view.findViewById(R.id.text_view_product_name_of_order);
        txtDeliveryAddress = view.findViewById(R.id.text_view_address_of_order);
        txtOrderId = view.findViewById(R.id.text_view_order_details_id);
        btnCancel = view.findViewById(R.id.button_cancel_order_details);
        btnAccept = view.findViewById(R.id.button_accept_order_details);
        rating = view.findViewById(R.id.rating_bar_of_order);
        txtComment = view.findViewById(R.id.edit_text_comment_of_order);

        layoutCommentOfOrder = view.findViewById(R.id.constraint_layout_comment_of_order_details);

        btnCancel.setText(titleCancel);
        btnAccept.setText(titleAccept);
    }

    @SuppressLint("CheckResult")
    private void setDataOrder() {
        if (mOrder == null) return;
        String state = mOrder.getState();
        // for admin
        layoutCommentOfOrder.setVisibility(View.GONE);
        if (AppUtils.isAdmin(requireContext())) {
            if (state.equals(AppUtils.orderState[0])) { // chưa duyệt
                btnAccept.setVisibility(View.VISIBLE);
            } else if (state.equals(AppUtils.orderState[1])) { // đang giao
                btnAccept.setVisibility(View.INVISIBLE);
            } else if (state.equals(AppUtils.orderState[2])) { // đã giao
                btnAccept.setVisibility(View.INVISIBLE);
                // check order commented
                if (mOrder.isCommented()) {
                    layoutCommentOfOrder.setVisibility(View.VISIBLE);
                    layoutCommentOfOrder.setEnabled(false);
                } else {
                    layoutCommentOfOrder.setVisibility(View.GONE);
                }
            }
        } else {// for user
            layoutCommentOfOrder.setVisibility(View.GONE);
            if (state.equalsIgnoreCase(AppUtils.orderState[0])) { // chưa duyệt
                setTitleButton("Đóng", "Hủy đơn hàng");
            } else if (state.equalsIgnoreCase(AppUtils.orderState[1])) { // đang giao
                setTitleButton("Đóng", "Đã nhận hàng");
            } else if (state.equalsIgnoreCase(AppUtils.orderState[2])) { //đã giao
                layoutCommentOfOrder.setVisibility(View.VISIBLE);
                if (mOrder.isCommented()) {
                    btnAccept.setVisibility(View.INVISIBLE);
                } else {
                    setTitleButton("Đóng", "Gửi đánh giá");
                }


            } else if (state.equalsIgnoreCase(AppUtils.orderState[3])) { //đã hủy
                btnAccept.setVisibility(View.INVISIBLE);
            }
        }
        txtOrderId.setText("Mã đơn hàng: " + mOrder.getId());
        txtTotal.setText(AppUtils.formatCurrency(mOrder.getTotalPriceOfProducts()));
        txtProductName.setText(mOrder.getProductsName());
        txtDeliveryAddress.setText(mOrder.getUser().getAddress() + "");

        if(mOrder.isCommented()){
            orderViewModel.callGetCommentOfOrder(mOrder.getId())
                    .subscribe(success -> {
                        if(success.isSuccessful() && success.body().getStatus().equalsIgnoreCase("Ok")){
                            Comment comment = success.body().getData();
                            rating.setRating(comment.getRating());
                            txtComment.setText(comment.getComment());
                        }
                    }, error ->  Log.d("HIEN", "get comment of order failed" + error.getMessage()));
        }
    }


}
