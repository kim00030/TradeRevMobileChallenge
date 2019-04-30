package com.dan.traderevmobilechallenge.view.fragments;


import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.GridAdapter;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This fragment class to utilize Grid view for showing photos
 * using Staggered layout
 */
public class GridViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_grid, container, false);
        gridAdapter = new GridAdapter(this);
        recyclerView.setAdapter(gridAdapter);
        //Get ViewModel
        MainActivityViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);

        viewModel.getPhotosLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                gridAdapter.setPhotos(photos);

            }
        });

        prepareTransitions();
        postponeEnterTransition();

        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();

    }

    private void scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left,
                                       int top,
                                       int right,
                                       int bottom,
                                       int oldLeft,
                                       int oldTop,
                                       int oldRight,
                                       int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);

                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                View viewAtPosition = Objects.requireNonNull(layoutManager).findViewByPosition(MainActivity.currentPosition);
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)) {
                    recyclerView.post(() -> layoutManager.scrollToPosition(MainActivity.currentPosition));
                }
            }
        });
    }

    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.

                        RecyclerView.ViewHolder selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                        if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.iv_photo));

                    }
                });

    }
}
