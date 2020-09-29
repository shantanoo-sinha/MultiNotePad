# Multi-Note Pad
MultiNotePad Android Application

## App Features
* This app allows the creation and maintenance of multiple notes. Any number of notes are allowed (including no notes at all). Notes are made up of a title, a note text, and a last-update time.
* Notes are saved to (and loaded from) the internal file system in JSON format.
* The application is made up of 3 activities. These are described below:

### Main Activity:
* Notes are displayed in a list, in time order (latest-update-first, oldest-update-last). The note list is implemented using the RecyclerView.
* The Main Activity allows the user to create a new note via an Add options-menu item. Pressing this button will open the Edit Activity (described next) with empty Title and Note Text areas.
* The main activity allows the user to edit an existing note by tapping on an existing note in the displayed list of notes. Doing so will open the Edit Activity, displaying the note’s Title and Note Text – both available for editing.
* The main activity also has an Info options-menu item that will open the About Activity when pressed. The About Activity indicates the application’s name, the date & author, and the version number.
* Notes can be deleted from the Main Activity by long-pressing on a note entry. Upon doing so, a confirmation dialog will be opened where the user can confirm (or cancel) the delete operation.
* Note list-entries contain the note title, the last-save-time, and the first 80 characters of the note text. Notes titles or note text with more than 80 characters displays “…” after the first 80 characters.
* The current number of notes is displayed in the title-bar.
* Icons for the Add and Info menu items are taken from Google’s Material Design icon set (https://material.io/icons/)

### Edit Activity
* The Edit Activity contains editable fields for the note title and note text. The last-save time is not displayed here and is never user-editable – it is automatically generated and saved when the note is saved.
* The note title is a single-line text field where the user can enter or edit a title for the current note (no size limit).
* The note text is a multi-line text area with no size limit. This has scrolling capability for when notes exceed the size of the activity.
* The Edit Activity allows the note title & text to be saved by either:
    * Pressing the Save options-menu item. This will return the new note to the MainActivity, and then exit the Edit Activity. MainActivity will then add the note to the MainActivity’s list of notes. Note that if no changes have been made to the current note, the Edit Activity simply exits.
    * Pressing the Back arrow to exit the activity. This will first display a confirmation dialog where the user can opt to save the note (if changes have been made) before exiting the activity. If saved, the new note is returned to the MainActivity, and then exit the Edit Activity. MainActivity will then add the note to the MainActivity’s list of notes. Note that if no changes have been made to the current note, the Edit Activity simply exits.
* Note this “Save” in steps “a” and “b” does not imply saving to file – it implies updating an existing note object in the Main Activity’s note list or adding a new note to the Main Activity’s note list.
* A note without a title is not allowed to be saved (even if there is note text). If such an attempt is made simply exit the activity without saving and show a Toast message indicating that the un-titled activity was not saved.
* Icons for the Save menu item are taken from Google’s Material Design icon set (https://material.io/icons/)

### About Activity
* The About Activity contains the application title, a copyright date and the developer's name, and the version number.
* There is no functionality present on this activity. The only action a user can take is to press the Back arrow to exit the activity.