package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.INTRO_SCREENS_DONE;
import static com.nsa.comuty.onboarding.extra.Keys.REGISTER;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentIntroScreensBinding;
import com.nsa.comuty.onboarding.adapters.ScreensAdapter;
import com.nsa.comuty.onboarding.extra.SavedText;





public class IntroScreensFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IntroScreensFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private FragmentIntroScreensBinding binding;
    private ScreensAdapter adapter;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentIntroScreensBinding
               .inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        if(new SavedText(getContext()).getText(GO_TO).equals(REGISTER) && FirebaseAuth.getInstance().getCurrentUser()!=null){
            goToRegister();
        }else{
        if(new SavedText(getContext()).getText(INTRO_SCREENS_DONE).equals("1")){
            goToWelcome();
        }}
        adapter=new ScreensAdapter(getContext());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionChanged(position);
            }
        });
        binding.beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPager.setCurrentItem(1,true);
            }
        });
        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem()+1,true);
            }
        });
        binding.prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem()-1,true);
            }
        });
        binding.skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              goToWelcome();
            }
        });
        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWelcome();
            }
        });
        binding.viewPager.setPageTransformer(new DepthPageTransformer());
    }

    private void goToRegister() {
        navController.navigate(R.id.action_introScreensFragment_to_registerFragment);
    }

    private void goToWelcome() {
        new SavedText(getContext()).setText(INTRO_SCREENS_DONE,"1");
        navController.navigate(R.id.action_introScreensFragment_to_welcomeFragment);
    }

    private void positionChanged(int position) {

            if(position==0){
               binding.beginButton.setVisibility(View.VISIBLE);
               binding.startButton.setVisibility(View.GONE);
            }
            if(position==4){
                binding.beginButton.setVisibility(View.GONE);
                binding.startButton.setVisibility(View.VISIBLE);
            }
            if(position!=0 && position !=4){
                binding.beginButton.setVisibility(View.GONE);
                binding.startButton.setVisibility(View.GONE);

                binding.skipbtn.setVisibility(View.VISIBLE);
                binding.nextBTN.setVisibility(View.VISIBLE);
                binding.prevBTN.setVisibility(View.VISIBLE);
            }else{
                binding.skipbtn.setVisibility(View.GONE);
                binding.nextBTN.setVisibility(View.GONE);
                binding.prevBTN.setVisibility(View.GONE);
            }

    }

    private void showToast(String start) {
        Toast.makeText(getContext(), ""+start, Toast.LENGTH_SHORT).show();
    }

    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}