package com.example.task.task;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task.HomeScreenMapsActivity;
import com.example.task.R;

import static android.widget.Toast.LENGTH_SHORT;

public class TaskFormFragment extends Fragment {

    public TaskFormFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_task_form, container, false);
        Button taskFormCancelButton = rootView.findViewById(R.id.formTaskCancelButton);
        Button taskFormSaveButton = rootView.findViewById(R.id.formTaskOkButton);
        EditText editTextTaskName = rootView.findViewById(R.id.editTextTaskName);
        EditText editTextTaskDetails = rootView.findViewById(R.id.editTextTaskDetails);

        Bundle extras =  getArguments();
        String taskName = extras.getString("taskName");
        String taskDetails = extras.getString("taskDetails");
        editTextTaskName.setText(taskName);
        editTextTaskDetails.setText(taskDetails);
        if(taskName != null || taskDetails != null){
            editTextTaskName.setFocusable(false);
            editTextTaskDetails.setFocusable(false);
            taskFormSaveButton.setVisibility(View.INVISIBLE);
        }

        taskFormCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHomeScreen();
            }
        });

        taskFormSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =  getArguments();
                double locationLat = bundle.getDouble("Key_Lat");
                double locationLng = bundle.getDouble("Key_Lng");

                String taskLocationString = locationLat + "," + locationLng;
                String taskName = editTextTaskName.getText().toString();
                String taskDetails = editTextTaskDetails.getText().toString();
                clearHomeScreen();
                int userIdTask = ((HomeScreenMapsActivity) getActivity()).getUserIdTask();
                taskSaveDatabase(userIdTask, taskName, taskDetails, taskLocationString);
                ((HomeScreenMapsActivity) getActivity()).setExpandableListView();
                Toast.makeText(getContext(), "Kayıt Başarılı!", LENGTH_SHORT).show();
                ((HomeScreenMapsActivity) getActivity()).setExpandableListView();
            }
        });
        return rootView;
    }

    public void clearHomeScreen(){
        ((HomeScreenMapsActivity) getActivity()).closeTaskFormFragment();
        ((HomeScreenMapsActivity) getActivity()).startTaskCheckButton.setVisibility(View.INVISIBLE);
        ((HomeScreenMapsActivity) getActivity()).startTaskPlusButton.setVisibility(View.VISIBLE);
        ((HomeScreenMapsActivity) getActivity()).mMap.clear();
    }

    public void taskSaveDatabase(int userId, String taskName, String taskDetails, String taskLocation){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RepoDatabaseTask database = RepoDatabaseTask.getInstance(getContext());
                final TaskDao taskDao = database.getTaskDao();
                taskDao.insertTask(new Task(userId, taskName, taskDetails, taskLocation));
            }
        }).start();
    }
}