package com.example.food.Domain;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;


public class Comment {
    private String id;
    private Date createAt;
    private int rating;
    private String comment;

    public Comment() {
    }



    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment(String id, Date createAt, int rating, String comment) {
        this.id = id;
        this.createAt = createAt;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return rating == comment1.rating && Objects.equals(id, comment1.id) && Objects.equals(createAt, comment1.createAt) && Objects.equals(comment, comment1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createAt, rating, comment);
    }

    public static DiffUtil.ItemCallback<Comment> itemCallback = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.equals(newItem);
        }
    };

}
