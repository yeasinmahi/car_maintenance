package com.gits.sami.carmaintenance.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gits.sami.carmaintenance.R;
import com.gits.sami.carmaintenance.db.PopulatedOpenHelper;
import com.gits.sami.carmaintenance.fragments.DatePickerFragment;
import com.gits.sami.carmaintenance.fragments.EntryFragment;
import com.gits.sami.carmaintenance.fragments.ReportsFragment;
import com.gits.sami.carmaintenance.others.Utility;
import com.gits.sami.carmaintenance.others.Utility.dateEnum;
import com.gits.sami.carmaintenance.others.ViewPagerAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init();
        PopulatedOpenHelper.getInstance(getApplicationContext());
    }

    private void Init() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EntryFragment(), "Entry");
        adapter.addFragment(new ReportsFragment(), "Reports");
        viewPager.setAdapter(adapter);
    }
    dateEnum bill;
    public void datePicker(View view){
        switch (view.getId()){
            case R.id.dateButton: bill = dateEnum.EntryDate;
                break;
            case R.id.fromDateButton: bill = dateEnum.ReportFromDate;
                break;
            case R.id.toDateButton: bill = dateEnum.ReportToDate;
                break;
        }
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    private void setDate(final Calendar calender){
        switch (bill){
            case EntryDate: ((EditText) findViewById(R.id.dateEditText)).setText(Utility.getDateAsString(calender.getTime(), Utility.myDateFormat.dd_MMM_yyyy));
                break;
            case ReportFromDate: ((EditText) findViewById(R.id.fromDateEditText)).setText(Utility.getDateAsString(calender.getTime(),Utility.myDateFormat.dd_MMM_yyyy));
                break;
            case ReportToDate: ((EditText) findViewById(R.id.toDateEditText)).setText(Utility.getDateAsString(calender.getTime(),Utility.myDateFormat.dd_MMM_yyyy));
                break;
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        setDate(calendar);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_electricity) {
            moveToFragment(new EntryFragment());
        } else if (id == R.id.nav_wasa) {
            moveToFragment(new WasaFragment());
        } else if (id == R.id.nav_gas) {

        } else if (id == R.id.nav_reports) {

        } else*/ if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        }
        else if(id==R.id.nav_exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.viewpager, fragment, "createPost").addToBackStack(null).commit();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
