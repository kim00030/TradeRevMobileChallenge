package com.dan.traderevmobilechallenge.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ItemLayoutBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.view.SlideShowActivity;

import java.util.ArrayList;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "myDebug";
    private ArrayList<Photo> photos;
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemLayoutBinding itemLayoutBinding = ItemLayoutBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = holder.context
                .getString(R.string.transition_name, position,position);
        ViewCompat.setTransitionName(holder.itemLayoutBinding.ivPhoto,name);

        holder.bind(this.photos.get(position));
    }

    public void setPhotos(ArrayList<Photo> photos) {

        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.photos == null ? 0 : this.photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemLayoutBinding itemLayoutBinding;
        private Context context;


        public ViewHolder(@NonNull final ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());

            this.itemLayoutBinding = itemLayoutBinding;
            this.context = itemLayoutBinding.getRoot().getContext();
            this.itemLayoutBinding.ivPhoto.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent((AppCompatActivity) context, SlideShowActivity.class);
            intent.putExtra(Constants.KEY_CURRENT_POSITION, getAdapterPosition());
            intent.putParcelableArrayListExtra(Constants.KEY_PHOTOS, photos);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                String name = v.getContext()
//                        .getString(R.string.transition_name, getAdapterPosition(), getAdapterPosition());
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((AppCompatActivity) context), v, v.getTransitionName());
//
//                ((AppCompatActivity) context).startActivityForResult(intent, 0, options.toBundle());
//                sharedViewListener.onSharedViewListener(photoViews, getAdapterPosition());


                intent.putExtra(Constants.KEY_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(itemLayoutBinding.ivPhoto));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ((AppCompatActivity)context),
                        itemLayoutBinding.ivPhoto,
                        ViewCompat.getTransitionName(itemLayoutBinding.ivPhoto)
                );

                ((AppCompatActivity)context).startActivityForResult(intent,0);

            } else {
                ((AppCompatActivity) context).startActivity(intent);
            }

        }

        void bind(Photo photo) {


            this.itemLayoutBinding.setPhoto(photo);
        }


    }


}
