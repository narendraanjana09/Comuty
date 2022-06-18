package com.nsa.comuty.trail_for_code;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter_LifecycleAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityDatabasePagingBinding;
import com.nsa.comuty.extra.Database;

import java.util.Locale;


import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;


public class FirebaseDbPagingActivity extends AppCompatActivity {

        private static final String TAG = "PagingActivity";
    private static final String STATUS_DATABASE_NOT_FOUND = "DATA_NOT_FOUND";
    private static final String MESSAGE_DATABASE_NOT_FOUND = "Data not found at given child path!";
    private static final String DETAILS_DATABASE_NOT_FOUND = "No data was returned for the given query: ";

        private ActivityDatabasePagingBinding mBinding;

        private FirebaseDatabase mDatabase;
        private Query mQuery;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBinding = ActivityDatabasePagingBinding.inflate(getLayoutInflater());
            setContentView(mBinding.getRoot());

            mDatabase = FirebaseDatabase.getInstance();
            mQuery = mDatabase.getReference().child("items");

            setUpAdapter();

            mBinding.addBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createItems();
                }
            });
            mBinding.topBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBinding.pagingRecycler.smoothScrollToPosition(0);
                    view.setVisibility(View.GONE);
                }
            });



        }
        private FirebaseRecyclerPagingAdapter<Item, ItemViewHolder> mAdapter;
    private void setUpAdapter() {

        //Initialize Paging Configurations
        PagingConfig config = new PagingConfig(30, 5, false);


        //Initialize Firebase Paging Options
        DatabasePagingOptions<Item> options = new DatabasePagingOptions.Builder<Item>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Item.class)
                .build();

        //Initializing Adapter

      mAdapter = new FirebaseRecyclerPagingAdapter<Item, ItemViewHolder>(options) {
                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_item, parent, false);
                        return new ItemViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder,
                                                    int position,
                                                    @NonNull Item model) {
                        holder.bind(model);
                    }

                };

        mAdapter.addLoadStateListener(states -> {
            LoadState refresh = states.getRefresh();
            LoadState append = states.getAppend();

            if (refresh instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error) refresh;
                mBinding.swipeRefreshLayout.setRefreshing(false);
                if(loadStateError.getError().getLocalizedMessage().contains(MESSAGE_DATABASE_NOT_FOUND)){
                   showToast("No Db");
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
                mBinding.swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG+2, loadStateError.getError().getLocalizedMessage());
            }

            if (append instanceof LoadState.Loading) {
                mBinding.swipeRefreshLayout.setRefreshing(true);
            }

            if (append instanceof LoadState.NotLoading) {
                LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                if (notLoading.getEndOfPaginationReached()) {
                    // This indicates that the user has scrolled
                    // until the end of the data set.
                    mBinding.swipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, "Reached end of data set");
                    return null;
                }

                if (refresh instanceof LoadState.NotLoading) {
                    // This indicates the most recent load
                    // has finished.
                    mBinding.swipeRefreshLayout.setRefreshing(false);
                    return null;
                }
            }
            return null;
        });


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mBinding.pagingRecycler.setLayoutManager(layoutManager);
        mBinding.pagingRecycler.setAdapter(mAdapter);

        // Reload data on swipe
        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            //Reload Data
            mAdapter.refresh();
        });


            mQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mAdapter.refresh();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.toString() );
                }
            });

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
                             mBinding.pagingRecycler.smoothScrollToPosition(0);
                         }else if(layoutManager.findFirstVisibleItemPosition()!=-1){
                         if(layoutManager.findLastVisibleItemPosition()<mAdapter.getItemCount()){
                             mBinding.topBTN.setVisibility(View.VISIBLE);
                         }
                         }
                     }
                 });
             }
         }.start();



    }


    //122
    //123

        private void createItems() {
            DatabaseReference referense =new Database().getDatabase().child("count");
            referense.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        long n= (long) snapshot.getValue();
                        long val=n-1;
                        long item=Integer.MAX_VALUE-val;
                        mDatabase.getReference("items").child(val+"").setValue(new Item("item"+item,(int)item));
                        referense.setValue(val);
                    }else{
                        showToast("no value");
                        referense.setValue(Integer.MAX_VALUE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void showToast(@NonNull String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


        public static class Item {

            @Nullable
            public String text;
            public int value;

            public Item(){}

            public Item(@Nullable String text, int value) {
                this.text = text;
                this.value = value;
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;
            TextView mValueView;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.item_text);
                mValueView = itemView.findViewById(R.id.item_value);
            }

            void bind(@NonNull Item item) {
                mTextView.setText(item.text);
                mValueView.setText(String.valueOf(item.value));
            }
        }

    }