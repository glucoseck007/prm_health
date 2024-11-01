package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;

public class AddUpdateActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription;
    private TimePicker timePickerStart, timePickerEnd;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int activityId = -1; // Default value for adding new activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        dbHelper = new DatabaseHelper(this);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        btnSave = findViewById(R.id.btnSave);
    }

    private void bindingAction() {
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1);
        if (activityId != -1) {
            loadActivityData(activityId);
        }

        btnSave.setOnClickListener(view -> {
            if (activityId != -1) {
                updateActivity();
            } else {
                addActivity();
            }
        });
    }

    private void loadActivityData(int id) {
        // Load activity data from database and set to EditTexts and TimePickers
        ActivityModel activity = dbHelper.getActivityById(id);
        editTextName.setText(activity.getName());
        editTextDescription.setText(activity.getDescription());

        // Set time in TimePickers
        String[] startTimeParts = activity.getStartTime().split(":");
        timePickerStart.setHour(Integer.parseInt(startTimeParts[0]));
        timePickerStart.setMinute(Integer.parseInt(startTimeParts[1]));

        String[] endTimeParts = activity.getEndTime().split(":");
        timePickerEnd.setHour(Integer.parseInt(endTimeParts[0]));
        timePickerEnd.setMinute(Integer.parseInt(endTimeParts[1]));
    }

    private void addActivity() {
        // Add a new activity
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String startTime = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String endTime = String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());

        dbHelper.addActivity(1, name, description, startTime, endTime); // Assuming user_id is 1 for demo
        finish();
    }

    private void updateActivity() {
        // Update existing activity
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String startTime = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String endTime = String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());

        dbHelper.updateActivity(activityId, name, description, startTime, endTime);
        finish();
    }
}