package com.nsa.comuty.onboarding.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.databinding.FragmentRegister1Binding;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment_1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment_1 newInstance(String param1, String param2) {
        RegisterFragment_1 fragment = new RegisterFragment_1();
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

    private FragmentRegister1Binding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRegister1Binding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_registerFragment_2);
            }
        });
        getDOB();
       getImage();
    }

    private void getDOB() {
        binding.dobED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                if(!s.isEmpty()){
                    if((s.charAt(s.length()-1)+"").equals("/")){
                        s = s.substring(0, s.length()-1);
                        binding.dobED.setText(s);
                        binding.dobED.setSelection(s.length());
                    }else {
                        if (s.length() == 3 || s.length() == 6) {
                            s = s.substring(0, s.length()-1) + "/" + s.charAt(s.length()-1);
                            binding.dobED.setText(s);
                            binding.dobED.setSelection(s.length());
                        }
                    }
                }

            }
        });
    }


    private void getImage() {
        binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(getActivity())
                        .cropSquare()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                getImage.launch(intent);
                                return null;
                            }
                        });
            }
        });
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Uri uri = result.getData().getData();
                        binding.profileImageview.setImageURI(uri);
                        binding.profileImageview.setScaleType(ImageView.ScaleType.FIT_XY);
                        binding.profileImageview.setImageTintList(null);



                        // Handle the Intent
                    }
                }
            });

    private void showToast(String uri) {
        Toast.makeText(getContext(), ""+uri, Toast.LENGTH_SHORT).show();
    }



}