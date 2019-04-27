package com.dan.traderevmobilechallenge.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dan.traderevmobilechallenge.databinding.ItemLayoutBinding;
import com.dan.traderevmobilechallenge.model.Photo;

import java.util.ArrayList;

/**
 * Created by Dan Kim on 2019-04-26
 */
public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "myDebug";
    private ArrayList<Photo> photos;
    private LayoutInflater layoutInflater;
    private Callback callback;

    public StaggeredRecyclerViewAdapter(Callback callback) {
        this.callback = callback;
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemLayoutBinding itemLayoutBinding;

        public ViewHolder(@NonNull final ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;

            this.itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + photos);
                    callback.onClickItem(getAdapterPosition());
                }
            });
        }

        void bind(Photo photo) {
            this.itemLayoutBinding.setPhoto(photo);
        }
    }

    public interface Callback {
        void onClickItem(int selectedItemPosition);
    }
}
