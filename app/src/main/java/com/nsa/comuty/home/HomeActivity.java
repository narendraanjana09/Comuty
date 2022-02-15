package com.nsa.comuty.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityHomeBinding;
import com.nsa.comuty.home.adapters.ViewPagerAdapter;
import com.nsa.comuty.home.ui.ChatsFragment;
import com.nsa.comuty.home.ui.EventFragment;
import com.nsa.comuty.home.ui.ExploreFragment;
import com.nsa.comuty.home.ui.FriendsFragment;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());








        setupViewPager();
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
                    case R.id.nav_friends:

                        binding.viewPager.setCurrentItem(3,true);
                        break;
                    default:showToast("add button");
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
        adapter.addFragment(new FriendsFragment());
        binding.viewPager.setAdapter(adapter);
    }

    private void checkToolbar() {
        switch (binding.bottomNavigation.getSelectedItemId()){
            case R.id.nav_explore: binding.titleTxt.setText("Explore");
                break;
            case R.id.nav_event:binding.titleTxt.setText("Events");
                break;
            case R.id.nav_chat:binding.titleTxt.setText("Chats");
                break;
            case R.id.nav_friends:binding.titleTxt.setText("Friends");
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}