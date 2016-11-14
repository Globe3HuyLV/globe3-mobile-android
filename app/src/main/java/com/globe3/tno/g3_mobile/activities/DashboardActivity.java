package com.globe3.tno.g3_mobile.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Company;
import com.globe3.tno.g3_mobile.app_objects.factory.AuditFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.CompanyFactory;
import com.globe3.tno.g3_mobile.app_objects.factory.UserFactory;
import com.globe3.tno.g3_mobile.constants.TagTableUsage;
import com.globe3.tno.g3_mobile.fragments.CompanySelectFragment;
import com.globe3.tno.g3_mobile.fragments.SyncDownFragment;
import com.globe3.tno.g3_mobile.util.DateUtility;
import com.globe3.tno.g3_mobile.util.GPSUtility;
import com.globe3.tno.g3_mobile.util.HttpUtility;
import com.globe3.tno.g3_mobile.util.PermissionUtility;

import java.util.ArrayList;
import java.util.Date;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.constants.App.REQUEST_GPS;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANY_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;
import static com.globe3.tno.g3_mobile.globals.Globals.mGPSLocation;
import static com.globe3.tno.g3_mobile.globals.Globals.mGPSUtility;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DashboardActivity dashboardActivity;

    AuditFactory auditFactory;
    UserFactory userFactory;
    CompanyFactory companyFactory;

    Toolbar dashboardToolbar;
    DrawerLayout dashboardDrawer;
    NavigationView dashboardNavigationView;
    FloatingActionMenu menuApps;

    TextView tv_server_connect;
    TextView tv_last_sync;
    TextView tv_gps;
    TextView tv_pending_sync;

    ArrayList<Company> companyList;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dashboard);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (dashboardDrawer.isDrawerOpen(GravityCompat.START)) {
            dashboardDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        dashboardDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS: {
                if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    PermissionUtility.requestLocationServices(dashboardActivity, false);
                }
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_company_select) {
            FragmentManager fragmentManager = getFragmentManager();
            CompanySelectFragment companySelectFragment = new CompanySelectFragment();
            Bundle args = new Bundle();
            args.putSerializable("company_list", companyList);
            companySelectFragment.setArguments(args);
            companySelectFragment.setCancelable(true);
            companySelectFragment.show(fragmentManager, getString(R.string.label_select_entity));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void onActivityLoading(){
        try {
            dashboardActivity = this;

            auditFactory = new AuditFactory(dashboardActivity);
            userFactory = new UserFactory(dashboardActivity);
            companyFactory = new CompanyFactory(dashboardActivity);

            dashboardToolbar = (Toolbar) findViewById(R.id.dashboardToolbar);
            dashboardDrawer = (DrawerLayout) findViewById(R.id.drawer_dashboard);
            dashboardNavigationView = (NavigationView) findViewById(R.id.nav_view);
            menuApps = (FloatingActionMenu) findViewById(R.id.fam_apps);

            tv_server_connect = (TextView) findViewById(R.id.tv_server_connect);
            tv_last_sync = (TextView) findViewById(R.id.tv_last_sync);
            tv_gps = (TextView) findViewById(R.id.tv_gps);
            tv_pending_sync = (TextView) findViewById(R.id.tv_pending_sync);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            setSupportActionBar(dashboardToolbar);
            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dashboardDrawer, dashboardToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            dashboardDrawer.addDrawerListener(toggle);
            toggle.syncState();

            dashboardNavigationView.setNavigationItemSelectedListener(this);

            menuApps.setClosedOnTouchOutside(true);
            Drawable menuAppsIcons[] = new Drawable[2];
            menuAppsIcons[0] = getDrawable(R.drawable.ic_apps_white_24dp);
            menuAppsIcons[1] = getDrawable(R.drawable.ic_add_white_24dp);

            final TransitionDrawable menuAppsCrossfader = new TransitionDrawable(menuAppsIcons);
            menuAppsCrossfader.setCrossFadeEnabled(true);
            menuApps.getMenuIconView().setImageDrawable(menuAppsCrossfader);
            menuApps.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean opened) {
                    if(opened){
                        menuAppsCrossfader.startTransition(150);
                    }else{
                        menuAppsCrossfader.reverseTransition(150);
                    }
                }
            });

            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(dashboardActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
                PermissionUtility.requestLocationServices(dashboardActivity, false);
            }

            loadCompanies();
            showLastSync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityReady(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    new DashboardRefresh().execute();
                    mGPSUtility = new GPSUtility(dashboardActivity);
                    mGPSLocation = mGPSUtility.getGPSLocation();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    public void loadCompanies(){
        if(getSupportActionBar() != null){

            ActionBar mActionBar = getSupportActionBar();
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.actionbar_dashboard, null);
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
            mTitleTextView.setText(COMPANY_NAME);

            mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            mActionBar.setDisplayShowCustomEnabled(true);

            companyList = companyFactory.getUserCompanys(userFactory.getUser(USERLOGINUNIQ).getCompanies());
        }
    }

    public void selectCompany(View view){
        FragmentManager fragmentManager = getFragmentManager();
        CompanySelectFragment companySelectFragment = new CompanySelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("company_list", companyList);
        companySelectFragment.setArguments(args);
        companySelectFragment.setCancelable(true);
        companySelectFragment.show(fragmentManager, getString(R.string.label_select_entity));
    }

    public void resetCompanyName(){
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_text)).setText(COMPANY_NAME);
    }

    public void showLastSync(){
        dashboardActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Date lastSync = auditFactory.getLastSync(TagTableUsage.DATA_SYNC_DOWN);
                if(lastSync==null){
                    tv_last_sync.setText(getString(R.string.msg_never));
                    tv_last_sync.setAllCaps(true);
                    tv_last_sync.setTextColor(ActivityCompat.getColor(dashboardActivity, R.color.colorFailed));
                }else{
                    tv_last_sync.setText(DateUtility.getDateString(lastSync, "HH:mm MMM dd"));
                    tv_last_sync.setAllCaps(false);
                    tv_last_sync.setTextColor(ActivityCompat.getColor(dashboardActivity, R.color.colorBlackLight));
                }
            }
        });
    }

    public void syncDown(View view){
        FragmentManager fragmentManager = getFragmentManager();
        SyncDownFragment syncDownFragment = new SyncDownFragment();
        syncDownFragment.setCancelable(false);
        syncDownFragment.show(fragmentManager, getString(R.string.label_sync_down));
    }

    public void requestGPS(View view){
        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(dashboardActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionUtility.requestLocationServices(dashboardActivity, false);
        }
    }

    private void goToActivity(Class<?> activityClass){
        menuApps.close(true);
        startActivity(new Intent(dashboardActivity, activityClass));
    }

    public void goToTimesheet(View view){
        goToActivity(TimesheetActivity.class);
    }

    public void goToPhotos(View view){
        goToActivity(PhotosActivity.class);
    }

    public void goToRegisterFinger(View view){
        goToActivity(RegsiterFingerActivity.class);
    }

    private class DashboardRefresh extends AsyncTask<Void, Void, Boolean>
    {
        private Boolean serverConnect = false;
        private boolean gpsAllowed = false;
        private Boolean gpsOn = false;

        @Override
        protected Boolean doInBackground(Void... param) {
            try {
                serverConnect = HttpUtility.testConnection();
                gpsAllowed = ActivityCompat.checkSelfPermission( dashboardActivity, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED;
                gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            tv_server_connect.setText(getString(serverConnect?R.string.msg_online:R.string.msg_offline));
            if(gpsAllowed){
                tv_gps.setText(getString(gpsOn?R.string.msg_on:R.string.msg_off));
                tv_gps.setAllCaps(false);
                tv_gps.setTextColor(ActivityCompat.getColor(dashboardActivity, R.color.colorBlackLight));
            }else{
                tv_gps.setText(getString(R.string.msg_denied));
                tv_gps.setAllCaps(true);
                tv_gps.setTextColor(ActivityCompat.getColor(dashboardActivity, R.color.colorFailed));
            }
        }
    }
}
