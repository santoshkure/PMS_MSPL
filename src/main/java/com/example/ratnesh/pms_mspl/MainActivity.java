package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userNameTextView, userIdTextView;
    private User user;
    private String userName, userId;
    private View navHeaderView;
    public final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        userNameTextView = (TextView) headerView.findViewById(R.id.user_name);
        userIdTextView = (TextView) headerView.findViewById(R.id.user_id);

        user = SharedPrefManager.getInstance(this).getUser();
        userNameTextView.setText("Welcome, " +  user.getUserLoginId());
        userIdTextView.setText(user.getPartyId());

        displaySelectedScreen(R.id.nav_home, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            if (fragment != null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
                super.onBackPressed();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    finish();
                }
                System.exit(0);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId(), false);
        return true;
    }

    private void displaySelectedScreen(int itemId, boolean first) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeNew();
                break;
            case R.id.nav_contact_us:
                fragment = new ContactUs();
                break;
            case R.id.nav_logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                break;
        }

        if (fragment != null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (currentFragment == null || currentFragment.getClass() != fragment.getClass()) {
                OpenFragment(fragment, first);
            } else {
                CloseDraver();
            }
        } else {
            CloseDraver();
        }
    }

    private void OpenFragment(Fragment fragment, boolean first) {
        if (fragment != null) {
            //replacing the fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (first) {
                ft.add(R.id.content_frame, fragment);
            } else {
                ft.replace(R.id.content_frame, fragment, TAG_FRAGMENT);
            }
            ft.addToBackStack(null);
            ft.commit();
        }
        CloseDraver();
    }

    private void CloseDraver() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}

