package com.xstudioo.noteme;

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

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    CardView cvBtnFemale, cvBtnMale, cvBtnOther;
    TextView tvFemale, tvMale, tvOther;
    String strGender = "Med";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        cvBtnFemale = findViewById(R.id.cv_btn_female);
        cvBtnMale = findViewById(R.id.cv_btn_male);
        cvBtnOther = findViewById(R.id.cv_btn_other);
        tvFemale = findViewById(R.id.tv_female);
        tvMale = findViewById(R.id.tv_male);
        tvOther = findViewById(R.id.tv_other);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        noteDetails = findViewById(R.id.noteDetails);
        noteTitle = findViewById(R.id.noteTitle);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
            if (noteTitle.getText().length() != 0) {
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime, strGender);
                Log.i("TAG", "onOptionsItemSelected: " + strGender);
                SimpleDatabase sDB = new SimpleDatabase(this);
                long id = sDB.addNote(note);
                Note check = sDB.getNote(id);
                Log.d("inserted", "Note: " + id + " -> Title:" + check.getTitle() + " Date: " + check.getDate() + " STATUS : " + check.getStatus());
                onBackPressed();

                Toast.makeText(this, "Note Saved.", Toast.LENGTH_SHORT).show();
            } else {
                noteTitle.setError("Title Can not be Blank.");
            }

        } else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
