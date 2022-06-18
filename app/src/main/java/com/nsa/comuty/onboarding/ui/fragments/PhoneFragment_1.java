package com.nsa.comuty.onboarding.ui.fragments;

import static com.nsa.comuty.onboarding.extra.Keys.GO_TO;
import static com.nsa.comuty.onboarding.extra.Keys.HOME;
import static com.nsa.comuty.onboarding.extra.Keys.REGISTER;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.AcceptTermsDialogBinding;
import com.nsa.comuty.databinding.CountryCodeLayoutBinding;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.databinding.FragmentPhone2Binding;
import com.nsa.comuty.databinding.ProgressLayoutBinding;
import com.nsa.comuty.extra.CustomDialog;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.home.HomeActivity;
import com.nsa.comuty.onboarding.adapters.CountryCodeAdapter;
import com.nsa.comuty.onboarding.extra.Keyboard;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.interfaces.CountryCodeClickListener;
import com.nsa.comuty.onboarding.models.CountryModel;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import in.aabhasjindal.otptextview.OTPListener;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private static final String TAG = "PhoneAuth";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private CustomDialog success_dialog;
    private void showSuccesDialog(FirebaseUser user) {
        success_dialog = new CustomDialog(getContext(), R.style.DialogStyle);
        success_dialog.setContentView(R.layout.fragment_success_sign_in);
        success_dialog.setHeightWidth(getActivity(),1f,1f);
        success_dialog.show();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                success_dialog.dismiss();
                checkUserData(user);
                progressLayoutBinding.textview.setText("connecting...");
            }
        }.start();
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
           log("onVerificationCompleted:" + credential);
            if(otp_dialog!=null){
                otp_dialog.dismiss();
            }
            showToast("Phone Verified Successfully");
            showProgress();
            progressLayoutBinding.textview.setText("connecting...");
            otp_binding.otpLayout.setOTP(credential.getSmsCode());
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            log("onVerificationFailed"+ e);
            showToast("error");
            progress_dialog.dismiss();
            if(otp_dialog!=null){
                otp_dialog.dismiss();
            }


            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                showToast(" Invalid request");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                showToast("he SMS quota for the project has been exceeded");
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            log("onCodeSent:" + verificationId);
            progressLayoutBinding.textview.setText("code sent successfully.");
            progress_dialog.dismiss();
            showToast("otp sent");
            showOTPDialog();
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };
    private String getPhoneNumber(){
        return countryModel.getPhoneCode()+binding.phoneED.getText().toString();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            log( "signInWithCredential:success");

                            showToast("Verification Success");
                            FirebaseUser user = task.getResult().getUser();
                            showSuccesDialog(user);
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI

                            progress_dialog.dismiss();


                            log( "signInWithCredential:failure"+ task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showToast("invalid otp!");
                            }
                        }
                    }
                });
    }

    private void checkUserData(FirebaseUser user) {
        new Database().getReferenceColleges().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progress_dialog.dismiss();
                otp_dialog.dismiss();
                success_dialog.dismiss();
                if(snapshot.exists()){
                    new SavedText(getContext()).setText(GO_TO,HOME);
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                }else{
                    new SavedText(getContext()).setText(GO_TO,REGISTER);
                    navController.navigate(R.id.action_phoneFragment_1_to_registerFragment);
                    navController.clearBackStack(R.id.phoneFragment_1);
                    navController.clearBackStack(R.id.introScreensFragment);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("error");
                progress_dialog.dismiss();
                otp_dialog.dismiss();
            }
        });
    }
    private String previousNumber="";

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();



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
                if(isValidMobileNo(s)){
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
                if(previousNumber.equals(getPhoneNumber())){
                    showOTPDialog();
                }else{
                if(isValidMobileNo(binding.phoneED.getText().toString())){
                    previousNumber=getPhoneNumber();
                    showProgress();
                    startPhoneNumberVerification(getPhoneNumber());
                }else{
                    showToast("Please Enter Correct Phone Number!");
                }}
            }
        });
       initCountyCodeLayout();
    }
    private FragmentPhone2Binding otp_binding;
    private CustomDialog otp_dialog;
    private void showOTPDialog() {
        otp_dialog = new CustomDialog(getContext(), R.style.DialogStyle);
        otp_binding=FragmentPhone2Binding.inflate(getLayoutInflater());
        otp_dialog.setContentView(otp_binding.getRoot());
        otp_dialog.setHeightWidth(getActivity(),1f,1f);
        otp_dialog.show();

        otp_binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_dialog.dismiss();
            }
        });
        otp_binding.changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_dialog.dismiss();
            }
        });
        otp_binding.verifyOTPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                progressLayoutBinding.textview.setText("verifying..");
                verifyPhoneNumberWithCode(mVerificationId,otp_binding.otpLayout.getOTP());
            }
        });
        otp_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                countDownTimer.cancel();
            }
        });
        otp_binding.changeNumber.setText(Html.fromHtml("<u>change number?</u>"));
        otp_binding.txt2.setText("We have sent a  6 digit verification code\nto your mobile number "+getPhoneNumber());
        startTimer();
        checkOTPs();

    }
    private void checkOTPs() {

        otp_binding.otpLayout.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                otp_binding.verifyOTPBTN.setEnabled(false);
                otp_binding.verifyOTPBTN.setAlpha(0.5f);
            }

            @Override
            public void onOTPComplete(String otp) {
                otp_binding.verifyOTPBTN.setEnabled(true);
                otp_binding.verifyOTPBTN.setAlpha(1f);
            }
        });
    }

    private CountDownTimer countDownTimer;
    private void startTimer() {
        countDownTimer= new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                String timer=l/1000+"";
                otp_binding.resendBTN.setText("Resend code in "+timer);
            }

            @Override
            public void onFinish() {
                otp_binding.resendBTN.setText(Html.fromHtml("<u>resend code?</u>"));
                otp_binding.resendBTN.setTextColor(getResources().getColor(R.color.text_color));
                otp_binding.resendBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otp_dialog.dismiss();
                        showProgress();
                        progressLayoutBinding.textview.setText("resending otp...");
                        resendVerificationCode(getPhoneNumber()
                        ,mResendToken);
                    }
                });

            }
        };
        countDownTimer.start();
    }

    public boolean isValidMobileNo(String str)
    {
        if(str.equals("1234567899")){
            return true;
        }
        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher match = ptrn.matcher(str);
        return (match.find() && match.group().equals(str));
    }
    private ProgressLayoutBinding progressLayoutBinding;
    private CustomDialog progress_dialog;
    private void showProgress() {
        progress_dialog = new CustomDialog(getContext(), R.style.DialogStyle);
        progressLayoutBinding=ProgressLayoutBinding.inflate(getLayoutInflater());
        progress_dialog.setContentView(progressLayoutBinding.getRoot());
        progressLayoutBinding.textview.setText("Sending code...");
        progress_dialog.setHeightWidth(getActivity(),1f,1f);
        progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress_dialog.show();

    }

    public void log(String message){
        Log.e(TAG, "log: "+message);
    }



    private void showToast(String time) {
        Toast.makeText(getContext(),time+"",Toast.LENGTH_SHORT).show();
    }

    private void checkBTN() {
        if(binding.checkbox.isChecked() && isValidMobileNo(binding.phoneED.getText().toString())){
            binding.sendOTPBTN.setEnabled(true);
            binding.sendOTPBTN.setAlpha(1f);
        }else{
            binding.sendOTPBTN.setEnabled(false);
            binding.sendOTPBTN.setAlpha(0.5f);
        }
    }

    private CountryModel countryModel=new CountryModel("in", "India", "+91", "🇮🇳");
    private void initCountyCodeLayout() {
        adapter=new CountryCodeAdapter(getContext());
        adapter.setListener(new CountryCodeClickListener() {
            @Override
            public void onClick(CountryModel model) {
                countryModel=model;
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
                Keyboard.hide(countrycodeBinding.searchED);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
}