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

  public static List<Note> getCurrentNotesByParentId(int parentId) {
    currentNotes.clear();
    for (Note note: allNotes) {
      if (note.getIdParent() == parentId) {
        currentNotes.add(note);
      }
    }
    return currentNotes;
  }

  public static List<Note> getCurrentNotes() {
    return currentNotes;
  }

  public static List<Note> getAllNotes() {
    return allNotes;
  }

  public static int getParentId() {
    return currentNotes.get(0).getIdParent();
  }

  public static Note findNoteById(int id) {
    for (Note note: allNotes) {
      if (note.getId() == id) {
        return note;
      }
    }
    return null;
  }
}
