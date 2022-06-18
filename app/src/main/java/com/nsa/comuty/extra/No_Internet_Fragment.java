package com.nsa.comuty.extra;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentNoInternetBinding;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link No_Internet_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class No_Internet_Fragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public No_Internet_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment No_Internet_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static No_Internet_Fragment newInstance(String param1, String param2) {
        No_Internet_Fragment fragment = new No_Internet_Fragment();
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

    private FragmentNoInternetBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getDialog() != null && getDialog().getWindow()!= null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        }

        binding =FragmentNoInternetBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    public interface Retry{
        void onRetry();
    }
    private Retry retry;

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry.onRetry();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getDialog().getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.85);
        int dialogWindowHeight = (int) (displayHeight * 0.6);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        getDialog().getWindow().setAttributes(layoutParams);
    }
}