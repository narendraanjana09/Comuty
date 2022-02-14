package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.WITH_FACEBOOK;
import static com.nsa.comuty.onboarding.extra.Keys.WITH_GOOGLE;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentWelcomeBinding;


public class WelcomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WelcomeFragment() {
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

    private FragmentWelcomeBinding binding;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentWelcomeBinding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        binding.withGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(WITH_GOOGLE);
            }
        });
        binding.withFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(WITH_FACEBOOK);
            }
        });
        binding.withPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_welcomeFragment_to_phoneFragment_1);
            }
        });




    }
    private void showDialog(int with){
        Dialog dialog = new Dialog(getContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.accept_terms_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView privacyTXT=dialog.findViewById(R.id.privacyPolicyBTN);
        MaterialCheckBox checkBox=dialog.findViewById(R.id.checkbox);
        ExtendedFloatingActionButton continueBTN=dialog.findViewById(R.id.continueBTN);
        privacyTXT.setText(Html.fromHtml("<u>Privacy Policy</u>"));
        privacyTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("navigate to privacy policy page");
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    continueBTN.setAlpha(1f);
                    continueBTN.setEnabled(true);
                }else{
                    continueBTN.setAlpha(0.5f);
                    continueBTN.setEnabled(false);
                }
            }
        });
        continueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                navController.navigate(R.id.action_welcomeFragment_to_successFragment);
            }
        });



        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

    }
}