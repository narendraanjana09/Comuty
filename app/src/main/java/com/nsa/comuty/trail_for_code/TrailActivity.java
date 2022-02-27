package com.nsa.comuty.trail_for_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.imageview.ShapeableImageView;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityTrailBinding;

import java.util.ArrayList;
import java.util.List;

public class TrailActivity extends AppCompatActivity {

    private ActivityTrailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MainAdapter adapter = new MainAdapter( numberImage(), R.layout.post_stack_image_item, this);
        binding.stackView.setAdapter(adapter);
    }
    public int getHeightWidhtInDP(int val){
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, getResources().getDisplayMetrics());
        return (int) pixels;
    }
    private List<Integer> numberImage()
    {
        List<Integer> image=new ArrayList<>();
        image.add(R.drawable.screen1);
        image.add(R.drawable.screen2);
        image.add(R.drawable.screen3);

        return image;
    }
}
class MainAdapter extends ArrayAdapter {

    List<Integer> numberImage;
    int itemLayout;
    Context c;

    // constructor is called to initialize the objects
    public MainAdapter(List<Integer> image, int resource, Context context) {
        super(context, resource, image);
        numberImage = image;
        itemLayout = resource;
        c = context;
    }

    // getCount() is called to return
    // the total number of words to be used
    @Override
    public int getCount() {
        return numberImage.size();
    }

    // getView() is called to get position,
    // parent and view of the images.
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }

        Integer image = numberImage.get(position);

        ShapeableImageView imageView = convertView.findViewById(R.id.image1);
        imageView.setImageResource(image);
        return convertView;
    }
}