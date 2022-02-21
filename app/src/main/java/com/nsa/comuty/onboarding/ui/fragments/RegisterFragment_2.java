package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.HOME;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.CountryCodeLayoutBinding;
import com.nsa.comuty.databinding.FragmentRegister2Binding;
import com.nsa.comuty.onboarding.adapters.CollegeNameAdapter;
import com.nsa.comuty.onboarding.extra.Keyboard;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.interfaces.CollegeClickListener;
import com.nsa.comuty.onboarding.models.CollegeModel;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment_2.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment_2 newInstance(String param1, String param2) {
        RegisterFragment_2 fragment = new RegisterFragment_2();
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

    private FragmentRegister2Binding binding;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRegister2Binding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    private CountryCodeLayoutBinding collegeNameBinding;
    private BottomSheetBehavior sheetBehavior;
    private CollegeNameAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        View collegeNameView= binding.coordinator.findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(collegeNameView);
        collegeNameBinding=CountryCodeLayoutBinding.bind(collegeNameView);

        binding.collegeNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(view);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SavedText(getContext()).setText(GO_TO,HOME);
            }
        });
        initCollegeNameLayout();

}
    private void initCollegeNameLayout() {
        adapter=new CollegeNameAdapter(getContext());
        adapter.setListener(new CollegeClickListener() {
            @Override
            public void onClick(CollegeModel model) {
                Keyboard.hide(collegeNameBinding.searchED);
                collegeNameBinding.close.callOnClick();
                binding.collegeNameTV.setText(model.getCollegeName());
            }
        });
        collegeNameBinding.countryCodeRecyclerView.setAdapter(adapter);
        collegeNameBinding.countryCodeRecyclerView.addItemDecoration(new Space(5));
        collegeNameBinding.searchED.addTextChangedListener(new TextWatcher() {
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
                    List<CollegeModel> list=new ArrayList<>();
                    for(CollegeModel model:adapter.getSearchList()){
                        if(model.getCollegeName().toLowerCase().contains(s) ){
                            list.add(model);
                        }
                    }
                    adapter.setList(list);
                }
                adapter.notifyDataSetChanged();
            }
        });
        collegeNameBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }}
class Space extends RecyclerView.ItemDecoration{
    int space;

    public Space(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left=space;
        outRect.right=space;
        if(parent.getChildLayoutPosition(view)==0)
            outRect.top=space;
        outRect.bottom=space;
    }
}