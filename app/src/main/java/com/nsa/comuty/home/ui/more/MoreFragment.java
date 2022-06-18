package com.nsa.comuty.home.ui.more;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentMoreBinding;
import com.nsa.comuty.home.ui.more.adapters.MoreAdapter;
import com.nsa.comuty.home.ui.more.interfaces.OnMoreClickListener;
import com.nsa.comuty.home.models.MoreModel;

import java.util.ArrayList;
import java.util.List;



public class MoreFragment extends Fragment {



    private FragmentMoreBinding binding;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    private MoreAdapter adapter;
    private List<MoreModel> list;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        list=new ArrayList<>();
        list.add(new MoreModel("Search User",getContext().getDrawable(R.drawable.search)));
        list.add(new MoreModel("Settings",getContext().getDrawable(R.drawable.settings)));
        list.add(new MoreModel("Feedback",getContext().getDrawable(R.drawable.feedback)));
        adapter=new MoreAdapter(getContext(), list, new OnMoreClickListener() {
            @Override
            public void onClick(int index) {
                switch (index){
                    case 0: break;
                    case 1: break;
                    case 2:
                        navController.navigate(R.id.feedbackFragment);
                        break;
                }
            }
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recyclerView.setAdapter(adapter);

    }
}