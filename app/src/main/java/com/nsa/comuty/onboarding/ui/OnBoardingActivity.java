package com.nsa.comuty.onboarding.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityOnboardingBinding;

public class OnBoardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}