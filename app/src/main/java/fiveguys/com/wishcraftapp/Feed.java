package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.support.v7.widget.Toolbar;

public class Feed extends AppCompatActivity {

    final HomeFragment homeFragment = new HomeFragment();
    final ItemSearchFragment itemSearchFragment = new ItemSearchFragment();
    final ProfileFragment profileFragment = new ProfileFragment();
    final FriendsFragment friendsFragment = new FriendsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("WishCraft");
        setFragment(homeFragment);

        //initializes bottom navigation menu and its fragments/frames
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationMenu);
        navigationView.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_toolbar_menu, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()) {
                case R.id.home:
                    setFragment(homeFragment);
                    setTitle("WishCraft");
                    return true;
                case R.id.search:
                    setFragment(itemSearchFragment);
                    setTitle("Item Search");
                    return true;
                case R.id.profile:
                    setFragment(profileFragment);
                    //setTitle("Your Profile");
                    return true;
                case R.id.friends:
                    setFragment(friendsFragment);
                    setTitle("Friends");
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public void settingsButton(MenuItem item) {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }
}