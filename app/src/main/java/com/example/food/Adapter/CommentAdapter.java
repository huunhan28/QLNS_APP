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

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommentAdapter extends ListAdapter<Comment, CommentAdapter.MyViewHolder> {

    UserRepository userRepository;

    public CommentAdapter(@NonNull DiffUtil.ItemCallback<Comment> diffCallback, UserRepository userRepository) {
        super(diffCallback);
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
        return new MyViewHolder(
                view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtDate, txtComment;
        RatingBar ratingBar;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.text_view_name_of_user_comment);
            txtDate = itemView.findViewById(R.id.text_view_date_of_comment);
            txtComment = itemView.findViewById(R.id.text_view_comment_of_user);
            ratingBar = itemView.findViewById(R.id.rating_bar_of_comment);
            circleImageView = itemView.findViewById(R.id.circle_image_view_avt_user_comment);


        }

        @SuppressLint("CheckResult")
        public void bind(Comment item) {
            String temp = item.getId().split("-")[0];
            temp = temp.substring(6,temp.length());

            int userId = Integer.parseInt(temp);
            userRepository.getUserById(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            success -> {
                                if (success.isSuccessful()) {
                                    User user = success.body().getData();
                                    txtName.setText(user.getName());
                                    if(user.getImageUser()!=null && !user.getImageUser().getLink().equals("")) {
                                        String urlImage = user.getImageUser().getLink();
                                        Glide.with(circleImageView.getContext())
                                                .load(urlImage)
                                                .error(R.drawable.ic_user)
                                                .placeholder(R.drawable.ic_user)
                                                .into(circleImageView);
                                    }
                                    txtDate.setText(AppUtils.formatDate(item.getCreateAt(), "dd-MM-yyyy"));
                                    txtComment.setText(item.getComment());
                                    ratingBar.setRating(item.getRating());
                                }else{
                                    Log.d("HIEN", "call user for comment failed " + success.errorBody().string());
                                }
                            }
                            , error -> Log.d("HIEN", "call user for comment failed " + error.getLocalizedMessage())

                    );


        }
    }

}
