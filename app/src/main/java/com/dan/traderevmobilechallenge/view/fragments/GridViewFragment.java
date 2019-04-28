package com.dan.traderevmobilechallenge.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dan.traderevmobilechallenge.adapters.StaggeredRecyclerViewAdapter;
import com.dan.traderevmobilechallenge.databinding.FragmentGridBinding;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewFragment extends Fragment {

    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private static final int SPAN_COUNT = 2;
    private FragmentGridBinding fragmentGridBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentGridBinding = FragmentGridBinding.inflate(inflater);

        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, LinearLayoutManager.VERTICAL);

        // To avoid item moving around
        fragmentGridBinding.recyclerView.setItemAnimator(null);
        fragmentGridBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        fragmentGridBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        fragmentGridBinding.recyclerView.setHasFixedSize(true);

        MainActivityViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);
        viewModel.getPhotosLiveData().observe(getViewLifecycleOwner(), photos -> staggeredRecyclerViewAdapter.setPhotos(photos));
        // Inflate the layout for this fragment
        return fragmentGridBinding.recyclerView;
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

        fragmentGridBinding.recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {

                fragmentGridBinding.recyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = fragmentGridBinding.recyclerView.getLayoutManager();
                View viewAtPosition = Objects.requireNonNull(layoutManager).findViewByPosition(MainActivity.currentPosition);
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)) {
                    fragmentGridBinding.recyclerView.post(() -> layoutManager.scrollToPosition(MainActivity.currentPosition));
                }
            }
        });
    }

    public void updateCurrentPosition(int currentPosition) {

        try {
            //noinspection ConstantConditions
            fragmentGridBinding.recyclerView.getLayoutManager().scrollToPosition(currentPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
