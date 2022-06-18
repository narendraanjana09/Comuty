package com.nsa.comuty.extra;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentNoInternetBinding;
import com.nsa.comuty.databinding.FragmentSpinnerBinding;
import com.nsa.comuty.home.adapters.ViewPagerAdapter;
import com.nsa.comuty.onboarding.interfaces.IndexClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Spinner_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Spinner_Fragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private IndexClickListener indexClickListener;
    private List<String> list;
    public Spinner_Fragment() {
        // Required empty public constructor
    }
    public Spinner_Fragment(IndexClickListener indexClickListener,List<String> list) {
        this.indexClickListener=indexClickListener;
        this.list=list;
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
    public static Spinner_Fragment newInstance(String param1, String param2) {
        Spinner_Fragment fragment = new Spinner_Fragment();
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

    private FragmentSpinnerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getDialog() != null && getDialog().getWindow()!= null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        }


        binding =FragmentSpinnerBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    private ViewPager2Adapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setDialogHW();
        if(list==null || list.isEmpty()){
            Toast.makeText(getContext(), "list can't be empty", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }

        adapter=new ViewPager2Adapter(getContext(),list);
        binding.viewpager.setAdapter(adapter);
        setOffSet();
        binding.okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexClickListener.onClick(binding.viewpager.getCurrentItem());
            }
        });
        binding.closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });





    }

    private void setDialogHW() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getDialog().getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.85);
        int dialogWindowHeight = (int) (displayHeight * 0.35);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        getDialog().getWindow().setAttributes(layoutParams);
    }

    private int lastPos=0,newPos=0;
    private void setOffSet() {
        binding.viewpager.setClipToPadding(false);
        binding.viewpager.setClipChildren(false);
        binding.viewpager.setOffscreenPageLimit(3);
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapter.setSelected(position);
                lastPos=newPos;
                newPos=position;
                if(lastPos<newPos){
                    adapter.notifyItemRangeChanged(lastPos,2);
                }else{
                    adapter.notifyItemRangeChanged(newPos,2);
                }

            }
        });

        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        int offsetPx = getResources().getDimensionPixelOffset(R.dimen.offset);
        binding.viewpager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                ViewPager2 viewPager =(ViewPager2) page.getParent().getParent();
                float offset = position * -(2 * offsetPx + pageMarginPx);
                if (viewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-offset);
                    } else {
                        page.setTranslationX(offset);
                    }
                } else {
                    page.setTranslationY(offset);
                }
            }
        });

    }





    class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

        List<String> list;
        private Context ctx;



        ViewPager2Adapter(Context ctx, List<String> list) {
            this.ctx = ctx;
            this.list=list;
        }

        // This method returns our layout
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.textview, parent, false);
            return new ViewHolder(view);
        }

        // This method binds the screen with the view
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // This will set the images in imageview
            holder.setData(list.get(position));
        }

        // This Method returns the size of the Array
        @Override
        public int getItemCount() {
            return list.size();
        }

        private int selected=0;
        public void setSelected(int position) {
            this.selected=position;
        }

        // The ViewHolder class holds the view
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.txt_spinner);
            }

            public void setData(String s) {

                if(selected==getAbsoluteAdapterPosition()){
                    textView.setText("- "+s+" -");
                    textView.setAlpha(1f);
                    textView.setTextColor(ctx.getResources().getColor(R.color.text_color));
                }else{
                    textView.setText(s);
                    textView.setAlpha(0.5f);
                    textView.setTextColor(ctx.getResources().getColor(R.color.text_hint_color));
                }
            }
        }
    }
}