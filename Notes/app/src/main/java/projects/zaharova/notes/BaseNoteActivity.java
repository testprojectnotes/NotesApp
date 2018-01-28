package projects.zaharova.notes;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import projects.zaharova.notes.db.NotesContract;

public abstract class BaseNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int LOADER_NOTE = 0;

    protected long noteId = -1;

    protected void initNoteLoader() {
        getLoaderManager().initLoader(LOADER_NOTE, null, this
        );
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
    protected abstract void displayNote(Cursor cursor);

}
