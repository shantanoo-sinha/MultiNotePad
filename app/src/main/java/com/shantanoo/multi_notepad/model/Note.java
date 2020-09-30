package com.shantanoo.multi_notepad.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shantanoo on 9/23/2020.
 */
public class Note implements Comparable<Note>, Serializable {

    private String title;
    private String text;
    private Date timestamp;

    public Note(String title, String text, Date timestamp) {
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(Note o) {
        if (o.getTimestamp() == null)
            return 0;
        return o.getTimestamp().compareTo(this.getTimestamp());
    }
}
