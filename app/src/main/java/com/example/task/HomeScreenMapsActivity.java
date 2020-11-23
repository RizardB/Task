package com.example.task;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.session.SessionManager;
import com.example.task.task.RepoDatabaseTask;
import com.example.task.task.Task;
import com.example.task.task.TaskDao;
import com.example.task.task.TaskFormFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.task.R.layout.*;

public class HomeScreenMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener{

    public GoogleMap mMap;
    public ImageButton startTaskPlusButton, startTaskCheckButton;
    Toolbar toolbar;
    private ArrayList<Task> tasks = new ArrayList<>();
    private TextView taskListUsername;
    private TaskArrayAdapter taskArrayAdapter;
    ExpandableListView expandableListView;
    Button taskDetailsButton, taskLocationButton, logOutButton;

    SessionManager session;
    String sessionUsername, sessionPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home_screen_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.homeScreenMapFragment);
        mapFragment.getMapAsync(this);
        toolbar = findViewById(R.id.toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.homeScreenDrawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startTaskPlusButton = findViewById(R.id.startTaskPlusButton);
        startTaskCheckButton = findViewById(R.id.startTaskCheckButton);
        findViewById(R.id.startTaskCheckButton).setVisibility(View.INVISIBLE);

        View headerView = navigationView.getHeaderView(0);
        taskListUsername = (TextView) headerView.findViewById(R.id.taskListUsername);
        taskListUsername.setText(getUsernameTask());

        taskDetailsButton = findViewById(R.id.taskDetailsButton);
        taskLocationButton = findViewById(R.id.taskLocationButton);

        View header = navigationView.getHeaderView(0);
        logOutButton = (Button) header.findViewById(R.id.user_log_out_button);

        session = new SessionManager(getApplicationContext());
        HashMap user = session.getUserDetails();

        sessionUsername = (String) user.get(SessionManager.key_username);
        sessionPassword = (String) user.get(SessionManager.key_password);

        if (sessionUsername.isEmpty() || sessionPassword.isEmpty()){

            Toast.makeText(getApplicationContext(), "Lütfen Giriş Yapınız.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent intent = new Intent(HomeScreenMapsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        setExpandableListView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homeScreenDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homeScreenDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ankara = new LatLng(39.9035557,32.6226819);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ankara));

        startTaskPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMap();
                startTaskPlusButton.setVisibility(View.INVISIBLE);
                startTaskCheckButton.setVisibility(View.VISIBLE);
                Marker startTaskMarker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                marker.setDraggable(true);
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }
                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng endDragPosition = marker.getPosition();
                        startTaskCheckButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkButtonEvent(marker, endDragPosition);
                            }
                        });
                    }
                });

                startTaskCheckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(marker.isDraggable())
                            checkButtonEvent(marker, marker.getPosition());
                    }
                });
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_view) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setExpandableListView(){
        List<Task> allTasks;
        RepoDatabaseTask database = RepoDatabaseTask.getInstance(this);
        final TaskDao taskDao = database.getTaskDao();
        allTasks = taskDao.getTaskUserId(getUserIdTask());
        tasks.clear();
        tasks.addAll(allTasks);

        expandableListView = (ExpandableListView)findViewById(R.id.listViewAllTasks);
        taskArrayAdapter =  new TaskArrayAdapter(this, tasks, this);
        expandableListView.setAdapter(taskArrayAdapter);
    }

    public void checkButtonEvent(Marker marker, LatLng position){
        marker.setDraggable(false);
        showTaskFormFragment(null, null, position);
    }

    public void showTaskFormFragment(String name, String detail, LatLng location){
        closeTaskFormFragment();
        TaskFormFragment taskFormFragment = new TaskFormFragment();
        ImageButton checkButton = findViewById(R.id.startTaskCheckButton);
        ImageButton addButton = findViewById(R.id.startTaskPlusButton);
        addButton.setVisibility(View.INVISIBLE);
        checkButton.setVisibility(View.INVISIBLE);

        double locationLatitude = location.latitude;
        double locationLLongitude = location.longitude;
        Bundle bundle =  new Bundle();
        bundle.putDouble("Key_Lat", locationLatitude);
        bundle.putDouble("Key_Lng", locationLLongitude);
        taskFormFragment.setArguments(bundle);

        bundle.putString("taskName", name);
        bundle.putString("taskDetails", detail);
        taskFormFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, taskFormFragment).addToBackStack(null).commit();
    }

    public void closeTaskFormFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        TaskFormFragment taskFormFragment = (TaskFormFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if(taskFormFragment !=null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(taskFormFragment).commit();
        }
    }

    public int getUserIdTask(){
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId",0);
        return userId;
    }

    public String getUsernameTask(){
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        return username;
    }

    public void clearMap(){
        mMap.clear();
    }
}