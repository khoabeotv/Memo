package databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import model.Note;

/**
 * Created by TrKaJv on 19-Apr-17.
 */

public class NoteDatabase extends SQLiteAssetHelper {


    private static final String DATABASE_NAME = "note.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String ICON = "icon";
    private static final String COLOR = "color";
    private static final String CONTENT = "content";
    private static final String LEVEL = "level";

    private static final String[] ALL_COLUMN = {ID, TITLE, ICON, COLOR, CONTENT, LEVEL};

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Note> loadAllParentNote() {
        List<Note> nodeList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

//        Cursor cursor = db.rawQuery("SELECT * FROM note WHERE level = 0", null);
        Cursor cursor = db.query("note", ALL_COLUMN, LEVEL + " = ?", new String[]{"0"}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            int level = cursor.getInt(cursor.getColumnIndex("level"));

            Note note = new Note(id, title, icon, color, content);
            nodeList.add(note);
        }
        return nodeList;
    }
}
