package com.nsa.comuty.onboarding.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.databinding.FragmentRegister1Binding;
import com.nsa.comuty.extra.Util;
import com.nsa.comuty.extra.Zoom_Image_Dialog;
import com.nsa.comuty.home.models.DateModel;
import com.nsa.comuty.onboarding.models.ImageModel;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Calendar;


import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RegisterFragment_1 extends Fragment implements OnMenuItemClickListener<PowerMenuItem> {

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
    public FirebaseUser fUser;
    private GoogleSignInAccount account;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRegister1Binding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    private ImageModel imageModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        account= com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account==null){
            showToast("error");
        }else{
            binding.profileImageview.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.profileImageview.setImageTintList(null);
            Glide.with(getContext())
                    .load(account.getPhotoUrl())
                    .into(binding.profileImageview);
            binding.nameEd.setText(account.getDisplayName());
            imageModel=new ImageModel(account.getPhotoUrl().toString(),true);
        }

        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.nameEd.getText().toString().trim();
                String dob=binding.dobED.getText().toString().trim();
                String bio=binding.bioED.getText().toString().trim();
                if(name.isEmpty()){
                    showToast("Name can't be empty");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("dob", dob);
                bundle.putString("bio",bio);
                bundle.putString("image",imageModel.getPath());
                bundle.putBoolean("isLink",imageModel.isLink());
                navController.navigate(R.id.action_registerFragment_to_registerFragment_2,bundle);
            }
        });

        getDOB();
       getImage();
    }

    private void getDOB() {
        binding.dobED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String date=binding.dobED.getText().toString().trim();

                Calendar calendar = Calendar.getInstance();
                int yr = calendar.get(Calendar.YEAR);
                int mnth = calendar.get(Calendar.MONTH);
                int dy = calendar.get(Calendar.DAY_OF_MONTH);

                if(!date.isEmpty()){
                    DateModel model= Util.getDate(date);
                    yr= Integer.parseInt(model.getYear());
                    mnth= Integer.parseInt(model.getMonth())-1;
                    dy= Integer.parseInt(model.getDay());
                }
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String s=day+"/"+(month+1)+"/"+year;
                        binding.dobED.setText(s);
                    }
                },yr, mnth,dy);
                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000));
                datePickerDialog.show();
            }
        });
    }


    private void getImage() {
        binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 powerMenu = new PowerMenu.Builder(getContext())
                         .addItem(new PowerMenuItem("view", false))
                        .addItem(new PowerMenuItem("update", false)) // add an item.// aad an item list.
                        .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                        .setMenuRadius(10f) // sets the corner radius.
                        .setMenuShadow(10f) // sets the shadow.
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.text_color))
                        .setTextGravity(Gravity.CENTER)
                        .setTextSize(16)
                        .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                        .setMenuColor(ContextCompat.getColor(getContext(), R.color.background))
                        .setSelectedMenuColor(ContextCompat.getColor(getContext(), R.color.teal_200))
                        .setOnMenuItemClickListener(RegisterFragment_1.this)
                        .build();
                powerMenu.showAsDropDown(view);
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
                       imageModel=new ImageModel(uri.toString(),false);



                        // Handle the Intent
                    }
                }
            });

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }


    PowerMenu powerMenu;
    @Override
    public void onItemClick(int position, PowerMenuItem item) {

        if(position==1){
            ImagePicker.with(getActivity())
                    .cropSquare()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .galleryOnly()
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            getImage.launch(intent);
                            return null;
                        }
                    });
        }else{
            if(imageModel!=null){
                Zoom_Image_Dialog dialog=new Zoom_Image_Dialog(imageModel);
                dialog.show(getParentFragmentManager(),"zoomImage");
            }

        }
        powerMenu.setSelectedPosition(position); // change selected item
        powerMenu.dismiss();
    }
}