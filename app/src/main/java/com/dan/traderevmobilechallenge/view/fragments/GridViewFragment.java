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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dan.traderevmobilechallenge.adapters.StaggeredRecyclerViewAdapter;
import com.dan.traderevmobilechallenge.databinding.FragmentGridBinding;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.Objects;

/**
 * This fragment class to utilize Grid view for showing photos
 * using Staggered layout
 */
public class GridViewFragment extends Fragment {

    private StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter;
    private static final int SPAN_COUNT = 2;
    private FragmentGridBinding fragmentGridBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentGridBinding = FragmentGridBinding.inflate(inflater);
        // Instantiate view adapter for recycler view
        staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (SPAN_COUNT, LinearLayoutManager.VERTICAL);

        // To avoid item moving around
        fragmentGridBinding.recyclerView.setItemAnimator(null);
        fragmentGridBinding.recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        fragmentGridBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        fragmentGridBinding.recyclerView.setHasFixedSize(true);
        //Get ViewModel
        MainActivityViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);
        // Observe photo data state and update it to UI
        viewModel.getPhotosLiveData().observe(getViewLifecycleOwner(), photos -> staggeredRecyclerViewAdapter.setPhotos(photos));

        return fragmentGridBinding.recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Update RecyclerView with current position
     *
     * @param currentPosition current position of selected item or item viewed at ViewPager
     */
    public void updateCurrentPosition(int currentPosition) {

        try {

            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) fragmentGridBinding.recyclerView.getLayoutManager();

            int[] into = new int[SPAN_COUNT];// 2 is span count.

            int firstVisibleItem = Objects.requireNonNull(layoutManager).findFirstVisibleItemPositions(into)[0];
            // TESTING CODES
//            int findLastCompletedVisibleItem = layoutManager.findLastCompletelyVisibleItemPositions(into)[0];
//            int lastCompletedVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPositions(into)[0];
//            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(into)[0];

            if (currentPosition < firstVisibleItem) {
                currentPosition = currentPosition - (firstVisibleItem - currentPosition) < 0 ? 0 : currentPosition - (firstVisibleItem - currentPosition);
            }

            Objects.requireNonNull(fragmentGridBinding.recyclerView.getLayoutManager())
                    .scrollToPosition(currentPosition);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
