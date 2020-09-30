package com.shantanoo.multi_notepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shantanoo.multi_notepad.activity.About;
import com.shantanoo.multi_notepad.activity.AddNote;
import com.shantanoo.multi_notepad.adapter.NotesAdapter;
import com.shantanoo.multi_notepad.model.Note;
import com.shantanoo.multi_notepad.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private static final int NEW_NOTE = 1;
    private static final int EXISTING_NOTE = 2;

    private List<Note> notes;
    private boolean notesUpdated = false;

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes = new ArrayList<>();

        recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setHasFixedSize(true);
        notesAdapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(notesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Load Notes from JSON file
        loadNotes();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Saving instance state");
        outState.putBoolean(getString(R.string.notes_updated), notesUpdated);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: Restoring instance state");
        super.onRestoreInstanceState(savedInstanceState);
        notesUpdated = savedInstanceState.getBoolean(getString(R.string.notes_updated));
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: Saving Notes to JSON file");
        saveNotes();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Selected menu item: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.mnuAbout:
                // Open About activity
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.mnuAddNote:
                // Open Add Note activity
                addNoteActivity(0, false, null);
                return true;
            default:
                Log.d(TAG, "Unknown menu item: " + item.getItemId());
                Toast.makeText(this, "Unknown item", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (notes == null)
            return;

        final int notePosition = recyclerView.getChildLayoutPosition(v);
        addNoteActivity(notePosition, true, notes.get(notePosition));
    }

    @Override
    public boolean onLongClick(View v) {
        if (notes == null)
            return false;

        final int notePosition = recyclerView.getChildLayoutPosition(v);
        Note note = notes.get(notePosition);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // No button
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
            }
        });

        // Yes button
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notes.remove(notePosition);
                notesUpdated = true;
                updateNotesCount();
                notesAdapter.notifyDataSetChanged();
            }
        });

        // Setting message
        String noteTitle = note == null ? "" : note.getTitle();
        builder.setMessage(getString(R.string.delete_note) + noteTitle + getString(R.string.question));

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void loadNotes() {
        Log.d(TAG, "loadNotes: Loading Notes from JSON file");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.notes_json_file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, getString(R.string.utf_8)));

            String line;
            StringBuilder builder = new StringBuilder();

            while ((line = reader.readLine()) != null)
                builder.append(line);

            Log.d(TAG, "loadNotes: Loading notes from JSON data");
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.notes_list));

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject noteJson = jsonArray.getJSONObject(i);

                    if (noteJson == null)
                        continue;

                    Note note = new Note(noteJson.getString(getString(R.string.title)),
                            noteJson.getString(getString(R.string.text)),
                            Util.formatStringAsDate(noteJson.getString(getString(R.string.timestamp))));
                    notes.add(note);
                }
            }

            // Sorting notes on the note timestamp
            if (notes.size() > 0)
                Collections.sort(notes);

            // Update Notes Count in App Title
            updateNotesCount();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "loadNotes: Failed to load JSON file", e);
        }
    }

    private void updateNotesCount() {
        if (notes != null && notes.size() > 0)
            setTitle(getString(R.string.app_name) + " (" + notes.size() + ")");
        else
            setTitle(getString(R.string.app_name));
    }

    private void addNoteActivity(int notePosition, boolean existingNote, Note note) {
        int requestCode = NEW_NOTE;
        Intent intent = new Intent(this, AddNote.class);
        intent.putExtra(getString(R.string.existing_note), existingNote);
        if (existingNote) {
            requestCode = EXISTING_NOTE;
            if (note != null) {
                intent.putExtra(getString(R.string.note_position), notePosition);
                intent.putExtra(getString(R.string.note), (Serializable) note);
            }
        }
        startActivityForResult(intent, requestCode);
    }

    private void saveNotes() {
        if (!notesUpdated)
            return;

        Log.d(TAG, "saveNotes: Saving notes to JSON file");
        FileOutputStream fos;
        JsonWriter writer = null;
        try {
            fos = getApplicationContext().openFileOutput(getString(R.string.notes_json_file), Context.MODE_PRIVATE);
            writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.utf_8)));

            writer.setIndent("  ");
            writer.beginObject();
            writer.name(getString(R.string.notes_list));
            writer.beginArray();

            for (Note note : notes) {
                if (note == null)
                    continue;

                writer.beginObject();
                writer.name(getString(R.string.title)).value(note.getTitle());
                writer.name(getString(R.string.text)).value(note.getText());
                writer.name(getString(R.string.timestamp)).value(Util.formatDateAsString(note.getTimestamp()));
                writer.endObject();
            }

            writer.endArray();
            writer.endObject();
            notesUpdated = false;
        } catch (IOException e) {
            Log.e(TAG, "saveNotes: Failed to save notes to Json file", e);
            //e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "saveNotes: Failed to close resources", e);
                /*e.printStackTrace();*/
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTE) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra(getString(R.string.new_note));
                if (note == null)
                    return;

                notes.add(note);
                notesUpdated = true;
                Log.d(TAG, "onActivityResult: new note added: " + note.toString());

                // Sorting notes
                Collections.sort(notes);

                // Update Notes Count in App Title
                updateNotesCount();

                // Notify the adapter about DataSet change
                notesAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }
        } else if (requestCode == EXISTING_NOTE) {
            if (resultCode == RESULT_OK) {
                boolean isNoteChanged = data.getBooleanExtra(getString(R.string.note_updated), false);
                if (!isNoteChanged)
                    return;

                Note existingNote = (Note) data.getSerializableExtra(getString(R.string.note));
                if (existingNote == null)
                    return;

                int notePosition = data.getIntExtra(getString(R.string.note_position), 0);
                notes.set(notePosition, existingNote);
                notesUpdated = true;
                Log.d(TAG, "onActivityResult: existing note edited: " + existingNote.toString());

                // Sorting notes
                Collections.sort(notes);

                // Update Notes Count in App Title
                updateNotesCount();

                // Notify the adapter about DataSet change
                notesAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "onActivityResult: Existing Note Edited: " + resultCode);
            }
        }
    }
}