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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.application.CustomApp;
import com.dan.traderevmobilechallenge.databinding.ImageCardBinding;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.view.fragments.FullImagePagerFragment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is adapter associated with RecyclerView
 * It shows photo data in Grid style
 *
 * Created by Dan Kim on 2019-04-30
 */
public class GridAdapter extends ListAdapter<Photo,GridAdapter.ImageViewHolder> {

    private final RequestManager requestManager;
    private final ViewHolderListener viewHolderListener;
    private LayoutInflater layoutInflater;

    /**
     * A listener that is attached to all ViewHolders to handle photo loading events and clicks.
     */
    private interface ViewHolderListener {

        void onLoadCompleted(int adapterPosition);
        void onItemClicked(View view, int adapterPosition);
    }

    private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.id.equals(newItem.id) && oldItem.user.name.equals(newItem.user.name)
                    && oldItem.createdAt.equals(newItem.createdAt);
        }
    };

    /**
     * Constructs a new grid adapter for the given {@link com.dan.traderevmobilechallenge.view.fragments.GridViewFragment}.
     */
    public GridAdapter(Fragment fragment) {
        super(DIFF_CALLBACK);
        this.requestManager = Glide.with(fragment);
        this.viewHolderListener = new ViewHolderListenerImpl(fragment);
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
        holder.onBind(getItem(position));
    }

    /**
     *  {@link ViewHolderListener} implementation
     */
    private static class ViewHolderListenerImpl implements ViewHolderListener {

        private final Fragment fragment;
        private final AtomicBoolean enterTransitionStarted;

        /**
         * Construct with passing a given Fragment
         * @param fragment {@link com.dan.traderevmobilechallenge.view.fragments.GridViewFragment}
         */
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

            // Shared element transition starts
            ImageView transitioningView = view.findViewById(R.id.iv_photo);
            Objects.requireNonNull(fragment.getFragmentManager())
                    .beginTransaction()
                    .setReorderingAllowed(true) // Optimize for shared element transition
                    .addSharedElement(transitioningView, transitioningView.getTransitionName())
                    .replace(R.id.fragment_container, new FullImagePagerFragment(), FullImagePagerFragment.class
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

        private final RequestManager requestManager;
        private final ViewHolderListener viewHolderListener;
        private final ImageCardBinding imageCardBinding;

        ImageViewHolder(ImageCardBinding imageCardBinding, RequestManager requestManager,
                        ViewHolderListener viewHolderListener) {
            super(imageCardBinding.getRoot());

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

            setImage(photo);
            //data bind to xml
            imageCardBinding.setPhoto(photo);
            imageCardBinding.ivPhoto.setTransitionName(CustomApp.getContext().getString(R.string.photo) + photo.id);
        }

        void setImage(Photo photo) {
            // Load the photoImage with Glide
            requestManager
                    .load(photo.urls.small)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(getAdapterPosition());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(getAdapterPosition());
                            return false;
                        }
                    })
                    .into(imageCardBinding.ivPhoto);

        }

        @Override
        public void onClick(View view) {
            // Let the listener start the FullImagePagerFragment.
            viewHolderListener.onItemClicked(view, getAdapterPosition());
        }
    }
}
