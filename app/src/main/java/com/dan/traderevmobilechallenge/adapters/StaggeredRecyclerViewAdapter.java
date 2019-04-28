package com.dan.traderevmobilechallenge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dan.traderevmobilechallenge.databinding.ItemLayoutBinding;
import com.dan.traderevmobilechallenge.globals.Constants;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.SlideShowActivity;

import java.util.ArrayList;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

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
        private final Context context;

        ViewHolder(@NonNull final ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());

            this.itemLayoutBinding = itemLayoutBinding;
            this.context = itemLayoutBinding.getRoot().getContext();
            this.itemLayoutBinding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SlideShowActivity.class);
            intent.putExtra(Constants.KEY_CURRENT_POSITION, getAdapterPosition());
            intent.putParcelableArrayListExtra(Constants.KEY_PHOTOS, photos);

            ((AppCompatActivity) context).startActivityForResult(intent, 0);

        }

        void bind(Photo photo) {

            this.itemLayoutBinding.ivPhoto.setTransitionName(photo.urls.regular);
            this.itemLayoutBinding.setPhoto(photo);
        }
    }
}
