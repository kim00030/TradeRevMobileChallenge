package com.dan.traderevmobilechallenge.adapters;

import android.graphics.drawable.Drawable;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.databinding.ImageCardBinding;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.view.fragments.ImagePagerFragment;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Dan Kim on 2019-04-30
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

    private final RequestManager requestManager;
    private final ViewHolderListener viewHolderListener;
    private ArrayList<Photo> photos;
    private LayoutInflater layoutInflater;

    /**
     * A listener that is attached to all ViewHolders to handle image loading events and clicks.
     */
    private interface ViewHolderListener {

        void onLoadCompleted(int adapterPosition);

        void onItemClicked(View view, int adapterPosition);
    }

    /**
     * Constructs a new grid adapter for the given {@link Fragment}.
     */
    public GridAdapter(Fragment fragment) {
        this.requestManager = Glide.with(fragment);
        this.viewHolderListener = new ViewHolderListenerImpl(fragment);
    }

    /**
     * setter to set list of photo data sent from server
     *
     * @param photos list of photo data
     */
    public void setPhotos(ArrayList<Photo> photos) {

        this.photos = photos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        // Inflate views being used by DataBinding object
        ImageCardBinding imageCardBinding = ImageCardBinding.inflate(layoutInflater, parent, false);


        return new ImageViewHolder(imageCardBinding, requestManager, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.onBind(photo);
    }

    @Override
    public int getItemCount() {
        return this.photos == null ? 0 : this.photos.size();
    }

    private static class ViewHolderListenerImpl implements ViewHolderListener {

        private final Fragment fragment;
        private final AtomicBoolean enterTransitionStarted;

        ViewHolderListenerImpl(Fragment fragment) {
            this.fragment = fragment;
            this.enterTransitionStarted = new AtomicBoolean();
        }

        @Override
        public void onLoadCompleted(int position) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainActivity.currentPosition != position) {
                return;
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            fragment.startPostponedEnterTransition();
        }


        @Override
        public void onItemClicked(View view, int position) {
            // Update the position.
            MainActivity.currentPosition = position;

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            ((TransitionSet) Objects.requireNonNull(fragment.getExitTransition())).excludeTarget(view, true);

            ImageView transitioningView = view.findViewById(R.id.iv_photo);
            Objects.requireNonNull(fragment.getFragmentManager())
                    .beginTransaction()
                    .setReorderingAllowed(true) // Optimize for shared element transition
                    .addSharedElement(transitioningView, transitioningView.getTransitionName())
                    .replace(R.id.fragment_container, new ImagePagerFragment(), ImagePagerFragment.class
                            .getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }


    /**
     * ViewHolder for the grid's images.
     */
    static class ImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private final ImageView photoImage;
        private final RequestManager requestManager;
        private final ViewHolderListener viewHolderListener;
        private final ImageCardBinding imageCardBinding;

        ImageViewHolder(ImageCardBinding imageCardBinding, RequestManager requestManager,
                        ViewHolderListener viewHolderListener) {
            super(imageCardBinding.getRoot());
            this.photoImage = imageCardBinding.ivPhoto;
            this.requestManager = requestManager;
            this.viewHolderListener = viewHolderListener;
            this.imageCardBinding = imageCardBinding;
            imageCardBinding.cardView.setOnClickListener(this);
        }


        /**
         * Binds this view holder to the given adapter position.
         * <p>
         * The binding will load the photoImage into the photoImage view, as well as set its transition name for
         * later.
         */
        void onBind(Photo photo) {
            int adapterPosition = getAdapterPosition();
            setImage(photo, adapterPosition);
            imageCardBinding.setPhoto(photo);
            photoImage.setTransitionName("photo" + photo.id);
        }

        void setImage(Photo photo, final int adapterPosition) {
            // Load the photoImage with Glide to prevent OOM error when the photoImage drawables are very large.
            requestManager
                    .load(photo.urls.small)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(adapterPosition);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(adapterPosition);
                            return false;
                        }
                    })
                    .into(photoImage);

        }

        @Override
        public void onClick(View view) {
            // Let the listener start the ImagePagerFragment.
            viewHolderListener.onItemClicked(view, getAdapterPosition());
        }


    }

}
