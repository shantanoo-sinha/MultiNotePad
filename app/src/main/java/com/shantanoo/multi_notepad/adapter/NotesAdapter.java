package com.shantanoo.multi_notepad.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shantanoo.multi_notepad.MainActivity;
import com.shantanoo.multi_notepad.R;
import com.shantanoo.multi_notepad.model.Note;
import com.shantanoo.multi_notepad.util.Util;

import java.util.List;

/**
 * Created by Shantanoo on 9/23/2020.
 */
public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NotesAdapter";

    private List<Note> notesList;
    private MainActivity mainActivity;

    public NotesAdapter(List<Note> notesList, MainActivity mainActivity) {
        this.notesList = notesList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: creating view holder");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_row, parent, false);
        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding view holder");

        Note note = notesList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNotesTitle;
        private TextView tvNotesText;
        private TextView tvNotesTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotesTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNotesText = itemView.findViewById(R.id.tvNoteText);
            tvNotesTimestamp = itemView.findViewById(R.id.tvNoteTimestamp);
        }

        public void bind(Note note) {
            Log.d(TAG, "bind: Binding note to the ViewHolder");
            String title = note.getTitle();
            if (title != null && title.length() > 80)
                title = title.substring(0, 80) + mainActivity.getString(R.string.continued_string);
            tvNotesTitle.setText(title);

            String text = note.getText();
            if (text != null && text.length() > 80)
                text = text.substring(0, 80) + mainActivity.getString(R.string.continued_string);
            tvNotesText.setText(text);

            tvNotesTimestamp.setText(Util.formatDateAsString(note.getTimestamp()));
        }
    }
}
