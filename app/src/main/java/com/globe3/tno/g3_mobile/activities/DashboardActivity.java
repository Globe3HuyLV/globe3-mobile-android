package com.globe3.tno.g3_mobile.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.globe3.tno.g3_mobile.BuildConfig;
import com.globe3.tno.g3_mobile.R;
import com.globe3.tno.g3_mobile.app_objects.Company;
import com.globe3.tno.g3_mobile.app_objects.GPSLocation;
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
import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.client.NBiometricClient;

import java.util.ArrayList;
import java.util.Date;

import static com.globe3.tno.g3_mobile.constants.App.GLOBE3_DB;
import static com.globe3.tno.g3_mobile.constants.App.REQUEST_GPS;
import static com.globe3.tno.g3_mobile.globals.Globals.BIOMETRIC_DATA;
import static com.globe3.tno.g3_mobile.globals.Globals.COMPANY_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_ID;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_MODEL;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICE_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.MAC;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINID;
import static com.globe3.tno.g3_mobile.globals.Globals.USERLOGINUNIQ;
import static com.globe3.tno.g3_mobile.globals.Globals.GPS_LOCATION;
import static com.globe3.tno.g3_mobile.globals.Globals.GPS_UTILITY;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DashboardActivity dashboard_activity;

    AuditFactory audit_factory;
    UserFactory user_factory;
    CompanyFactory company_factory;

    ActionBar action_bar;
    ActionBarDrawerToggle toggle;

    Drawable menu_apps_icons[];

    Toolbar dashboard_toolbar;
    DrawerLayout dashboard_drawer;
    NavigationView dashboard_nav_view;
    LinearLayout ll_logo;
    View navigation_drawer_header;
    NavigationView navigation_drawer;
    FloatingActionMenu menu_apps;

    TextView tv_server_connect;
    TextView tv_last_sync;
    TextView tv_gps;
    TextView tv_pending_sync;

    RelativeLayout rl_blocker;

    FloatingActionButton fab_timesheet;
    FloatingActionButton fab_register_finger;
    FloatingActionButton fab_location_check;
    FloatingActionButton fab_photos;
    FloatingActionButton fab_task_update;

    NavigationView navigation_drawer_logout;

    ArrayList<Company> company_list;

    LocationManager location_manager;

    int logo_touch_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dashboard);
        super.onCreate(savedInstanceState);

        dashboard_activity = this;
    }

    @Override
    public void onBackPressed() {
        if (dashboard_drawer.isDrawerOpen(GravityCompat.START)) {
            dashboard_drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        dashboard_drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS: {
                if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    PermissionUtility.requestLocationServices(dashboard_activity, false);
                }else{
                    GPS_UTILITY = new GPSUtility(dashboard_activity);
                    GPS_LOCATION = GPS_UTILITY.getGPSLocation();
                }
            }
        }
    }

    public void onActivityLoading(){
        audit_factory = new AuditFactory(dashboard_activity);
        user_factory = new UserFactory(dashboard_activity);
        company_factory = new CompanyFactory(dashboard_activity);

        dashboard_toolbar = (Toolbar) findViewById(R.id.dashboardToolbar);
        dashboard_drawer = (DrawerLayout) findViewById(R.id.drawer_dashboard);
        dashboard_nav_view = (NavigationView) findViewById(R.id.nav_view);
        navigation_drawer = (NavigationView) findViewById(R.id.navigation_drawer);
        navigation_drawer_header = navigation_drawer.getHeaderView(0);
        ll_logo = (LinearLayout) navigation_drawer_header.findViewById(R.id.ll_logo);
        menu_apps = (FloatingActionMenu) findViewById(R.id.fam_apps);

        tv_server_connect = (TextView) findViewById(R.id.tv_server_connect);
        tv_last_sync = (TextView) findViewById(R.id.tv_last_sync);
        tv_gps = (TextView) findViewById(R.id.tv_gps);
        tv_pending_sync = (TextView) findViewById(R.id.tv_pending_sync);

        rl_blocker = (RelativeLayout) findViewById(R.id.rl_blocker);

        fab_timesheet = (FloatingActionButton) findViewById(R.id.fab_timesheet);
        fab_register_finger = (FloatingActionButton) findViewById(R.id.fab_register_finger);
        fab_location_check = (FloatingActionButton) findViewById(R.id.fab_location_check);
        fab_photos = (FloatingActionButton) findViewById(R.id.fab_photos);
        fab_task_update = (FloatingActionButton) findViewById(R.id.fab_task_update);

        navigation_drawer_logout = (NavigationView) findViewById(R.id.navigation_drawer_logout);

        DEVICE_ID = Settings.Secure.getString(dashboard_activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        DEVICE_MODEL = android.os.Build.MODEL;
        DEVICE_NAME = BluetoothAdapter.getDefaultAdapter().getName();

        location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        GPS_LOCATION = new GPSLocation();

        if(BIOMETRIC_DATA==null){
            BIOMETRIC_DATA = new NBiometricClient();
            BIOMETRIC_DATA.setMatchingThreshold(48);
            BIOMETRIC_DATA.setFingersMatchingSpeed(NMatchingSpeed.LOW);
            BIOMETRIC_DATA.setDatabaseConnectionToSQLite(GLOBE3_DB);
        }

        action_bar = getSupportActionBar();
    }

    public void onActivityReady(){
        setSupportActionBar(dashboard_toolbar);
        if(action_bar != null){
            action_bar.setDisplayShowTitleEnabled(false);
        }

        ll_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppVersion();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dashboard_drawer, dashboard_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dashboard_drawer.addDrawerListener(toggle);
        toggle.syncState();

        dashboard_nav_view.setNavigationItemSelectedListener(this);

        menu_apps.setClosedOnTouchOutside(true);
        Drawable menuAppsIcons[] = new Drawable[2];
        menuAppsIcons[0] = getDrawable(R.drawable.ic_apps_white_24dp);
        menuAppsIcons[1] = getDrawable(R.drawable.ic_add_white_24dp);

        final TransitionDrawable menuAppsCrossfader = new TransitionDrawable(menuAppsIcons);
        menuAppsCrossfader.setCrossFadeEnabled(true);
        menu_apps.getMenuIconView().setImageDrawable(menuAppsCrossfader);
        menu_apps.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened){
                    rl_blocker.setVisibility(View.VISIBLE);
                    rl_blocker.setAnimation(AnimationUtils.loadAnimation(base_activity, R.anim.animate_fade_in));
                    menuAppsCrossfader.startTransition(150);
                }else{
                    rl_blocker.setAnimation(AnimationUtils.loadAnimation(base_activity, R.anim.animate_fade_out));
                    menuAppsCrossfader.reverseTransition(150);
                    rl_blocker.setVisibility(View.GONE);
                }
            }
        });

        if(!USERLOGINID.equals("m8")){
            fab_timesheet.setVisibility(MAC.substring(0,1).equals("n")?View.GONE:View.VISIBLE);
            if(MAC.substring(1,2).equals("n")){
                navigation_drawer.getMenu().clear();
            }else{
                navigation_drawer.getMenu().clear();
                navigation_drawer.inflateMenu(R.menu.activity_dashboard_drawer);
            }
            fab_register_finger.setVisibility(MAC.substring(2,3).equals("n")?View.GONE:View.VISIBLE);
            fab_photos.setVisibility(MAC.substring(3,4).equals("n")?View.GONE:View.VISIBLE);
            fab_location_check.setVisibility(MAC.substring(4,5).equals("n")?View.GONE:View.VISIBLE);
            fab_task_update.setVisibility(MAC.substring(5,6).equals("n")?View.GONE:View.VISIBLE);
        }

        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(dashboard_activity, Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionUtility.requestLocationServices(dashboard_activity, false);
        }else{
            GPS_UTILITY = new GPSUtility(dashboard_activity);
            GPS_LOCATION = GPS_UTILITY.getGPSLocation();
        }

        navigation_drawer_logout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                dashboard_activity.finish();
                return true;
            }
        });

        loadCompanies();
        showLastSync();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    new DashboardRefresh().execute();
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
            LayoutInflater mInflater = LayoutInflater.from(dashboard_activity);

            View mCustomView = mInflater.inflate(R.layout.actionbar_dashboard, null);
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
            mTitleTextView.setText(COMPANY_NAME);

            mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            mActionBar.setDisplayShowCustomEnabled(true);

            if(user_factory.getActiveUsers().size() > 0){
                company_list = company_factory.getUserCompanys(user_factory.getUser(USERLOGINUNIQ).getCompanies());
            }else{
                company_list = new ArrayList<Company>();
            }
        }
    }

    public void selectCompany(View view){
        FragmentManager fragmentManager = getFragmentManager();
        CompanySelectFragment companySelectFragment = new CompanySelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("company_list", company_list);
        companySelectFragment.setArguments(args);
        companySelectFragment.setCancelable(true);
        companySelectFragment.show(fragmentManager, getString(R.string.label_select_entity));
    }

    public void resetCompanyName(){
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_text)).setText(COMPANY_NAME);
    }

    public void showLastSync(){
        Date lastSync = audit_factory.getLastSync(TagTableUsage.DATA_SYNC_DOWN);
        if(lastSync==null){
            tv_last_sync.setText(getString(R.string.msg_never));
            tv_last_sync.setAllCaps(true);
            tv_last_sync.setTextColor(ActivityCompat.getColor(dashboard_activity, R.color.colorFailed));
        }else{
            tv_last_sync.setText(DateUtility.getDateString(lastSync, "HH:mm MMM dd"));
            tv_last_sync.setAllCaps(false);
            tv_last_sync.setTextColor(ActivityCompat.getColor(dashboard_activity, R.color.colorBlackLight));
        }
    }

    public void syncDown(View view){
        FragmentManager fragmentManager = getFragmentManager();
        SyncDownFragment syncDownFragment = new SyncDownFragment();
        syncDownFragment.setCancelable(false);
        syncDownFragment.show(fragmentManager, getString(R.string.label_sync_down));
    }

    public void requestGPS(View view){
        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(dashboard_activity, Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionUtility.requestLocationServices(dashboard_activity, false);
        }else{
            GPS_UTILITY = new GPSUtility(dashboard_activity);
            GPS_LOCATION = GPS_UTILITY.getGPSLocation();
        }
    }

    private void goToActivity(Class<?> activityClass){
        menu_apps.close(false);
        startActivity(new Intent(dashboard_activity, activityClass));
    }

    public void closeMenuApps(View view){
        menu_apps.close(true);
    }

    public void showAppVersion(){
        logo_touch_counter++;
        if(logo_touch_counter==3){
            Toast.makeText(dashboard_activity, dashboard_activity.getString(R.string.msg_version_date_1s, DateUtility.getDateString(new Date(BuildConfig.TIMESTAMP), "yyyy-MM-dd")), Toast.LENGTH_SHORT).show();
            logo_touch_counter = 0;
        }
    }

    public void goToTimesheet(View view){
        goToActivity(TimesheetActivity.class);
    }

    public void goToPhotos(View view){
        goToActivity(PhotosActivity.class);
    }

    public void goToRegisterFinger(View view){
        goToActivity(RegisterFingerActivity.class);
    }

    public void goToLocationCheck(View view){
        goToActivity(LocationCheckActivity.class);
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
                gpsAllowed = ActivityCompat.checkSelfPermission(dashboard_activity, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED;
                gpsOn = location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

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
                tv_gps.setTextColor(ActivityCompat.getColor(dashboard_activity, R.color.colorBlackLight));
            }else{
                tv_gps.setText(getString(R.string.msg_denied));
                tv_gps.setAllCaps(true);
                tv_gps.setTextColor(ActivityCompat.getColor(dashboard_activity, R.color.colorFailed));
            }
        }
    }
}
