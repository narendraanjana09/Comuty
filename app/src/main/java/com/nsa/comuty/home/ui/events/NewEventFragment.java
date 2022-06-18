package com.nsa.comuty.home.ui.events;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.slider.RangeSlider;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentNewEventBinding;
import com.nsa.comuty.databinding.FragmentNewPostBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEventFragment newInstance(String param1, String param2) {
        NewEventFragment fragment = new NewEventFragment();
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

    private FragmentNewEventBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentNewEventBinding
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
        binding.previewCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    binding.preview.getRoot().setVisibility(View.VISIBLE);
                }else{
                    binding.preview.getRoot().setVisibility(View.GONE);
                }
            }
        });

        binding.teamMembersSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                String min=slider.getValues().get(0)+"";
                String max=slider.getValues().get(1)+"";
                min=min.replace(".0","");
                max=max.replace(".0","");
                binding.membersTxt.setText("Team Members("+min
                        +"-"+max+")");
            }
        });
        binding.eventNameED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable editable) {
                binding.nameEDCounter.setText(editable.toString().trim().length()+"/25");
                binding.preview.titleTv.setText(editable. toString().trim());
                if(editable. toString().trim().length()>=2){
                    binding.nameEDCounter.setTextColor(getResources().getColor(R.color.check_box));
                    binding.eventNameED.setTextColor(getResources().getColor(R.color.check_box));
                    binding.eventNameED.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                    binding.eventNameED.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                }else{
                    binding.nameEDCounter.setTextColor(getResources().getColor(R.color.text_hint_color));
                    binding.eventNameED.setTextColor(getResources().getColor(R.color.text_color));
                    binding.eventNameED.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
                    binding.eventNameED.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));

                }
            }
        });
        binding.eventFeesEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable. toString().trim().isEmpty()){
                    binding.preview.feesTv.setText("₹00");
                }else{
                    if(Integer.parseInt(editable. toString().trim())>1000){
                        showToast("Registration fee must be less than 1000!");
                    }
                    binding.preview.feesTv.setText("₹"+editable. toString().trim());
                }
                if(editable. toString().trim().length()>=1){

                    binding.eventFeesEd.setTextColor(getResources().getColor(R.color.check_box));
                    binding.eventFeesEd.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                    binding.eventFeesEd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                }else{

                    binding.eventFeesEd.setTextColor(getResources().getColor(R.color.text_color));
                    binding.eventFeesEd.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
                    binding.eventFeesEd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));

                }


            }
        });
        binding.eventDescpEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable editable) {
                binding.descEDCounter.setText(editable. toString().trim().length()+"/300");
                if(editable. toString().trim().length()>=10){
                    binding.descEDCounter.setTextColor(getResources().getColor(R.color.check_box));
                    binding.eventDescpEd.setTextColor(getResources().getColor(R.color.check_box));
                    binding.eventDescpEd.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                    binding.eventDescpEd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                }else{
                    binding.descEDCounter.setTextColor(getResources().getColor(R.color.text_hint_color));
                    binding.eventDescpEd.setTextColor(getResources().getColor(R.color.text_color));
                    binding.eventDescpEd.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
                    binding.eventDescpEd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));

                }
            }
        });
        binding.preview.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(getActivity())
                        .crop(4f,2f)                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        .galleryOnly()//Final image size will be less than 1 MB(Optional)
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
        getDateTime();
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Uri uri = result.getData().getData();
                        binding.preview.imageView.setImageURI(uri);
                        binding.preview.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        binding.preview.imageView.setImageTintList(null);
                        // Handle the Intent
                    }
                }
            });

    private void showToast(String s) {
        Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
    }


    private void getDateTime() {
        binding.dateTimeEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int yr = calendar.get(Calendar.YEAR);
                int mnth = calendar.get(Calendar.MONTH);
                int dy = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        Calendar c = Calendar.getInstance();
                       int hour = c.get(Calendar.HOUR);
                        int minute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hr, int min) {

                                binding.dateTimeEd.setText(getDateTimeString(day,month+1,year,hr,min));
                                binding.dateTimeEd.setTextColor(getResources().getColor(R.color.check_box));
                                binding.dateTimeEd.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                                binding.dateTimeEd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.check_box)));
                                runTimer(year,month+1,day,hr,min);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getContext()));
                        timePickerDialog.show();
                    }
                },yr, mnth,dy);
                datePickerDialog.getDatePicker().setMinDate((System.currentTimeMillis() - 1000)+(86400000));
                datePickerDialog.show();
            }
        });
    }

    private Handler handler;
    private Runnable runnable;
    private void runTimer(int year, int month, int day, int hr, int min) {

        if(handler!=null && runnable!=null){
            handler.removeCallbacks(runnable);
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse(year+"-"+month+"-"+day+" "+hr+":"+(min+1));

                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        String day="" + String.format("%02d", days);
                        String hr="" + String.format("%02d", hours);
                        String minu=""
                                + String.format("%02d", minutes);
                        String sec=""
                                + String.format("%02d", seconds);

                        if(day.equals("00")){
                            day="";
                        }else{
                            day=day+" days ";
                        }

                        if(hr.equals("00")){
                            hr="";
                        }else{
                            hr=hr+" hours ";
                        }

                        binding.preview.timerTv.setText(day+hr+minu+" minutes left");

                    } else {
                        binding.preview.timerTv.setText("registration date is over");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "run: "+e.getMessage() );
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);

    }

    private String getDateTimeString(int dy, int mnt, int year, int hr, int min) {
        String amPm="AM";
        if(hr>12){
            hr=hr-12;
            amPm="PM";
        }

        String hour=hr+"";
        String minutes=min+"";
        if(hour.length()==1){
            hour="0"+hour;
        }
        if(minutes.length()==1){
            minutes="0"+minutes;
        }

        String day=dy+"";
        String month=mnt+"";
        if(day.length()==1){
            day="0"+day;
        }
        if(month.length()==1){
            month="0"+month;
        }

        return day+"/"+month+"/"+year+" "+hour+":"+minutes+" "+amPm;
    }
}

