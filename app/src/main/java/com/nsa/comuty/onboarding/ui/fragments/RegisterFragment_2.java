package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.COLLEGE;
import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.HOME;
import static com.nsa.comuty.onboarding.extra.Keys.NAME;
import static com.nsa.comuty.onboarding.extra.Keys.PROFILE_IMAGE;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.CountryCodeLayoutBinding;
import com.nsa.comuty.databinding.FragmentRegister2Binding;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.extra.Storage;
import com.nsa.comuty.home.HomeActivity;
import com.nsa.comuty.onboarding.adapters.CollegeNameAdapter;
import com.nsa.comuty.onboarding.extra.DownloadImage;
import com.nsa.comuty.onboarding.extra.Keyboard;
import com.nsa.comuty.onboarding.extra.ProgressDialog;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.interfaces.CollegeClickListener;
import com.nsa.comuty.onboarding.interfaces.DownloadListener;
import com.nsa.comuty.onboarding.models.CollegeModel;
import com.nsa.comuty.onboarding.models.UserCollegeModel;
import com.nsa.comuty.onboarding.models.UserModel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

    String name,dob,bio,image,college="",branch="",year="",section,enrollment,gradYear;
    boolean isLink;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             name=bundle.getString("name", "");
             dob=bundle.getString("dob","");
             bio=bundle.getString("bio","");
             image=bundle.getString("image","");
             isLink=bundle.getBoolean("isLink");
        }
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
    private List<String> graduationList;
    private GoogleSignInAccount account;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        account= com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getActivity());
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog();

        //----Branch Spinner-----//
        String[] branches=getResources().getStringArray(R.array.branch);
        String[] years=getResources().getStringArray(R.array.studyYear);
        String[] sections=getResources().getStringArray(R.array.section);
        ArrayAdapter<String> branchAdapter= new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, branches);
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        binding.branchSpinner.setAdapter(branchAdapter);
        binding.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branch=branches[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.branchSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Keyboard.hide(view);
                return false;
            }
        });

        //----------//

        // ----Year Spinner-----//
        ArrayAdapter<String> yearAdapter= new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.studyYear));
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        binding.yearSpinner.setAdapter(yearAdapter);
        binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year=years[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.yearSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Keyboard.hide(view);
                return false;
            }
        });
        //----------//

        // ----Section Spinner-----//
        ArrayAdapter<String> sectionAdapter= new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.section));
        sectionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        binding.sectionSpinner.setAdapter(sectionAdapter);
        binding.sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                section=sections[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.sectionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Keyboard.hide(view);
                return false;
            }
        });
        //----------//

        // ----Graduation Year Spinner-----//
        graduationList=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int yr = calendar.get(Calendar.YEAR);
        for(int i=yr;i<=yr+4;i++){
            graduationList.add(i+"");
        }

        ArrayAdapter<String> graduationYearAdapter= new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item,graduationList);
        graduationYearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        binding.graduationYearSpinner.setAdapter(graduationYearAdapter);
        binding.graduationYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gradYear=graduationList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.graduationYearSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Keyboard.hide(view);
                return false;
            }
        });
        //----------//

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
                String enrollment=binding.enrollmentED.getText().toString().trim();
                String email="";
                if(account!=null){
                    email=account.getEmail();
                }
                if(check(college,branch,year)){

                    progressDialog.show(getParentFragmentManager(),"tag");
                    UserModel userModel=new UserModel(name,dob,email, firebaseUser.getUid(), bio,"",college,branch,year,section,enrollment,gradYear);
                    uploadImage(userModel);

                }
            }
        });
        initCollegeNameLayout();


}

    private void uploadImage(UserModel userModel) {
        if(isLink){
            new DownloadImage(new DownloadListener() {
                @Override
                public void OnDownloaded(InputStream inputStream) {
                    startUpload(inputStream,userModel);
                }
            }).execute(image);
        }else{
            try {
                InputStream  inputStream = getContext().getContentResolver().openInputStream(Uri.parse(image));
                startUpload(inputStream,userModel);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showToast("error");
                progressDialog.dismiss();
            }
        }
    }

    private void startUpload(InputStream inputStream, UserModel userModel) {
        StorageReference ref=new Storage().getCollegeRef().child(userModel.getCollege()).child("users/"+firebaseUser.getUid()+"/profile.jpg");
        UploadTask uploadTask = ref.putStream(inputStream);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    showToast("error");
                    progressDialog.dismiss();
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    userModel.setProfileUrl(downloadUri.toString());
                    Log.e("TAG", "imageLink: "+downloadUri);
                    uploadUserData(userModel);
                } else {
                    showToast("error");
                    progressDialog.dismiss();
                }
            }
        });
    }

    private Database database;
    private void uploadUserData(UserModel userModel) {
        database=new Database();
        database.getReferenceColleges().child(userModel.getCollege()).child("users").child(firebaseUser.getUid())
                .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UserCollegeModel model=new UserCollegeModel(userModel.getEmail(),userModel.getCollege());
                database.getReferenceUsersCollege().child(firebaseUser.getUid()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showToast("Profile Created!");
                        progressDialog.dismiss();
                        new SavedText(getContext()).setText(GO_TO,HOME);
                        new SavedText(getContext()).setText(COLLEGE,userModel.getCollege());
                        new SavedText(getContext()).setText(NAME,userModel.getName());
                        new SavedText(getContext()).setText(PROFILE_IMAGE,userModel.getProfileUrl());
                        Intent intent=new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        showToast("error");
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                showToast("error");
                progressDialog.dismiss();
            }
        });
    }


    private boolean check(String college, String branch, String year) {
        if(college.isEmpty()){
            showToast("please select your college!");
            return false;
        }
        if(branch.isEmpty()){
            showToast("please select your branch!");
            return false;
        }
        if(year.isEmpty()){
            showToast("please select your year!");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
    private void initCollegeNameLayout() {
        college=new SavedText(getContext()).getText(COLLEGE);
        binding.collegeNameTV.setText(college);
        binding.collegeNameTV.setEnabled(false);
        adapter=new CollegeNameAdapter(getContext());
        adapter.setListener(new CollegeClickListener() {
            @Override
            public void onClick(CollegeModel model) {
                Keyboard.hide(collegeNameBinding.searchED);
                collegeNameBinding.close.callOnClick();
                college=model.getCollegeName();
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

// For Spacing between Recycler view items
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