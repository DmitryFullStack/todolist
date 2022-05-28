package com.arhiser.todolist.screens.details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    private Note note;

    private EditText editText;

    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);
        }
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getString(R.string.note_details_title));

        editText = findViewById(R.id.text);
        Button redButton = findViewById(R.id.red);
        Button greenButton = findViewById(R.id.green);
        FloatingActionButton storeButton = findViewById(R.id.store);

        redButton.setOnClickListener(btn -> editText.setTextColor(Color.RED));
        greenButton.setOnClickListener(btn -> editText.setTextColor(Color.GREEN));
        storeButton.setOnClickListener(btn -> saveNote());

        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
            if(note.color != null){
                editText.setTextColor(note.color);
            }
            editText.setText(note.text);
        } else {
            note = new Note();
        }

        showSoftKeyboard(editText);
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                saveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        if (editText.getText().length() > 0) {
            note.text = editText.getText().toString();
            note.done = false;
            note.color = editText.getCurrentTextColor();
            note.timestamp = System.currentTimeMillis();
            if (getIntent().hasExtra(EXTRA_NOTE)) {
                App.getInstance().getNoteDao().update(note);
            } else {
                App.getInstance().getNoteDao().insert(note);
            }
            finish();
        }
    }
}
