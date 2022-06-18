package com.nsa.comuty.onboarding.ui.fragments;


import static com.nsa.comuty.onboarding.extra.Keys.COLLEGE;
import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.HOME;
import static com.nsa.comuty.onboarding.extra.Keys.NAME;
import static com.nsa.comuty.onboarding.extra.Keys.PROFILE_IMAGE;
import static com.nsa.comuty.onboarding.extra.Keys.REGISTER;
import static com.nsa.comuty.onboarding.extra.Keys.WITH_FACEBOOK;
import static com.nsa.comuty.onboarding.extra.Keys.WITH_GOOGLE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.BuildConfig;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.AcceptTermsDialogBinding;
import com.nsa.comuty.databinding.CountryCodeLayoutBinding;
import com.nsa.comuty.databinding.FragmentWelcomeBinding;
import com.nsa.comuty.extra.CustomDialog;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.extra.NetworkStatus;
import com.nsa.comuty.home.HomeActivity;
import com.nsa.comuty.onboarding.adapters.CollegeNameAdapter;
import com.nsa.comuty.onboarding.extra.Keyboard;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.interfaces.CollegeClickListener;
import com.nsa.comuty.onboarding.models.CollegeModel;
import com.nsa.comuty.onboarding.models.UserCollegeModel;
import com.nsa.comuty.onboarding.models.UserModel;
import com.nsa.comuty.onboarding.ui.OnBoardingActivity;

import java.util.ArrayList;
import java.util.List;





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
    private Database database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentWelcomeBinding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }
    private FirebaseAuth mfirebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        new NetworkStatus(getActivity(),getParentFragmentManager()).startInternetCheck();

        // for google sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mAuth = FirebaseAuth.getInstance();
        database=new Database();






        binding.withGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(college.isEmpty()){
                    showToast("Please Select Your College!");
                    return;
                }
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

        View collegeNameView= binding.coordinator.findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(collegeNameView);
        collegeNameBinding=CountryCodeLayoutBinding.bind(collegeNameView);

        binding.txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hide(view);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        initCollegeNameLayout();




    }
    private CountryCodeLayoutBinding collegeNameBinding;
    private BottomSheetBehavior sheetBehavior;
    private CollegeNameAdapter adapter;
    private String college="";
    private void initCollegeNameLayout() {
        adapter=new CollegeNameAdapter(getContext());
        adapter.setListener(new CollegeClickListener() {
            @Override
            public void onClick(CollegeModel model) {
                Keyboard.hide(collegeNameBinding.searchED);
                collegeNameBinding.close.callOnClick();
                college=model.getCollegeName();
                binding.txt2.setText(model.getCollegeName());
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
    }
    private String TAG="welcome";


    private AcceptTermsDialogBinding termsDialogBinding;
    private CustomDialog dialog;
    private void showDialog(int with){
         dialog = new CustomDialog(getContext(), R.style.DialogStyle);
        termsDialogBinding=AcceptTermsDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(termsDialogBinding.getRoot());
        dialog.setHeightWidth(getActivity(),0.8f,0.25f);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);


        termsDialogBinding.privacyPolicyBTN.setText(Html.fromHtml("<u>Privacy Policy</u>"));
        termsDialogBinding.privacyPolicyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("navigate to privacy policy page");
            }
        });
        termsDialogBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    termsDialogBinding.continueBTN.setAlpha(1f);
                    termsDialogBinding.continueBTN.setEnabled(true);
                }else{
                    termsDialogBinding.continueBTN.setAlpha(0.5f);
                    termsDialogBinding.continueBTN.setEnabled(false);
                }
            }
        });
        termsDialogBinding.continueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setCancelable(false);
                showDialogProgress();
                continueGoogleSignIn();
            }
        });



        dialog.show();
    }

    private void showDialogProgress() {
        termsDialogBinding.cbLayout.setVisibility(View.GONE);
        termsDialogBinding.continueBTN.setVisibility(View.GONE);
        termsDialogBinding.progressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialogProgess() {
        dialog.setCancelable(true);
        termsDialogBinding.cbLayout.setVisibility(View.VISIBLE);
        termsDialogBinding.continueBTN.setVisibility(View.VISIBLE);
        termsDialogBinding.progressBar.setVisibility(View.GONE);
    }


    private void continueGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        googleSign_in.launch(signInIntent);

    }
    ActivityResultLauncher<Intent> googleSign_in = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }else{
                        hideDialogProgess();
                        Log.e(TAG, "onActivityResult: error1");
                    }
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            log("google success");
            startFirebaseAuthentication(account);
        } catch (ApiException e) {
            hideDialogProgess();
            showToast("error2");
            log( "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void log(String s) {
        Log.e(TAG, "log: +"+s );
    }

    private void startFirebaseAuthentication(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            log("facebook success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserData(user,account);


                        } else {
                            hideDialogProgess();
                            showToast("error3");
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void showSuccesDialog(int goToHome) {
       CustomDialog alertDialog = new CustomDialog(getContext(), R.style.DialogStyle);
        alertDialog.setContentView(R.layout.fragment_success_sign_in);
        alertDialog.setHeightWidth(getActivity(),1f,1f);
        alertDialog.show();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
               alertDialog.dismiss();
               if(goToHome==GO_TO_HOME){
                   goToHome();
               }else{
                   goToRegister();
               }

            }
        }.start();
    }
    private static int GO_TO_HOME=101;
    private static int GO_TO_REGISTER=102;

    private void checkUserData(FirebaseUser user, GoogleSignInAccount account) {
        termsDialogBinding.infoTV.setText("checking your info...");
        database.getReferenceColleges().child(college).child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    dialog.dismiss();
                    new SavedText(getContext()).setText(GO_TO,HOME);
                    UserModel userModel=snapshot.getValue(UserModel.class);
                    new SavedText(getContext()).setText(COLLEGE,userModel.getCollege());
                    new SavedText(getContext()).setText(NAME,userModel.getName());
                    new SavedText(getContext()).setText(PROFILE_IMAGE,userModel.getProfileUrl());
                  showSuccesDialog(GO_TO_HOME);
                }else{
                   checkInOtherColleges(account.getEmail());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("error4");
                Log.e(TAG, "onCancelled: "+error.toString() );
                hideDialogProgess();
            }

        });
    }

    private void goToHome() {
        startActivity(new Intent(getActivity(),HomeActivity.class));
        getActivity().finish();
    }

    private void checkInOtherColleges(String email) {
        Query queryToGetData = database.getReferenceUsersCollege()
                .orderByChild("email").equalTo(email);
        queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserCollegeModel model=null;
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    model=snapshot.getValue(UserCollegeModel.class);
                    }
                    logout();
                    showToast("Email already exist for college\n"+model.getCollege());
                    hideDialogProgess();
                }else{
                    showSuccesDialog(GO_TO_REGISTER);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("error");
            }
        });

    }

    private void goToRegister() {
        dialog.dismiss();
        new SavedText(getContext()).setText(COLLEGE,college);
        new SavedText(getContext()).setText(GO_TO,REGISTER);
        navController.navigate(R.id.action_welcomeFragment_to_registerFragment);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

    }
    private void logout() {
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseAuth.getInstance().signOut();
                log("logout successfull");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                log(e.getMessage());
            }
        });
    }
}