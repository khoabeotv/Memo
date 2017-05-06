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

    private static final String ID__NOTE_TABLE = "id";
    private static final String TITLE__NOTE_TABLE = "title";
    private static final String ICON__NOTE_TABLE = "icon";
    private static final String COLOR__NOTE_TABLE = "color";
    private static final String CONTENT__NOTE_TABLE = "content";
    private static final String DATE__NOTE_TABLE = "date";
    private static final String ID_PARENT__NOTE_TABLE = "id_parent";

    private static final String[] ALL_COLUMN__NOTE_TABLE = {ID__NOTE_TABLE, TITLE__NOTE_TABLE, ICON__NOTE_TABLE, COLOR__NOTE_TABLE, CONTENT__NOTE_TABLE, DATE__NOTE_TABLE, ID_PARENT__NOTE_TABLE};

    private static final String NOTE_ID__NOTE_IMG_TABLE = "note_id";
    private static final String IMG__NOTE_IMG_TABLE = "img";
    private static final String[] ALL_COLUMN__NOTE_IMG_TABLE = { NOTE_ID__NOTE_IMG_TABLE, IMG__NOTE_IMG_TABLE};

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Note> loadAllNotes() {
        List<Note> nodeList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("note", ALL_COLUMN__NOTE_TABLE, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID__NOTE_TABLE));
            String title = cursor.getString(cursor.getColumnIndex(TITLE__NOTE_TABLE));
            String icon = cursor.getString(cursor.getColumnIndex(ICON__NOTE_TABLE));
            String color = cursor.getString(cursor.getColumnIndex(COLOR__NOTE_TABLE));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT__NOTE_TABLE));
            String date = cursor.getString(cursor.getColumnIndex(DATE__NOTE_TABLE));
            int id_parent = cursor.getInt(cursor.getColumnIndex(ID_PARENT__NOTE_TABLE));

            ArrayList<String> imgs = loadNoteImg(id);

            Note note = new Note(id, title, icon, color, content, date, id_parent, imgs);
            nodeList.add(note);
        }

        cursor.close();

        return nodeList;
    }


    public void moveNote(Note note, int newID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_parent", newID + "");
        db.update("note", contentValues, "id = ?", new String[]{note.getId() + ""});
    }

    public void copyNote(Note note, int newIDParent) {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("icon", note.getIcon());
        contentValues.put("color", note.getColor());
        contentValues.put("content", note.getContent());
        contentValues.put("date", format.format(new Date()));   // lấy thởi điểm hiện tại
        contentValues.put("id_parent", newIDParent);

        long newID = db.insert("note", null, contentValues);
        insertNoteImg(note, newID);

        Cursor cursor = getCursorOfChild(note.getId());
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID__NOTE_TABLE));
            String title = cursor.getString(cursor.getColumnIndex(TITLE__NOTE_TABLE));
            String icon = cursor.getString(cursor.getColumnIndex(ICON__NOTE_TABLE));
            String color = cursor.getString(cursor.getColumnIndex(COLOR__NOTE_TABLE));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT__NOTE_TABLE));
            String date = cursor.getString(cursor.getColumnIndex(DATE__NOTE_TABLE));
            int id_parent = cursor.getInt(cursor.getColumnIndex(ID_PARENT__NOTE_TABLE));
            ArrayList<String> imgs = loadNoteImg(id);

            Note noteChild = new Note(id, title, icon, color, content, date, id_parent, imgs);
            copyNote(noteChild, (int) newID);
        }
    }

    public void deleteNote(int idNote) {
        SQLiteDatabase db_wr = getWritableDatabase();

        Cursor cursor = getCursorOfChild(idNote);
        while (cursor.moveToNext()) {
            int idChild = cursor.getInt(cursor.getColumnIndex(ID__NOTE_TABLE));
            deleteNote(idChild);
        }

        db_wr.delete("note", "ID__NOTE_TABLE = ?", new String[]{idNote + ""});
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        ContentValues contentValues_NoteTable = new ContentValues();
        contentValues_NoteTable.put("title", note.getTitle());
        contentValues_NoteTable.put("icon", note.getIcon());
        contentValues_NoteTable.put("color", note.getColor());
        contentValues_NoteTable.put("content", note.getContent());
        contentValues_NoteTable.put("date", format.format(new Date()));   // lấy thởi điểm hiện tại
        contentValues_NoteTable.put("id_parent", note.getIdParent());

        long id = db.insert("note", null, contentValues_NoteTable);
        insertNoteImg(note, id);

    }

    public Cursor getCursorOfChild(int idNote) {
        SQLiteDatabase db_re = getReadableDatabase();
        Cursor cursor = db_re.query("note", ALL_COLUMN__NOTE_TABLE, "id_parent = ?", new String[]{idNote + ""}, null, null, null);
        return cursor;
    }


    private ArrayList<String> loadNoteImg(int noteID) {
        ArrayList<String> imgs = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("note_img", ALL_COLUMN__NOTE_IMG_TABLE, "note_id = ?", new String[]{noteID + ""}, null, null, null);
        while (cursor.moveToNext()) {
            String img = cursor.getString(cursor.getColumnIndex(IMG__NOTE_IMG_TABLE));
            imgs.add(img);
        }

        cursor.close();
        return imgs;
    }

    private void insertNoteImg(Note note, long id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues_NoteImgTable = new ContentValues();
        for (String img : note.getImg()) {
            contentValues_NoteImgTable.put("note_id", id);
            contentValues_NoteImgTable.put("img", img);
            db.insert("note_img", null, contentValues_NoteImgTable);
            contentValues_NoteImgTable.clear();
        }

    }

}
