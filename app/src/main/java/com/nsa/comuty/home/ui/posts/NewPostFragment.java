package com.nsa.comuty.home.ui.posts;

import static com.nsa.comuty.extra.Constants.USER_MODEL;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentNewPostBinding;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.home.ui.posts.adapters.NewPostImagesAdapter;
import com.nsa.comuty.home.ui.posts.adapters.PostStackImagesAdapter;
import com.nsa.comuty.home.ui.posts.models.PostModel;
import com.nsa.comuty.home.ui.posts.viewmodel.PostsViewModel;
import com.nsa.comuty.home.viewmodel.HomeViewModel;
import com.nsa.comuty.onboarding.extra.ProgressDialog;
import com.nsa.comuty.onboarding.models.UserModel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private UserModel userModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            userModel=getArguments().getParcelable(USER_MODEL);
        }
        postsViewModel = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);
        postsViewModel.setContext(getContext());
    }

    private FragmentNewPostBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentNewPostBinding
                .inflate(inflater,container,false);
        return binding.getRoot();
    }

    private NewPostImagesAdapter adapter;
    private List<Uri> imagesList;
    private PostsViewModel postsViewModel;
    private ProgressDialog progressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        progressDialog=new ProgressDialog();
        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        binding.descEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.preview.descTv.setText(editable.toString());
                binding.textCounterTv.setText(editable.toString().length()+"/500");
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
        binding.preview.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLiked){
                    isLiked=false;
                    binding.preview.likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart_unfill));
                    binding.preview.likeTv.setTextColor(getResources().getColor(R.color.black));
                }else{
                    isLiked=true;
                    binding.preview.likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart_fill));
                    binding.preview.likeTv.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
       setImagesAdapter();
       binding.shareBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String desc=binding.descEd.getText().toString().trim();
               if(desc.isEmpty() && imagesList.size()==0){
                   showToast("please add some content first");
               }else{
                   post(desc,imagesList);
               }
           }
       });
    }

    private void post(String desc, List<Uri> imagesList) {
        PostModel postModel=new PostModel(0+"",userModel.getName(),userModel.getUid(),userModel.getProfileUrl()
        ,System.currentTimeMillis()+"",new ArrayList<PostModel.Like>(),new ArrayList<PostModel.Comment>()
        ,desc,new ArrayList<String>());


        postsViewModel.getSuccessMessage().observe(getViewLifecycleOwner(),successMessage->{
            showToast(successMessage);
            if(progressDialog.isVisible()){
                progressDialog.dismiss();
            }
            binding.backBTN.callOnClick();

        });
        postsViewModel.getErrorMessage().observe(getViewLifecycleOwner(),errorMessage->{
            showToast(errorMessage);
        });
        postsViewModel.getLoading().observe(getViewLifecycleOwner(),isLoading->{
            if(isLoading){
                binding.backBTN.callOnClick();
            }else{
            }
        });
        postsViewModel.uploadPost(postModel,imagesList,userModel);

    }

    private String TAG="newpostfrag";
    boolean isLiked=false;

    private void setImagesAdapter() {
        imagesList=new ArrayList<>();

        adapter=new NewPostImagesAdapter(getContext());
        adapter.setImagesList(imagesList);
        binding.imagesRV.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.imagesRV.setAdapter(adapter);

        stackImagesAdapter=new PostStackImagesAdapter(getContext());
        stackImagesAdapter.setImagesList(imagesList);
        binding.preview.imagesRv.setAdapter(stackImagesAdapter);

        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagesList.size()==3){
                    showToast("Maximum 3 images!");
                }else{
                    addImages();
                }
            }
        });
        adapter.setListener(new NewPostImagesAdapter.OnClickListener() {
            @Override
            public void onClick(int pos) {

            }

            @Override
            public void onDelete(int pos) {
                imagesList.remove(pos);
                addImagesToPreview();
                if(imagesList.size()==0){
                    binding.txt2.setVisibility(View.GONE);
                }
                stackImagesAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.imagesRV);

    }

    private PostStackImagesAdapter stackImagesAdapter;
    private void addImagesToPreview() {
                if(imagesList.size()!=0){
                    binding.preview.imagesRv.setVisibility(View.VISIBLE);
                }else{
                    binding.preview.imagesRv.setVisibility(View.GONE);
                }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAbsoluteAdapterPosition();
            int toPosition = target.getAbsoluteAdapterPosition();
            Log.e("TAG", "onMove: "+fromPosition+","+toPosition );
            Collections.swap(imagesList, fromPosition, toPosition);
            if((fromPosition==0 && toPosition==2) || (fromPosition==2 && toPosition==0)){
                Collections.swap(imagesList, 1, 2);
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            stackImagesAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private Uri getAddImage() {
        return getUriToDrawable(getContext(),R.drawable.ic_baseline_add_photo_alternate_24);
    }

    private void addImages() {
        ImagePicker.with(getActivity())
                .crop()
                .compress(1024)
                .galleryOnly()
                .maxResultSize(1080, 1080)
                .createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent intent) {
                        getImage.launch(intent);
                        return null;
                    }
                });
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Uri uri = result.getData().getData();

                       imagesList.add(uri);
                       binding.txt2.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        stackImagesAdapter.notifyDataSetChanged();
                        addImagesToPreview();

                        // Handle the Intent
                    }
                }
            });





    public int getHeightWidhtInDP(int val){
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, getResources().getDisplayMetrics());
        return (int) pixels;
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message+"", Toast.LENGTH_SHORT).show();
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}