package com.example.food.Domain;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.food.Domain.Response.OrderDetail;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private int id;
    private User user;
    private Date createAt;
    private Discount discount;
    private String state;
    private List<OrderDetail> orderDetails;
    private float total=0;
    private boolean commented;

    public Order() {
    }

    public Order(int id, User user, Date createAt, Discount discount, String state) {
        this.id = id;
        this.user = user;
        this.createAt = createAt;
        this.discount = discount;
        this.state = state;
    }


    public Order(int id, User user, Date createAt, Discount discount, String state, List<OrderDetail> orderDetails, float total) {
        this.id = id;
        this.user = user;
        this.createAt = createAt;
        this.discount = discount;
        this.state = state;
        this.orderDetails = orderDetails;
        this.total = total;
    }

    public Order(int id, User user, Date createAt, Discount discount, String state, List<OrderDetail> orderDetails, float total, boolean commented) {
        this.id = id;
        this.user = user;
        this.createAt = createAt;
        this.discount = discount;
        this.state = state;
        this.orderDetails = orderDetails;
        this.total = total;
        this.commented = commented;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public float getTotal() {
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId() &&
                Objects.equals(getUser(), order.getUser()) &&
                Objects.equals(getCreateAt(), order.getCreateAt()) &&
                Objects.equals(getDiscount(), order.getDiscount()) &&
                Objects.equals(getState(), order.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getCreateAt(), getDiscount(), getState());
    }

    public static DiffUtil.ItemCallback<Order> itemCallback = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }
    };

    public float getTotalPriceOfProducts(){
        if(orderDetails==null) return 0;
        else{
            total=0;
            orderDetails.stream()
                .forEach(orderDetail -> {
                total +=orderDetail.getPrice()*orderDetail.getQuantity()*(1-orderDetail.getDiscount());
            });
            return total;
        }
    }
    public String getProductsName(){

        if(orderDetails==null) return "";
        else{
            StringBuilder string = new StringBuilder();
            for(OrderDetail orderDetail: orderDetails){
                if(string.length()>0){
                    string.append("\n");
                }
                string.append(orderDetail.getProduct().getName() + " 1x" + orderDetail.getQuantity());
            }
            return string.toString();
        }

    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }
}
