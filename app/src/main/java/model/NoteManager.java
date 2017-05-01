package model;

import java.util.ArrayList;
import java.util.List;

import application.NoteApplication;

/**
 * Created by KhoaBeo on 4/21/2017.
 */

public class NoteManager {

    private static List<Note> allNotes = NoteApplication.getInstance().getNoteDatabase().loadAllNotes();
    private static List<Note> currentNotes = new ArrayList<>();
    private static int currentParentId;

    public static void setCurrentNotesByParentId(int parentId) {
        currentNotes.clear();
        currentParentId = parentId;
        for (Note note : allNotes) {
            if (note.getIdParent() == parentId) {
                currentNotes.add(note);
            }
        }
    }

    public static void reloadAllNotes() {
        allNotes = NoteApplication.getInstance().getNoteDatabase().loadAllNotes();
    }

    public static List<Note> getCurrentNotes() {
        return currentNotes;
    }

    public static int getParentId() {
        return currentParentId;
    }

    public static boolean checkHasChilds(Note note) {
        for (Note note1 : allNotes) {
            if (note1.getIdParent() == note.getId()) {
                return true;
            }
        }
        return false;
    }
}
