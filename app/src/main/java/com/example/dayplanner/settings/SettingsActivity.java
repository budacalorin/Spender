package com.example.dayplanner.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dayplanner.R;

public class SettingsActivity extends AppCompatActivity {
    private Button colorButtonPrimary;
    private Button colorButtonDarkGradient;
    private Button colorButtonDark;
    private Button colorButtonAccent;
    private Button colorButtonText;
    private Button colorButtonSelected;
    private Button colorButtonForButtons;
    private Button colorButtonListEntry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        colorButtonAccent = (Button) findViewById(R.id.color_button_accent);
        colorButtonDark = (Button) findViewById(R.id.color_button_dark);
        colorButtonDarkGradient = (Button) findViewById(R.id.color_button_dark_gradient);
        colorButtonForButtons = (Button) findViewById(R.id.color_button_for_buttons);
        colorButtonListEntry = (Button) findViewById(R.id.color_button_list_entry);
        colorButtonPrimary = (Button) findViewById(R.id.color_button_primary);
        colorButtonSelected = (Button) findViewById(R.id.color_button_selected);
        colorButtonText = (Button) findViewById(R.id.color_button_text);


    }

    private class SpecialOnClickListener
            implements View.OnClickListener {
        int color;

        SpecialOnClickListener(int resource){
            color = resource;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
