package com.example.task;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.WorkerThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.task.task.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TaskArrayAdapter extends BaseExpandableListAdapter{

    private final LayoutInflater inflater;
    private final Context context;
    private final ArrayList<Task> tasks;
    List<Address> address;
    public HomeScreenMapsActivity homeScreenMapsActivity;

    public TaskArrayAdapter(Context context, ArrayList<Task> tasks, HomeScreenMapsActivity homeScreenMapsActivity){
        this.context = context;
        this.tasks = tasks;
        this.homeScreenMapsActivity = homeScreenMapsActivity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getGroup(int listPosition) {

        String locationAddress;
        Geocoder geocoder = new Geocoder(context);
        String [] latlong = tasks.get(listPosition).getTaskLocation().split(",");
        double locationLatitude = Double.parseDouble(latlong[0]);
        double locationLongitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(locationLatitude, locationLongitude);
        try {
            address = getAddress(geocoder, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String taskName= tasks.get(listPosition).getTaskName();
        if(address != null)
            locationAddress = taskName + " - " + address.get(0).getAddressLine(0);
        else
            locationAddress = taskName;

        return locationAddress;
    }

    @Override
    public int getGroupCount() {
        return this.tasks.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_view_item, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.task_list_name);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_child, null);
        }
        Button locationButton = (Button) convertView
                .findViewById(R.id.taskLocationButton);

        Button detailsButton = (Button) convertView
                .findViewById(R.id.taskDetailsButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeScreenMapsActivity.onBackPressed();
                String[] latlong = tasks.get(listPosition).getTaskLocation().split(",");
                double locationLatitude = Double.parseDouble(latlong[0]);
                double locationLongitude = Double.parseDouble(latlong[1]);
                LatLng location = new LatLng(locationLatitude, locationLongitude);
                homeScreenMapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,10));
                Marker markerTaskLocation = homeScreenMapsActivity.mMap.addMarker(new MarkerOptions().position(location));
                markerTaskLocation.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        });

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeScreenMapsActivity.onBackPressed();
                String[] latlong = tasks.get(listPosition).getTaskLocation().split(",");
                String taskName = tasks.get(listPosition).getTaskName();
                String taskDetails = tasks.get(listPosition).getTaskDetails();
                double locationLatitude = Double.parseDouble(latlong[0]);
                double locationLongitude = Double.parseDouble(latlong[1]);
                LatLng location = new LatLng(locationLatitude, locationLongitude);
                homeScreenMapsActivity.showTaskFormFragment(taskName,taskDetails, location);
            }
        });
        convertView.setFocusableInTouchMode(true);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if(tasks!=null)
            return 1;
        else
            return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @WorkerThread
    List<Address> getAddress(Geocoder geocoder, LatLng location) throws IOException {
        address = geocoder.getFromLocation(location.latitude, location.longitude,1);
        return address;
    }

}
