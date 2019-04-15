package fiveguys.com.wishcraftapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

public class Feed extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //initializes bottom navigation menu and its fragments/frames
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationMenu);

        final HomeFragment homeFragment = new HomeFragment();
        final ItemSearchFragment itemSearchFragment = new ItemSearchFragment();
        final NotificationsFragment notificationsFragment = new NotificationsFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.home) {
                    setFragment(homeFragment);
                    return true;
                } else if(id == R.id.search) {
                    setFragment(itemSearchFragment);
                    return true;
                } else if(id == R.id.notifications) {
                    setFragment(notificationsFragment);
                    return true;
                } else if(id == R.id.profile) {
                    setFragment(profileFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}