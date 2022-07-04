package com.example.food.feature.category;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Activity.ProductListActivity;
import com.example.food.Domain.Category;
import com.example.food.Domain.Image;
import com.example.food.R;
import com.example.food.feature.home.HomeScreenFragmentDirections;
import com.example.food.feature.home.HomeViewModel;
import com.example.food.feature.product.ProductScreenFragmentArgs;
import com.example.food.util.AppUtils;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Random;

public class CategoryAdapter extends ListAdapter<CategoryDTO, CategoryAdapter.CategoryViewHolder>{
    private HomeViewModel homeViewModel;
    private Resources resources;
    private String[]colorsBackground;
    private TypedArray categoryBackground;
    private Context context;

    public CategoryAdapter(HomeViewModel homeViewModel, @NonNull DiffUtil.ItemCallback<CategoryDTO> diffCallback,
                           Context context) {
        super(diffCallback);
        this.homeViewModel = homeViewModel;
        this.context = context;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        resources = parent.getResources();
        categoryBackground = parent.getResources().obtainTypedArray(R.array.category_background);
        colorsBackground = parent.getContext().getResources().getStringArray(R.array.color_category);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(
                view,
                homeViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        try {
            holder.bind(resources,
                    getItem(position));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HomeViewModel homeViewModel;
        TextView txt_category_name;
        ImageView img_category;
        RelativeLayout mainLayoutCat;
        CategoryDTO Category;


        public CategoryViewHolder(@NonNull View itemView, HomeViewModel homeViewModel) {
            super(itemView);
            this.homeViewModel = homeViewModel;
            txt_category_name = itemView.findViewById(R.id.txt_category_name_item);
            mainLayoutCat = itemView.findViewById(R.id.mainLayoutCat);
            img_category = itemView.findViewById(R.id.img_category_item);

            mainLayoutCat.setOnClickListener(this);
        }


        public void bind(Resources resources, CategoryDTO item) throws XmlPullParserException, IOException {
            Category = item;
            txt_category_name.setText(item.getName());
//            mainLayoutCat.setBackground(AppUtils.getDrawableBackgroundRandom(resources));
            int color = item.getId()% colorsBackground.length;
            int background = item.getId()%categoryBackground.length();

//            mainLayoutCat.setBackgroundColor(Color.parseColor(colorsBackground[new Random().nextInt(colorsBackground.length)]));
//            mainLayoutCat.setBackground(categoryBackground.getDrawable(background));
//            mainLayoutCat.setBackgroundColor(Color.parseColor(colorsBackground[color]));
            System.out.println("image category:" + item.getLink());
            Picasso.get().load(item.getLink()).into(img_category);

        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.mainLayoutCat){
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("category", getItem(getAdapterPosition()));
                context.startActivity(intent);
            }

        }
    }

   public static class CategoryDiff extends DiffUtil.ItemCallback<CategoryDTO>{


       @Override
       public boolean areItemsTheSame(@NonNull CategoryDTO oldItem, @NonNull CategoryDTO newItem) {
           return oldItem.getId() == newItem.getId();
       }


       @Override
       public boolean areContentsTheSame(@NonNull CategoryDTO oldItem, @NonNull CategoryDTO newItem) {
           return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getLink().equals(newItem.getLink());
       }
   }

}
