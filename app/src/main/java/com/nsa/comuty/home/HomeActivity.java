package com.nsa.comuty.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
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

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private AddPostEventLayoutBinding addPostEventLayoutBinding;
    private BottomSheetBehavior addPostEventSheetBehaviour;
    private NewChatGroupLayoutBinding newChatGroupLayoutBinding;
    private BottomSheetBehavior newChatGroupSheetBehaviour;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navController= Navigation.findNavController(this,R.id.navHostFragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                switch(navDestination.getId()){
                    case R.id.exploreFragment:
                        showBottomNaviagtion();
                        break;
                    case R.id.eventFragment:
                        showBottomNaviagtion();
                        break;
                    case R.id.chatsFragment:
                        showBottomNaviagtion();
                        break;
                    case R.id.moreFragment:

                        showBottomNaviagtion();
                        break;
                    default:
                        hideBottomNaviagtion();
                }
            }
        });

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

    private void hideBottomNaviagtion() {
        binding.motionLayout.transitionToState(R.id.end);
    }

    private void showBottomNaviagtion() {
        binding.motionLayout.transitionToStart();
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
        binding.bottomNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log("menu clicked");
            }
        });
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.nav_explore:
                        binding.titleTxt.setText("Explore");
                        navController.navigate(R.id.exploreFragment);
                        break;
                    case R.id.nav_event:
                        binding.titleTxt.setText("Events");
                        navController.navigate(R.id.eventFragment);
                        break;
                    case R.id.nav_chat:
                        binding.titleTxt.setText("Chats");
                        navController.navigate(R.id.chatsFragment);
                        break;
                    case R.id.nav_more:
                        binding.titleTxt.setText("More");
                        navController.navigate(R.id.moreFragment);
                        break;
                    default:addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return false;
                }
                checkToolbar(item.getItemId());
                return true;
            }
        });    }

    private void checkToolbar(int itemId) {

        if(itemId==R.id.nav_chat){
            binding.profileImage1.setVisibility(View.GONE);
            binding.chatsLayout.setVisibility(View.VISIBLE);
            binding.motionLayout.transitionToState(R.id.end_chats);
        }else{
            binding.motionLayout.transitionToStart();
            binding.profileImage1.setVisibility(View.VISIBLE);
            binding.chatsLayout.setVisibility(View.GONE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
    private void log(String message) {
        Log.e("HomeLog", "log: "+message );
    }
}