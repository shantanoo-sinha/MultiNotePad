# MultiNotePad
MultiNotePad Android Application

## App Features
* This app allows the creation and maintenance of multiple notes. Any number of notes are allowed (including no notes at all). Notes are made up of a title, a note text, and a last-update time.
* There is no need to use a different layout for landscape orientation in this application. One layout should work fine in either orientation.
* Notes should be saved to (and loaded from) the internal file system in JSON format. If no file is found upon loading, the application should start with no existing notes and no errors. (A new JSON file would then be created when new notes are saved).
* JSON file loading must happen in the onCreate method. Saving should happen in the onPause method.
* A simple java Note class (with title, note text, and last save date) should be created to represent each individual note in the application.
* The application is made up of 3 activities. These are described below:

### Main Activity:
* Notes should be displayed in a list, in time order (latest-update-first, oldest-update-last). The note list must be implemented using the RecyclerView.
* The Main Activity will allow the user to create a new note via an Add options-menu item . Pressing this button will open the Edit Activity (described next) with empty Title and Note Text areas.
* The main activity will allow the user to edit an existing note by tapping on an existing note in the displayed list of notes. Doing so will open the Edit Activity, displaying the note’s Title and Note Text – both available for editing.
* The main activity will also have an Info options-menu item that will open the About Activity (described later) when pressed. The About Activity indicates the application’s name, the date & author, and the version number.
* Notes can be deleted from the Main Activity by long-pressing on a note entry. Upon doing so, a confirmation dialog will be opened where the user can confirm (or cancel) the delete operation.
* Note list-entries contain the note title (bold), the last-save-time, and the first 80 characters of the note text (see image). Notes titles or note text with more than 80 characters should display “…” after the first 80 characters are displayed.
* The current number of notes is displayed in the title-bar.
* The activity background should be set to a color other than white (preferable a darker color). The list entries should be a lighter version of that same color.
* Icons for the Add and Info menu items should be selected from Google’s Material Design icon set (https://material.io/icons/)

### Edit Activity
* The Edit Activity contains editable fields for the note title and note text. The last-save time is NOT displayed here and is never user-editable – it should be automatically generated and saved when the note is saved.
* The note title is a single-line text field (EditText) where the user can enter or edit a title for the current note (no size limit).
* The note text is a multi-line text area (EditText) with no size limit. This should have scrolling capability for when notes exceed the size of the activity.
* The Edit Activity allows the note title & text to be saved by either:
    * Pressing the Save options-menu item . This will return the new note to the MainActivity, and then exit the Edit Activity. MainActivity will then add the note to the MainActivity’s list of notes. Note that if no changes have been made to the current note, the Edit Activity simply exits.
    * Pressing the Back arrow to exit the activity. This will first display a confirmation dialog where the user can opt to save the note (if changes have been made) before exiting the activity. If saved, the new note is returned to the MainActivity, and then exit the Edit Activity. MainActivity will then add the note to the MainActivity’s list of notes. Note that if no changes have been made to the current note, the Edit Activity simply exits.
* Note this “Save” in steps “a” and “b” does not imply saving to file – it implies updating an existing note object in the Main Activity’s note list or adding a new note to the Main Activity’s note list.
* A note without a title is not allowed to be saved (even if there is note text). If such an attempt is made simply exit the activity without saving and show a Toast message indicating that the un-titled activity was not saved.
* The activity background should be set to a color other than white (the same color used in the Main Activity). The title and text fields should be a lighter version of that same color (the same color used in the Main Activity).
* Icons for the Save menu item should be selected from Google’s Material Design icon set (https://material.io/icons/)

### About Activity
* The About Activity should contain a full-screen image background (use any non-copywritten image you desire).
* Over the background image, key information on the application should be clearly displayed (clearly readable). This information should include the application title, a copyright date and your name, and the version number (1.0).
* There is no functionality present on this activity. The only action a user can take is to press the Back arrow to exit the activity.