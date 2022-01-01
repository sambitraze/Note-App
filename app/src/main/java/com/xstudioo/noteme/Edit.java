package com.xstudioo.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText nTitle, nContent;
    Calendar c;
    String todaysDate;
    String currentTime;
    long nId;
    CardView cvBtnFemale, cvBtnMale, cvBtnOther;
    TextView tvFemale, tvMale, tvOther;
    String strGender = "Med";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        cvBtnFemale = findViewById(R.id.cv_btn_female);
        cvBtnMale = findViewById(R.id.cv_btn_male);
        cvBtnOther = findViewById(R.id.cv_btn_other);
        tvFemale = findViewById(R.id.tv_female);
        tvMale = findViewById(R.id.tv_male);
        tvOther = findViewById(R.id.tv_other);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        nId = i.getLongExtra("ID", 0);
        SimpleDatabase db = new SimpleDatabase(this);
        Note note = db.getNote(nId);

        final String title = note.getTitle();
        String content = note.getContent();
        nTitle = findViewById(R.id.noteTitle);
        nContent = findViewById(R.id.noteDetails);
        nTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        nTitle.setText(title);
        nContent.setText(content);

        // set current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + todaysDate);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: " + currentTime);
    }

    private String pad(int time) {
        if (time < 10)
            return "0" + time;
        return String.valueOf(time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Note note = new Note(nId, nTitle.getText().toString(), nContent.getText().toString(), todaysDate, currentTime, strGender);
            Log.d("EDITED", "edited: before saving id -> " + note.getId());
            SimpleDatabase sDB = new SimpleDatabase(getApplicationContext());
            long id = sDB.editNote(note);
            Log.d("EDITED", "EDIT: id " + id);
            goToMain();
            Toast.makeText(this, "Note Edited.", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void initLow(View view) {
        initFemale(null);
    }

    public void initMed(View view) {
        initMale(null);
    }

    public void initHigh(View view) {
        initOther(null);
    }

    public void initFemale(View view) {
        cvBtnFemale.setCardBackgroundColor(getResources().getColor(R.color.green));
        cvBtnMale.setCardBackgroundColor(getResources().getColor(R.color.white));
        cvBtnOther.setCardBackgroundColor(getResources().getColor(R.color.white));

        cvBtnFemale.setCardElevation(25);
        cvBtnMale.setCardElevation(0);
        cvBtnOther.setCardElevation(0);

        strGender = "Low";
    }

    public void initMale(View view) {
        cvBtnFemale.setCardBackgroundColor(getResources().getColor(R.color.white));
        cvBtnMale.setCardBackgroundColor(getResources().getColor(R.color.green));
        cvBtnOther.setCardBackgroundColor(getResources().getColor(R.color.white));

        cvBtnMale.setCardElevation(25);
        cvBtnFemale.setCardElevation(0);
        cvBtnOther.setCardElevation(0);

        strGender = "Med";
    }

    public void initOther(View view) {
        cvBtnOther.setCardBackgroundColor(getResources().getColor(R.color.green));
        cvBtnMale.setCardBackgroundColor(getResources().getColor(R.color.white));
        cvBtnFemale.setCardBackgroundColor(getResources().getColor(R.color.white));

        cvBtnOther.setCardElevation(25);
        cvBtnMale.setCardElevation(0);
        cvBtnFemale.setCardElevation(0);


        strGender = "High";

    }


}