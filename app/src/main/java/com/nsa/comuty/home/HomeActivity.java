package com.nsa.comuty.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityHomeBinding;
import com.nsa.comuty.databinding.AddPostEventLayoutBinding;
import com.nsa.comuty.databinding.NewChatGroupLayoutBinding;
import com.nsa.comuty.home.adapters.ViewPagerAdapter;
import com.nsa.comuty.home.ui.ChatsFragment;
import com.nsa.comuty.home.ui.EventFragment;
import com.nsa.comuty.home.ui.ExploreFragment;
import com.nsa.comuty.home.ui.MoreFragment;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private AddPostEventLayoutBinding addPostEventLayoutBinding;
    private BottomSheetBehavior addPostEventSheetBehaviour;
    private NewChatGroupLayoutBinding newChatGroupLayoutBinding;
    private BottomSheetBehavior newChatGroupSheetBehaviour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAddEvent_Post();
        setupNewChatGroup();
        setupViewPager();
        setupchatsMenu();
        binding.profileImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerRootLayout.openDrawer(GravityCompat.END);
            }
        });

    }

    private void setupNewChatGroup() {

        View view= binding.coordinator1.findViewById(R.id.new_chat_group_layout);
        newChatGroupSheetBehaviour = BottomSheetBehavior.from(view);
        newChatGroupLayoutBinding=NewChatGroupLayoutBinding.bind(view);
        newChatGroupLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newChatGroupSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        binding.addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newChatGroupSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setupAddEvent_Post() {
        View view= binding.coordinator1.findViewById(R.id.add_post_event_layout);
        addPostEventSheetBehaviour = BottomSheetBehavior.from(view);
        addPostEventLayoutBinding=AddPostEventLayoutBinding.bind(view);
        addPostEventLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void setupchatsMenu() {
        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.chats_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        showToast("You Clicked : " + item.getTitle());
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    private void setupViewPager() {
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_explore:

                        binding.viewPager.setCurrentItem(0,true);
                        break;
                    case R.id.nav_event:

                        binding.viewPager.setCurrentItem(1,true);
                        break;
                    case R.id.nav_chat:
                        binding.viewPager.setCurrentItem(2,true);
                        break;
                    case R.id.nav_more:

                        binding.viewPager.setCurrentItem(3,true);
                        break;
                    default:addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return false;
                }
                checkToolbar();
                return true;
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>1){
                    binding.bottomNavigation.getMenu().getItem(position+1).setChecked(true);

                }else {
                    binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                }
                checkToolbar();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ExploreFragment());
        adapter.addFragment(new EventFragment());
        adapter.addFragment(new ChatsFragment());
        adapter.addFragment(new MoreFragment());
        binding.viewPager.setAdapter(adapter);
    }

    private void checkToolbar() {
        switch (binding.bottomNavigation.getSelectedItemId()){
            case R.id.nav_explore: binding.titleTxt.setText("Explore");
                break;
            case R.id.nav_event:binding.titleTxt.setText("Events");
                break;
            case R.id.nav_more:binding.titleTxt.setText("More");
                break;
            case R.id.nav_chat:binding.titleTxt.setText("Chats");
                break;

        }
        if(binding.bottomNavigation.getSelectedItemId()==R.id.nav_chat){
            binding.profileImage1.setVisibility(View.GONE);
            binding.chatsLayout.setVisibility(View.VISIBLE);
            binding.addChatButton.setVisibility(View.VISIBLE);
        }else{
            binding.profileImage1.setVisibility(View.VISIBLE);
            binding.chatsLayout.setVisibility(View.GONE);
            binding.addChatButton.setVisibility(View.GONE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}