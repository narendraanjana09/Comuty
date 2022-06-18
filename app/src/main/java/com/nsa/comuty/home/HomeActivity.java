package com.nsa.comuty.home;

import static com.nsa.comuty.extra.Constants.USER_MODEL;
import static com.nsa.comuty.onboarding.extra.Keys.NAME;
import static com.nsa.comuty.onboarding.extra.Keys.PROFILE_IMAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nsa.comuty.BuildConfig;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ActivityHomeBinding;
import com.nsa.comuty.databinding.AddPostEventLayoutBinding;
import com.nsa.comuty.databinding.NavigationHeaderLayoutBinding;
import com.nsa.comuty.databinding.NewChatGroupLayoutBinding;
import com.nsa.comuty.extra.NetworkStatus;
import com.nsa.comuty.home.viewmodel.HomeViewModel;
import com.nsa.comuty.onboarding.extra.ProgressDialog;
import com.nsa.comuty.onboarding.extra.SavedText;
import com.nsa.comuty.onboarding.models.UserModel;
import com.nsa.comuty.onboarding.ui.OnBoardingActivity;




public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private AddPostEventLayoutBinding addPostEventLayoutBinding;
    private BottomSheetBehavior addPostEventSheetBehaviour;
    private NewChatGroupLayoutBinding newChatGroupLayoutBinding;
    private BottomSheetBehavior newChatGroupSheetBehaviour;
    private NavController navController;
    private NavigationHeaderLayoutBinding headerLayoutBinding;
    private HomeViewModel homeViewModel;
    private UserModel userModel;

    private Long backPressedTime=0l;
    @Override
    public void onBackPressed() {

        if(addPostEventSheetBehaviour.getState()==BottomSheetBehavior.STATE_EXPANDED
        || newChatGroupSheetBehaviour.getState()==BottomSheetBehavior.STATE_EXPANDED ){
            newChatGroupSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if(navController.getCurrentDestination()== navController.findDestination(R.id.newPostFragment)){
            super.onBackPressed();
            return;
        }
        if(binding.bottomNavigation.getSelectedItemId()==R.id.exploreFragment) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setContext(this);

       getUserData();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog();
        new NetworkStatus(this,getSupportFragmentManager()).startInternetCheck();
        headerLayoutBinding=NavigationHeaderLayoutBinding.bind(binding.navigationView.getHeaderView(0));

        navController= Navigation.findNavController(this,R.id.navHostFragment);


        setupAddEvent_Post();
        setupNewChatGroup();
        setupViewPager();
        setupchatsMenu();
        Glide.with(this)
                .load(new SavedText(this).getText(PROFILE_IMAGE))
                .into(binding.profileImage1);
        Glide.with(this)
                .load(new SavedText(this).getText(PROFILE_IMAGE))
                .into(headerLayoutBinding.profileImage2);
        headerLayoutBinding.nameTv.setText(new SavedText(this).getText(NAME));
        binding.profileImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerRootLayout.openDrawer(GravityCompat.END);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show(getSupportFragmentManager(),"tag");
                logout();
            }
        });

    }
   private Bundle userBundle;


    private void getUserData() {
        userBundle =new Bundle();
        homeViewModel.getUserModel();
        homeViewModel.getErrorMessage().observe(this,message->{
            showToast("err1 "+message);
        });
        homeViewModel.getUserModelLiveData().observe(this,model->{
            this.userModel=model;
            userBundle.putParcelable(USER_MODEL,userModel);
        });
    }

    private void logout() {
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseAuth.getInstance().signOut();
                progressDialog.dismiss();
                showToast("Logout Success!");
                new SavedText(HomeActivity.this).deleteAll();
                Intent intent=new Intent(HomeActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                showToast("Logout Failed!");
                log(e.getMessage());
            }
        });
    }
    private String TAG="homeactivity";

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
        addPostEventLayoutBinding.newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.motionLayout.transitionToStart();
                addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                navController.navigate(R.id.newPostFragment,userBundle);
            }
        });
        addPostEventLayoutBinding.newEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.motionLayout.transitionToStart();
                addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                navController.navigate(R.id.newEventFragment,userBundle);
            }
        });
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
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        binding.bottomNavigation.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                addPostEventSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                navController.navigate(R.id.newPostFragment,userBundle);
                return false;
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.titleTxt.setText(navDestination.getLabel());
                switch(navDestination.getId()){
                    case R.id.exploreFragment:
                        showBottomNaviagtion();
                        break;
                    case R.id.searchFragment:
                        showBottomNaviagtion();
                        break;
                    case R.id.chatsFragment:
                        showBottomNaviagtion();
                        checkToolbar(R.id.chatsFragment);
                        break;
                    case R.id.moreFragment:
                        showBottomNaviagtion();
                        break;
                    default:
                        hideBottomNaviagtion();
                }
            }
        });

    }



    private void checkToolbar(int itemId) {
        if(itemId==R.id.chatsFragment){
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
        Log.e(TAG, "log: "+message );
    }
}