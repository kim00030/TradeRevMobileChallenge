package com.dan.traderevmobilechallenge.view.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.ImagePagerAdapter;
import com.dan.traderevmobilechallenge.databinding.FragmentPagerBinding;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This is fragment utilize full screen view to show selected photo in grid view page{@link GridViewFragment}
 */
public class FullImagePagerFragment extends Fragment {

    private FragmentPagerBinding fragmentPagerBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //MainActivity's ViewModel
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);
        // Get photo list data from ViewModel
        ArrayList<Photo> photos = mainActivityViewModel.getPhotosLiveData().getValue();
        // Inflate by DataBinding object
        fragmentPagerBinding = FragmentPagerBinding.inflate(inflater);

        fragmentPagerBinding.viewPager.setAdapter(new ImagePagerAdapter(this, photos));
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        fragmentPagerBinding.viewPager.setCurrentItem(MainActivity.currentPosition);
        fragmentPagerBinding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MainActivity.currentPosition = position;
            }
        });

        prepareSharedElementTransition();

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition();
        }

        return fragmentPagerBinding.getRoot();

    }

    /**
     * Method to prepare shared elements transition from the one with same transition name
     */
    private void prepareSharedElementTransition() {
        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        setSharedElementEnterTransition(transition);

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the image view at the primary fragment (the ImageFragment that is currently
                        // visible). To locate the fragment, call instantiateItem with the selection position.
                        // At this stage, the method will simply return the fragment at the position and will
                        // not create a new one.
                        Fragment currentFragment = (Fragment) Objects.requireNonNull(fragmentPagerBinding.viewPager.getAdapter())
                                .instantiateItem(fragmentPagerBinding.viewPager, MainActivity.currentPosition);
                        View view = currentFragment.getView();
                        if (view == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements.put(names.get(0), view.findViewById(R.id.image));
                    }
                });
    }

}
