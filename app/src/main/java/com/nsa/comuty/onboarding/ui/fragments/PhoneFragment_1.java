package com.nsa.comuty.onboarding.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.CountryCodeLayoutBinding;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.onboarding.adapters.CountryCodeAdapter;
import com.nsa.comuty.onboarding.extra.Keyboard;
import com.nsa.comuty.onboarding.interfaces.CountryCodeClickListener;
import com.nsa.comuty.onboarding.models.CountryModel;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneFragment_1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneFragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneFragment_1 newInstance(String param1, String param2) {
        PhoneFragment_1 fragment = new PhoneFragment_1();
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

    private FragmentPhone1Binding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentPhone1Binding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    private CountryCodeLayoutBinding countrycodeBinding;
    private BottomSheetBehavior sheetBehavior;
   private CountryCodeAdapter adapter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        View countryCodeView= binding.coordinator.findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(countryCodeView);
        countrycodeBinding=CountryCodeLayoutBinding.bind(countryCodeView);
        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        binding.privacyPolicyBTN.setText(Html.fromHtml("<u>Privacy Policy.</u>"));
        binding.countryCodeTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(view);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        binding.phoneED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString()+"";
                checkBTN();
                if(s.length()==10){
                    binding.phoneED.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getContext().getDrawable(R.drawable.ic_baseline_done_24),null);
                }else{
                    binding.phoneED.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
                binding.phoneED.requestLayout();
            }
        });
        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkBTN();
            }
        });
        binding.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(view);
            }
        });
        binding.sendOTPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_phoneFragment_1_to_phoneFragment_2);
            }
        });
       initCountyCodeLayout();
    }

    private void checkBTN() {
        if(binding.checkbox.isChecked() && binding.phoneED.getText().toString().length()==10){
            binding.sendOTPBTN.setEnabled(true);
            binding.sendOTPBTN.setAlpha(1f);
        }else{
            binding.sendOTPBTN.setEnabled(false);
            binding.sendOTPBTN.setAlpha(0.5f);
        }
    }

    private void initCountyCodeLayout() {
        adapter=new CountryCodeAdapter(getContext());
        adapter.setListener(new CountryCodeClickListener() {
            @Override
            public void onClick(CountryModel model) {
                Keyboard.hide(countrycodeBinding.searchED);
                countrycodeBinding.close.callOnClick();
                binding.countryCodeTXT.setText(model.getPhoneCode());
            }
        });
        countrycodeBinding.countryCodeRecyclerView.setAdapter(adapter);
        countrycodeBinding.searchED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=(editable.toString()+"").toLowerCase();
                if(s.isEmpty()){
                    adapter.setList(adapter.getSearchList());
                }else{
                    List<CountryModel> list=new ArrayList<>();
                    for(CountryModel model:adapter.getSearchList()){
                        if(model.getCountryName().toLowerCase().contains(s) || model.getPhoneCode().contains(s)){
                            list.add(model);
                        }
                    }
                    adapter.setList(list);
                }
                adapter.notifyDataSetChanged();
            }
        });
        countrycodeBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
}