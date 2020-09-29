package com.shantanoo.multi_notepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.widget.Toast;

import com.shantanoo.multi_notepad.R;
import com.shantanoo.multi_notepad.model.Note;

import java.io.Serializable;
import java.util.Date;

public class AddNote extends AppCompatActivity {

    private static final String TAG = "AddNote";

    private EditText etNoteTitle;
    private EditText etNoteText;

    private int notePosition;
    private boolean existingNote;
    private String previousNoteTitle;
    private String previousNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteText = findViewById(R.id.etNoteText);
        etNoteText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.existing_note))) {
            existingNote = intent.getBooleanExtra(getString(R.string.existing_note), false);
            if (!existingNote)
                return;

            Note existingNote = (Note) intent.getSerializableExtra(getString(R.string.note));
            notePosition = intent.getIntExtra(getString(R.string.note_position), 0);
            if (existingNote == null)
                return;

            previousNoteTitle = existingNote.getTitle();
            previousNoteText = existingNote.getText();
            etNoteTitle.setText(previousNoteTitle);
            etNoteText.setText(previousNoteText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: Saving note");
        if (item.getItemId() == R.id.mnuSaveNote) {
            Log.d(TAG, "Selected menu item" + item.getItemId());
            saveNote(false);
            return true;
        }

        Log.d(TAG, "Unknown menu item" + item.getItemId());
        Toast.makeText(this, "Unknown item", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveNote(true);
    }

    private void saveNote(boolean fromBackButton) {
        final String currentTitle = etNoteTitle.getText().toString().trim();
        final String currentText = etNoteText.getText().toString().trim();

        if (existingNote) { // Existing note
            if( currentTitle.length() != 0 ){ // If title entered
                if( !currentTitle.equals(previousNoteTitle) || !currentText.equals(previousNoteText)) {
                    if(fromBackButton){
                        // save note from back button

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        // Setting Message
                        String message = getString(R.string.note_not_saved) +
                                getString(R.string.new_line) +
                                getString(R.string.save_this_note) + currentTitle + getString(R.string.question);
                        builder.setMessage(message);

                        // No button
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // exit without saving
                                exitWithoutSaving(false);
                            }
                        });

                        // Yes button
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // save note and add details to intent
                                Intent intent = new Intent();
                                Note note = new Note(currentTitle, currentText, new Date());
                                intent.putExtra(getString(R.string.note), (Serializable)note);
                                intent.putExtra(getString(R.string.note_position), notePosition);
                                intent.putExtra(getString(R.string.note_updated), true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        // save request from save button
                        Intent intent = new Intent();
                        Note note = new Note(currentTitle, currentText, new Date());
                        intent.putExtra(getString(R.string.note), (Serializable)note);
                        intent.putExtra(getString(R.string.note_position), notePosition);
                        intent.putExtra(getString(R.string.note_updated), true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else{
                    // Title and desc are same so exit with saving no toast
                    exitWithoutSaving(false);
                }
            } else{
                // No title entered exit with TOAST
                exitWithoutSaving(true);
            }
        } else { // New Note
            if (currentTitle.length() != 0) {
                if(fromBackButton){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // Mo button
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            exitWithoutSaving(false);
                        }
                    });

                    // Yes button
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            Note note = new Note(currentTitle, currentText, new Date());
                            intent.putExtra(getString(R.string.new_note), (Serializable) note);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                    // Setting message
                    String message = getString(R.string.note_not_saved) +
                            getString(R.string.new_line) +
                            getString(R.string.save_this_note) + currentTitle + getString(R.string.question);
                    builder.setMessage(message);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Intent intent = new Intent();
                    Log.d(TAG, "saveNote: intent created");
                    Note note = new Note(currentTitle, currentText, new Date());
                    Log.d(TAG, "saveNote: note object created");
                    intent.putExtra(getString(R.string.new_note), (Serializable) note);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else { // Title is empty exit with showing toast
                exitWithoutSaving(true);
            }
        }
    }

    // Function will navigate back to main without saving
    public void exitWithoutSaving(boolean showToast){
        if(showToast)
            Toast.makeText(this, R.string.title_empty_message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.note_updated), false);
        setResult(RESULT_OK, intent);
        finish();
    }
}