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
    private MainActivity.OnSharedViewListener sharedViewListener;
    public ArrayList<ImageView> photoViews = new ArrayList<>();

    public StaggeredRecyclerViewAdapter(MainActivity.OnSharedViewListener sharedViewListener) {
        this.sharedViewListener = sharedViewListener;
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

        holder.itemLayoutBinding.ivPhoto.setTag(position);
        photoViews.add(holder.itemLayoutBinding.ivPhoto);

        holder.bind(this.photos.get(position));
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
        if (photoViews == null){
            photoViews = new ArrayList<>();
        }
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

            Log.d(TAG, "onClick: ");
            intent.putExtra("current", getAdapterPosition());

            int i = (int) v.getTag();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                String name = v.getContext()
//                        .getString(R.string.transition_name, getAdapterPosition(), getAdapterPosition());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((AppCompatActivity) context), v, v.getTransitionName());

                ((AppCompatActivity) context).startActivityForResult(intent, 0, options.toBundle());
                sharedViewListener.onSharedViewListener(photoViews, getAdapterPosition());


            } else {
                ((AppCompatActivity) context).startActivity(intent);
            }

        }

        void bind(Photo photo) {

            for (int i = 0; i<photos.size() && i<photoViews.size();i++){

                photoViews.get(i).setTag(i);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    String name = this.context.getString(R.string.transition_name, getAdapterPosition(), i);

                    photoViews.get(i).setTransitionName(name);

                }
            }

            this.itemLayoutBinding.setPhoto(photo);
        }


    }


}
