package com.nsa.comuty.onboarding.ui.fragments;



import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.HOME;
import static com.nsa.comuty.onboarding.extra.Keys.REGISTER;
import static com.nsa.comuty.onboarding.extra.Keys.WITH_FACEBOOK;
import static com.nsa.comuty.onboarding.extra.Keys.WITH_GOOGLE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import android.os.CountDownTimer;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.AcceptTermsDialogBinding;
import com.nsa.comuty.databinding.FragmentWelcomeBinding;
import com.nsa.comuty.extra.CustomDialog;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.home.HomeActivity;
import com.nsa.comuty.onboarding.extra.SavedText;


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
    public static FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        // for google sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getActivity().getString(R.string.google_signin_key))
                .requestEmail()
                .build();
        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(getActivity(), gso);

        mAuth = FirebaseAuth.getInstance();
        database=new Database();






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
                        showToast("error");
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
            showToast("error");
            Log.w("sign_in", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void log(String s) {
        Log.e("sign-in", "log: +"+s );
    }

    private void startFirebaseAuthentication(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showToast("Sign-In Successfull");

                            log("facebook success");
                            showToast("Welcome "+account.getDisplayName());
                            Log.d("firebase sign", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showSuccesDialog(user,account);

                        } else {
                            hideDialogProgess();
                            showToast("error");
                            // If sign in fails, display a message to the user.
                            Log.w("firebase sign", "signInWithCredential:failure", task.getException());
                            Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void showSuccesDialog(FirebaseUser user, GoogleSignInAccount account) {
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
                checkUserData(user,account);
            }
        }.start();
    }

    private void checkUserData(FirebaseUser user, GoogleSignInAccount account) {
        termsDialogBinding.infoTV.setText("checking your info...");
        database.getReferenceUsers().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if(snapshot.exists()){
                    new SavedText(getContext()).setText(GO_TO,HOME);
                    startActivity(new Intent(getActivity(),HomeActivity.class));
                    getActivity().finish();
                }else{
                    new SavedText(getContext()).setText(GO_TO,REGISTER);
                   navController.navigate(R.id.action_welcomeFragment_to_registerFragment);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("error");
                hideDialogProgess();
            }

        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

    }
}