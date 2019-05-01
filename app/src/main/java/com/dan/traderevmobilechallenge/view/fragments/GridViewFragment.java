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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.GridAdapter;
import com.dan.traderevmobilechallenge.databinding.FragmentGridBinding;
import com.dan.traderevmobilechallenge.view.LoadingDialog;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This fragment class to utilize Grid view for showing photos
 * using Grid layout
 */
public class GridViewFragment extends Fragment {

    private GridAdapter gridAdapter;
    private FragmentGridBinding fragmentGridBinding;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentGridBinding = FragmentGridBinding.inflate(inflater);

        gridAdapter = new GridAdapter(this);
        fragmentGridBinding.recyclerView.setAdapter(gridAdapter);

        showLoadingDialog(savedInstanceState);
        //Get ViewModel
        MainActivityViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);

        // Observe Data from ViewModel
        viewModel.getPhotosLiveData().observe(getViewLifecycleOwner(), photos -> {
            // show data
            gridAdapter.submitList(photos);
            //loading dialog will be dismissing
            if (loadingDialog != null){
                loadingDialog.dismiss();
                loadingDialog = null;
            }else{
                loadingDialog = (LoadingDialog) getChildFragmentManager().findFragmentByTag(LoadingDialog.class.getSimpleName());
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        });

        prepareTransitions();
        postponeEnterTransition();

        return fragmentGridBinding.getRoot();
    }

    /**
     * Show Loading Dialog while fetching data
     * @param savedInstanceState Bundle sent by Android system
     */
    private void showLoadingDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadingDialog = new LoadingDialog();
            loadingDialog.setCancelable(false);
            loadingDialog.show(getChildFragmentManager(), LoadingDialog.class.getSimpleName());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();
    }

    /**
     * To Scroll to position the view for the current position is null (not currently part of
     * layout manager children), or it's not completely visible.
     */
    private void scrollToPosition() {
        fragmentGridBinding.recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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

    /**
     * To Prepare shared elements transition
     */
    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        // A similar mapping is set at the FullImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.

                        RecyclerView.ViewHolder selectedViewHolder = fragmentGridBinding.recyclerView
                                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                        if (selectedViewHolder == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.iv_photo));

                    }
                });
    }
}
