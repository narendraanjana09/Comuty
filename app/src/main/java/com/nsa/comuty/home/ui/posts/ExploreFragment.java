package com.nsa.comuty.home.ui.posts;

import static com.nsa.comuty.extra.Constants.MESSAGE_DATABASE_NOT_FOUND;
import static com.nsa.comuty.extra.Constants.STATUS_DATABASE_NOT_FOUND;
import static com.nsa.comuty.extra.Constants.USER_MODEL;
import static com.nsa.comuty.onboarding.extra.Keys.COLLEGE;
import static com.nsa.comuty.onboarding.extra.Keys.PROFILE_IMAGE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentExploreBinding;
import com.nsa.comuty.databinding.FragmentPhone1Binding;
import com.nsa.comuty.databinding.PostItemLayoutBinding;
import com.nsa.comuty.extra.Database;
import com.nsa.comuty.extra.Image;
import com.nsa.comuty.extra.Zoom_Image_Dialog;
import com.nsa.comuty.home.ui.posts.adapters.PostImagesAdapter;
import com.nsa.comuty.home.ui.posts.models.PostModel;
import com.nsa.comuty.home.ui.posts.viewmodel.PostsViewModel;
import com.nsa.comuty.home.viewmodel.HomeViewModel;
import com.nsa.comuty.onboarding.extra.ProgressDialog;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.models.ImageModel;
import com.nsa.comuty.onboarding.models.UserModel;
import com.nsa.comuty.trail_for_code.FirebaseDbPagingActivity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExploreFragment extends Fragment {

      private UserModel userModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentExploreBinding binding;
    private Query mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
    private HomeViewModel homeViewModel;
    private PostsViewModel postsViewModel;
    private ProgressDialog progressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postsViewModel = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);
        postsViewModel.setContext(requireContext());
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        progressDialog=new ProgressDialog();
        homeViewModel.getUserModelLiveData().observe(getViewLifecycleOwner(),model->{
            userModel=model;
            if(userModel==null){
                mQuery = new Database().getReferenceColleges().child(new SavedText(getContext()).getText(COLLEGE)).child("posts");
            }else{
                mQuery = new Database().getReferenceColleges().child(userModel.getCollege()).child("posts");
            }
            setData();
            setUpAdapter();
        });





        uploadPostSetup();



    }

    private void uploadPostSetup() {
        postsViewModel.getSuccessMessage().observe(getViewLifecycleOwner(),successMessage->{
            if(successMessage.isEmpty()){
                return;
            }
            showToast(successMessage);
            hideProgressLayout();
        });
        postsViewModel.getTotalsize().observe(getViewLifecycleOwner(),size->{
            Log.e(TAG, "uploadPostSetup: "+size );
            binding.uploadingCard.sizeTv.setText("Total Size "+size+" MB");
        });
        postsViewModel.getErrorMessage().observe(getViewLifecycleOwner(),errorMessage->{
            if(errorMessage.isEmpty()){
                return;
            }
            showToast(errorMessage);
            hideProgressLayout();
        });
        postsViewModel.getLoading().observe(getViewLifecycleOwner(),isLoading->{
            if(isLoading){
                showProgressLayout();
            }else{
               hideProgressLayout();
            }
        });
    }

    private void hideProgressLayout() {
        binding.uploadingCard.getRoot().setVisibility(View.GONE);
    }
    private void showProgressLayout() {
        binding.uploadingCard.getRoot().setVisibility(View.VISIBLE);
    }

    private FirebaseRecyclerPagingAdapter<PostModel, ItemViewHolder> mAdapter;
    private void setUpAdapter() {

        //Initialize Paging Configurations
        PagingConfig config = new PagingConfig(20, 5, false);


        //Initialize Firebase Paging Options
        DatabasePagingOptions<PostModel> options = new DatabasePagingOptions.Builder<PostModel>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(mQuery, config, PostModel.class)
                .build();

        //Initializing Adapter

        mAdapter = new FirebaseRecyclerPagingAdapter<PostModel,ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position, @NonNull PostModel model) {
                viewHolder.bind(model);
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                              int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_item_layout, parent, false);
                return new ItemViewHolder(view);
            }
        };

        mAdapter.addLoadStateListener(states -> {
            LoadState refresh = states.getRefresh();
            LoadState append = states.getAppend();

            if (refresh instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error) refresh;
                binding.progressBar.setVisibility(View.GONE);
                if(loadStateError.getError().getLocalizedMessage().contains(MESSAGE_DATABASE_NOT_FOUND)){
                    showToast("No Db");
                    setNoData();
                    mAdapter.notifyDataSetChanged();
                }else if(loadStateError.getError().getLocalizedMessage().contains(STATUS_DATABASE_NOT_FOUND)){
                    showToast("No Data");
                }else{
                    Log.e(TAG+1, "setUpAdapter: "+loadStateError.getError().getLocalizedMessage() );
                    showToast("error");
                }


            }
            if (append instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error) append;
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG+2, loadStateError.getError().getLocalizedMessage());
            }

            if (append instanceof LoadState.Loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            if (append instanceof LoadState.NotLoading) {
                LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                if (notLoading.getEndOfPaginationReached()) {
                    // This indicates that the user has scrolled
                    // until the end of the data set.
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Reached end of data set");
                    return null;
                }

                if (refresh instanceof LoadState.NotLoading) {
                    // This indicates the most recent load
                    // has finished.
                    binding.progressBar.setVisibility(View.GONE);
                    return null;
                }
            }
            return null;
        });


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.pagingRecycler.setLayoutManager(layoutManager);
        binding.pagingRecycler.setAdapter(mAdapter);

        homeViewModel.getUpdates().observe(getViewLifecycleOwner(),update->{
            mAdapter.refresh();
        });
        homeViewModel.addUpdateListener(mQuery);

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                        if(layoutManager.findFirstVisibleItemPosition()==0){
                            binding.pagingRecycler.smoothScrollToPosition(0);
                        }else if(layoutManager.findFirstVisibleItemPosition()!=-1){
                            if(layoutManager.findLastVisibleItemPosition()<mAdapter.getItemCount()){
                                binding.topBTN.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }.start();



    }

    private void setNoData() {
        binding.mainLayout.setBackgroundColor(getResources().getColor(R.color.background));
        binding.noDataLayout.setVisibility(View.VISIBLE);
        binding.pagingRecycler.setVisibility(View.GONE);
    }
    private void setData(){
        binding.noDataLayout.setVisibility(View.GONE);
        binding.pagingRecycler.setVisibility(View.VISIBLE);
    }

    private String TAG="explore";





    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private PostItemLayoutBinding binding;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
           binding=PostItemLayoutBinding.bind(itemView);
           binding.visibleBtn.setVisibility(View.GONE);

        }

        void bind(@NonNull PostModel model) {
            binding.nameTv.setText(model.getUserName());
            binding.timeTv.setText(getDate(model.getDateTime()));

            new Image().loadCircleImage(getContext(),model.getUserImage(),binding.profileImageview);

            if(model.getPostMessage().isEmpty()){
                binding.descTv.setVisibility(View.GONE);
            }else {
                binding.descTv.setText(model.getPostMessage());
            }
            if(model.getImagesList()!=null){
                binding.imagesRv.setAdapter(new PostImagesAdapter(getContext(), model.getImagesList(),new PostImagesAdapter.OnImageClickListener() {
                    @Override
                    public void OnClick(String link) {
                        Zoom_Image_Dialog zoomImageDialog=new Zoom_Image_Dialog(new ImageModel(link,true));
                        zoomImageDialog.show(getChildFragmentManager(),"zoomImage");
                    }
                }));
            }else{
                binding.descTv.setMaxLines(5);
            }

            if(model.getLikes()==null){
                binding.likeTv.setText("0");

            }else{
                binding.likeTv.setText(model.getLikes().size()+"");
            if(checkLike(model.getLikes())){
                binding.likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart_fill));
                binding.likeTv.setTextColor(getResources().getColor(R.color.red));
            }else{
                binding.likeBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart_unfill));
                binding.likeTv.setTextColor(getResources().getColor(R.color.black));
            }}


           if(model.getComments()==null){
               binding.commentTv.setText("0");
           }




        }
    }
    private String getDate(String millis) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(Long.parseLong(millis));

        SimpleDateFormat dayFormat =new SimpleDateFormat("dd");
        String day = dayFormat.format(date);
        SimpleDateFormat monthFormat =new SimpleDateFormat("MM");
        int month = Integer.parseInt(monthFormat.format(date));
        SimpleDateFormat yearFormat =new SimpleDateFormat("yy");
        String year = yearFormat.format(date);
        int dayIn=Integer.parseInt(day);
        day=dayIn+"";
        if((dayIn>=4 && dayIn<=20) || (dayIn>=24 && dayIn<=30)){
            day=day+"th";
        }else{
            if(day=="1" || day=="21" || day=="31"){
                day=day+"st";
            }
            if(day=="2" || day=="22"){
                day=day+"nd";
            }
            if(day=="3" || day=="23"){
                day=day+"rd";
            }
        }

        return  day+" "+theMonth(month-1)+" "+year+", "+simpleDateFormat.format(date);
    }
    private String theMonth(int month) {
        String[]  monthNames = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        return monthNames[month];
    }

    private boolean checkLike(List<PostModel.Like> likes) {
        if(likes==null){
            return false;
        }
        for(PostModel.Like like:likes){
            if(like.getUserId().equals(userModel.getUid())){
                return true;
            }
        }
        return false;
    }

}