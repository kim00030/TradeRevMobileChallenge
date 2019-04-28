package com.dan.traderevmobilechallenge.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.StaggeredRecyclerViewAdapter;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    private RecyclerView recyclerView;
    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private static final int SPAN_COUNT = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_grid, container, false);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);

        MainActivityViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);
        viewModel.getPhotosLiveData().observe(getViewLifecycleOwner(), photos -> staggeredRecyclerViewAdapter.setPhotos(photos));
        // Inflate the layout for this fragment
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();
    }

    /**
     * Scroll the recycler view to show the last viewed item in the grid. This is important when navigation
     * back from the grid
     */
    private void scrollToPosition() {

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {

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
}
