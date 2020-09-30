package com.shantanoo.multi_notepad.activity;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        Log.d(TAG, "onCreate:");
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
        Log.d(TAG, "onOptionsItemSelected: Selected menu item =>" + item.getItemId());
        if (item.getItemId() == R.id.mnuSaveNote) {
            Log.d(TAG, "onOptionsItemSelected: Saving note");
            saveNote(false);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected: Unknown menu item =>" + item.getItemId());
        Toast.makeText(this, R.string.unknown_item, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveNote(true);
    }

    private void saveNote(boolean fromBackButton) {
        Log.d(TAG, "saveNote: Saving note");

        final String currentTitle = etNoteTitle.getText().toString().trim();
        final String currentText = etNoteText.getText().toString().trim();

        if (existingNote) {
            Log.d(TAG, "saveNote: Existing note");
            // Existing note
            if (currentTitle.length() == 0) {
                // No title entered exit with TOAST
                Log.d(TAG, "saveNote: No title entered, exit with TOAST");
                exitWithoutSaving(true);
            } else {
                // If title is entered
                if (currentTitle.equals(previousNoteTitle) && currentText.equals(previousNoteText)) {
                    // Title and desc are same so exit with saving no toast
                    Log.d(TAG, "saveNote: Title and desc are same so exit with saving no toast");
                    exitWithoutSaving(false);
                } else {
                    saveNote(fromBackButton, currentTitle, currentText, false);
                }
            }
        } else {
            // New Note
            Log.d(TAG, "saveNote: New note");
            if (currentTitle.length() == 0) {
                // No title entered exit with TOAST
                Log.d(TAG, "saveNote: No title entered, exit with TOAST");
                exitWithoutSaving(true);
            } else {
                saveNote(fromBackButton, currentTitle, currentText, true);
            }
        }
    }

    private void saveNote(boolean fromBackButton, final String currentTitle, final String currentText, final boolean isNewNote) {
        if (fromBackButton) {
            // back button pressed. Show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Setting Message
            String message = getDialogMessage(currentTitle);
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
                    saveNote(currentTitle, currentText, isNewNote);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // save request from save button
            saveNote(currentTitle, currentText, isNewNote);
        }
    }

    private String getDialogMessage(String currentTitle) {
        return getString(R.string.note_not_saved) +
                getString(R.string.new_line) +
                getString(R.string.save_this_note) + currentTitle + getString(R.string.question);
    }

    // Save new note or existing note
    private void saveNote(String currentTitle, String currentText, boolean isNewNote) {
        Log.d(TAG, "saveNote: Saving note");
        Note note = new Note(currentTitle, currentText, new Date());
        Intent intent = new Intent();
        if(isNewNote) {
            intent.putExtra(getString(R.string.new_note), (Serializable) note);
        } else {
            intent.putExtra(getString(R.string.note), (Serializable) note);
            intent.putExtra(getString(R.string.note_position), notePosition);
            intent.putExtra(getString(R.string.note_updated), true);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    // This will navigate back to main activity without saving the note
    public void exitWithoutSaving(boolean showToast) {
        Log.d(TAG, "exitWithoutSaving:");
        if (showToast)
            Toast.makeText(this, R.string.title_empty_message, Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.putExtra(getString(R.string.note_updated), false);
        setResult(RESULT_OK, intent);
        finish();
    }
}