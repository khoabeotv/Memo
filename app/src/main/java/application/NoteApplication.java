package application;

import android.app.Application;

import databases.NoteDatabase;

/**
 * Created by TrKaJv on 19-Apr-17.
 */

public class NoteApplication extends Application {
    private static NoteApplication instance;
    private NoteDatabase noteDatabase;
    @Override
    public void onCreate() {
        noteDatabase = new NoteDatabase(this);
        super.onCreate();
        instance = this;
    }

    public static NoteApplication getInstance() {
        return instance;
    }

    public NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }
}
