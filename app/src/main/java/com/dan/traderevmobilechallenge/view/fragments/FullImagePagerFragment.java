package com.dan.traderevmobilechallenge.view.fragments;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dan.traderevmobilechallenge.R;
import com.dan.traderevmobilechallenge.adapters.ImagePagerAdapter;
import com.dan.traderevmobilechallenge.databinding.FragmentPagerBinding;
import com.dan.traderevmobilechallenge.model.Photo;
import com.dan.traderevmobilechallenge.utils.StringUtil;
import com.dan.traderevmobilechallenge.view.MainActivity;
import com.dan.traderevmobilechallenge.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This fragment utilizes full screen view to show selected photo in grid view page{@link GridViewFragment}
 */
@SuppressWarnings("ConstantConditions")
public class FullImagePagerFragment extends Fragment {

    private FragmentPagerBinding fragmentPagerBinding;
    private boolean toggleClick = false;
    private ArrayList<Photo> photos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //MainActivity's ViewModel
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainActivityViewModel.class);
        // Get photo list data from ViewModel
        photos = mainActivityViewModel.getPhotosLiveData().getValue();
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
        initFloatButton();
        showPhotoInfo();
        return fragmentPagerBinding.getRoot();
    }

    /**
     * Method to show photo info for current page
     */
    private void showPhotoInfo() {

        fragmentPagerBinding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // set photo info showing in full screen page
                fragmentPagerBinding.tvPhotoInfo.setText(StringUtil.formatPhotoData(photos.get(position)));
            }
        });
    }

    /**
     * Initialize floating button that controls showing/hiding photo info
     */
    private void initFloatButton() {

        //Animation with TextField for showing photo info
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(1)
                .playOn(fragmentPagerBinding.tvPhotoInfo));
        // when floating button clicks on toggle , show on/off the photo info
        fragmentPagerBinding.fbBtn.setOnClickListener(v -> getActivity().runOnUiThread(() -> {

            toggleClick = !toggleClick;

            if (toggleClick) {
                fragmentPagerBinding.fbBtn.setImageResource(R.drawable.eye_outline);
                fragmentPagerBinding.tvPhotoInfo.setVisibility(View.INVISIBLE);
            } else {
                fragmentPagerBinding.tvPhotoInfo.setVisibility(View.VISIBLE);
                fragmentPagerBinding.fbBtn.setImageResource(R.drawable.eye_off_outline);
            }
        }));
    }

    /**
     * Method to prepare shared elements transition from the one with same transition name in
     * GridView{@link GridViewFragment}
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

    @Override
    public void onResume() {
        super.onResume();
        // Hide toolbar
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // show toolbar for Activity
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }
}
