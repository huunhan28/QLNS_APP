package com.example.food.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.Domain.Comment;
import com.example.food.Domain.Order;
import com.example.food.Domain.User;
import com.example.food.R;
import com.example.food.network.repository.UserRepository;
import com.example.food.util.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//public class CommentAdapter extends ListAdapter<Comment, CommentAdapter.MyViewHolder> {
//
//    UserRepository userRepository;
//
//    public CommentAdapter(@NonNull DiffUtil.ItemCallback<Comment> diffCallback, UserRepository userRepository) {
//        super(diffCallback);
//        this.userRepository = userRepository;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
//        return new MyViewHolder(
//                view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.bind(getItem(position));
//
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtName, txtDate, txtComment;
//        RatingBar ratingBar;
//        CircleImageView circleImageView;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            txtName = itemView.findViewById(R.id.text_view_name_of_user_comment);
//            txtDate = itemView.findViewById(R.id.text_view_date_of_comment);
//            txtComment = itemView.findViewById(R.id.text_view_comment_of_user);
//            ratingBar = itemView.findViewById(R.id.rating_bar_of_comment);
//            circleImageView = itemView.findViewById(R.id.circle_image_view_avt_user_comment);
//
//
//        }
//
//        @SuppressLint("CheckResult")
//        public void bind(Comment item) {
//            String temp = item.getId().split("-")[0];
//            temp = temp.substring(6,temp.length());
//
//            int userId = Integer.parseInt(temp);
//            userRepository.getUserById(userId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            success -> {
//                                if (success.isSuccessful()) {
//                                    User user = success.body().getData();
//                                    txtName.setText(user.getName());
//                                    if(user.getImageUser()!=null && !user.getImageUser().getLink().equals("")) {
//                                        String urlImage = user.getImageUser().getLink();
//                                        Glide.with(circleImageView.getContext())
//                                                .load(urlImage)
//                                                .error(R.drawable.ic_user)
//                                                .placeholder(R.drawable.ic_user)
//                                                .into(circleImageView);
//                                    }
//                                    txtDate.setText(AppUtils.formatDate(item.getCreateAt(), "dd-MM-yyyy"));
//                                    txtComment.setText(item.getComment());
//                                    ratingBar.setRating(item.getRating());
//                                }else{
//                                    Log.d("NHAN", "call user for comment failed " + success.errorBody().string());
//                                }
//                            }
//                            , error -> Log.d("NHAN", "call user for comment failed " + error.getLocalizedMessage())
//
//                    );
//
//
//        }
//    }
//
//}
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    UserRepository userRepository;
    List<Comment> comments;
    public CommentAdapter(List<Comment> comments, UserRepository userRepository) {
        this.comments = comments;
        this.userRepository = userRepository;
    }

    public void setData(List<Comment> response) {
        comments = response;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(inflate);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_view_comment_of_user.setText(comments.get(position).getComment());
        holder.rating_bar_of_comment.setRating(comments.get(position).getRating());
        holder.text_view_date_of_comment.setText(AppUtils.formatDate(comments.get(position).getCreateAt(), "dd-MM-yyyy"));

//        String temp = comments.get(position).getId().split("-")[0];
//        temp = temp.substring(6,temp.length());
//
        int userId = comments.get(position).getUserId();
//        userId = Integer.parseInt(temp);

        userRepository.getUserById(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            success -> {
                                if (success.isSuccessful()) {
                                    User user = success.body().getData();
                                    holder.text_view_name_of_user_comment.setText(user.getName());
                                    if(user.getImageUser()!=null && !user.getImageUser().getLink().equals("")) {
                                        String urlImage = user.getImageUser().getLink();
                                        Glide.with(holder.circle_image_view_avt_user_comment.getContext())
                                                .load(urlImage)
                                                .error(R.drawable.ic_user)
                                                .placeholder(R.drawable.ic_user)
                                                .into(holder.circle_image_view_avt_user_comment);
                                    }
                                }else{
                                    Log.d("NHAN", "call user for comment failed " + success.errorBody().string());
                                }
                            }
                            , error -> Log.d("NHAN", "call user for comment failed " + error.getLocalizedMessage())

                    );
//        holder.tv_order_id.setText(orders.get(position).getId()+"");
//
//        //String newstring = orders.get(position).getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.get(position).getCreateAt());
//        holder.tv_date.setText(stringDate);
//        if(orders.get(position).getDiscount()!=null){
//            holder.tv_discount.setText(orders.get(position).getDiscount().getId());
//        }
//        if(orders.get(position).getState()!=null){
//            holder.tv_state.setText(orders.get(position).getState());
//        }
//        holder.constraintLayout.setOnClickListener(view -> mClickListener.onItemClicked(orders.get(position)));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_view_comment_of_user,text_view_date_of_comment,text_view_name_of_user_comment;
        RatingBar rating_bar_of_comment;
        CircleImageView circle_image_view_avt_user_comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_comment_of_user = itemView.findViewById(R.id.text_view_comment_of_user);
            rating_bar_of_comment = itemView.findViewById(R.id.rating_bar_of_comment);
            text_view_date_of_comment = itemView.findViewById(R.id.text_view_date_of_comment);
            circle_image_view_avt_user_comment = itemView.findViewById(R.id.circle_image_view_avt_user_comment);
            text_view_name_of_user_comment = itemView.findViewById(R.id.text_view_name_of_user_comment);
        }
    }
}
