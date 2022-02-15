package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.REGISTER_DONE;
import static com.nsa.comuty.onboarding.extra.Keys.SIGN_IN_DONE;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentSuccessRegisterBinding;
import com.nsa.comuty.databinding.FragmentSuccessSignInBinding;
import com.nsa.comuty.home.HomeActivity;
import com.nsa.comuty.onboarding.extra.SavedText;


public class RegisterSuccessFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterSuccessFragment() {
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

    private FragmentSuccessRegisterBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentSuccessRegisterBinding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        new SavedText(getContext()).setText(REGISTER_DONE,"yes");
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }
        }.start();
    }
}