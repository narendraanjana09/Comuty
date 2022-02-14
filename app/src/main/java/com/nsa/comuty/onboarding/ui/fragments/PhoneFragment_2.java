package com.nsa.comuty.onboarding.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.databinding.FragmentPhone2Binding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneFragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneFragment_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneFragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneFragment_2.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneFragment_2 newInstance(String param1, String param2) {
        PhoneFragment_2 fragment = new PhoneFragment_2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentPhone2Binding binding;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentPhone2Binding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
        binding.verifyOTPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_phoneFragment_2_to_successFragment);
            }
        });
        setUpOTPViews();

    }

    private void setUpOTPViews() {
        binding.otpED1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){

                    binding.otpED2.requestFocus();
                }
            }
        });
        binding.otpED2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){
                    binding.otpED3.requestFocus();
                }else{
                    binding.otpED1.requestFocus();
                }
            }
        });
        binding.otpED3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){
                    binding.otpED4.requestFocus();
                }else{
                    binding.otpED2.requestFocus();
                }
            }
        });
        binding.otpED4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){
                    binding.otpED5.requestFocus();
                }else{
                    binding.otpED3.requestFocus();
                }
            }
        });
        binding.otpED5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){

                    binding.otpED6.requestFocus();
                }else{
                    binding.otpED4.requestFocus();
                }
            }
        });
        binding.otpED6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkOTPs();
                if(s.length()==1){
                }else{
                    binding.otpED5.requestFocus();
                }
            }
        });
    }

    private void checkOTPs() {
        String otp1=binding.otpED1.getText().toString();
        String otp2=binding.otpED2.getText().toString();
        String otp3=binding.otpED3.getText().toString();
        String otp4=binding.otpED4.getText().toString();
        String otp5=binding.otpED5.getText().toString();
        String otp6=binding.otpED6.getText().toString();
        if(otp1.isEmpty() || otp2.isEmpty() ||
                otp3.isEmpty() || otp4.isEmpty() ||
                otp5.isEmpty() || otp6.isEmpty()){
            binding.verifyOTPBTN.setEnabled(false);
            binding.verifyOTPBTN.setAlpha(0.5f);
        }else{
            binding.verifyOTPBTN.setEnabled(true);
            binding.verifyOTPBTN.setAlpha(1f);
        }
    }
}