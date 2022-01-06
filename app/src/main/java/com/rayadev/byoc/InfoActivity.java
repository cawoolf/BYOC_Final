package com.rayadev.byoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView mTextViewVersion, mTextViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setUpToolbar();
        linkViews();
        setText();
    }

    private void setUpToolbar() {

        //Links the toolbar over the app Actionbar
        Toolbar toolbar = findViewById(R.id.info_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Info");
    }

    private void linkViews() {

        mTextViewVersion = findViewById(R.id.info_version_number_TextView);
        mTextViewEmail = findViewById(R.id.info_email_TextView);

    }

    private void setText() {
        String versionName = BuildConfig.VERSION_NAME;
        mTextViewVersion.setText("Current Version: " + versionName);
        mTextViewEmail.setText(R.string.info_email);
    }
}