package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String DATE = "date";
    private static final String ID_PARENT = "id_parent";

    private static final String[] ALL_COLUMN = {ID, TITLE, ICON, COLOR, CONTENT, ID_PARENT};

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Note> loadAllNotes() {
        List<Note> nodeList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

//        Cursor cursor = db.rawQuery("SELECT * FROM note WHERE level = 0", null);
        Cursor cursor = db.query("note", ALL_COLUMN, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            String icon = cursor.getString(cursor.getColumnIndex(ICON));
            String color = cursor.getString(cursor.getColumnIndex(COLOR));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            int id_parent = cursor.getInt(cursor.getColumnIndex(ID_PARENT));

            Note note = new Note(id, title, icon, color, content, date, id_parent);
            nodeList.add(note);
        }

        cursor.close();
        db.close();

        return nodeList;
    }


    public void moveNote(Note note, int newID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_parent", newID + "");
        db.update("note", contentValues, "id = ?", new String[]{note.getId() + ""});
        db.close();
    }

    public void copyNote(Note note, int newIDParent) {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        ContentValues contentValues = new ContentValues();
//        contentValues.put("id", note.getId());
        contentValues.put("title", note.getTitle());
        contentValues.put("icon", note.getIcon());
        contentValues.put("color", note.getColor());
        contentValues.put("content", note.getContent());
        contentValues.put("date", format.format(new Date()));   // lấy thởi điểm hiện tại
        contentValues.put("id_parent",newIDParent);

        db.insert("note", null, contentValues);
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        long r = db.delete("note", "ID = ?", new String[]{note.getId()+""});
        db.close();
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        ContentValues contentValues = new ContentValues();
//        contentValues.put("id", note.getId());
        contentValues.put("title", note.getTitle());
        contentValues.put("icon", note.getIcon());
        contentValues.put("color", note.getColor());
        contentValues.put("content", note.getContent());
        contentValues.put("date", format.format(new Date()));   // lấy thởi điểm hiện tại
        contentValues.put("id_parent", note.getIdParent());

        db.insert("note", null, contentValues);
        db.close();
    }
}
