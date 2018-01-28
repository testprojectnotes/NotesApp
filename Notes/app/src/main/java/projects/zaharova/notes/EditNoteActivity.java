package projects.zaharova.notes;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import projects.zaharova.notes.db.NotesContract;

public class EditNoteActivity extends BaseNoteActivity{

    public static final String EXTRA_NOTE_ID = "note_id";

    private TextView noteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTv = findViewById(R.id.text_tv);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            initNoteLoader();
        } else {
            finish();
        }

        getLoaderManager().initLoader(0, null, this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.view_note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_edit:
                editNote();
                return true;

            case R.id.action_delete:
                deleteNote();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContentUris.withAppendedId(NotesContract.Notes.URI, noteId), NotesContract.Notes.SINGLE_PROJECTION, null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);

        displayNote(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void editNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra(CreateNoteActivity.EXTRA_NOTE_ID, noteId);

        startActivity(intent);
    }

    private void deleteNote() {
        getContentResolver().delete(NotesContract.Notes.URI, null, new String[]{Long.toString(noteId)});
        finish();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    protected void displayNote(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            finish();
            return;
        }
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE));
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));

        setTitle(title);
        noteTv.setText(noteText);
    }
}
