package model;

import android.util.Log;

import java.io.Serializable;
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

  public static List<Note> getNoteFromTitle(String title) {
    currentNotes.clear();
    String titleSearch = title.toLowerCase();
    String[] strings = titleSearch.split(" ");
    int[] type = new int[strings.length];
    int count = 0;
    for (int i = 0; i < allNotes.size(); i++) {
      Note note = allNotes.get(i);
      String noteTitle = note.getTitle().toLowerCase();
      if (noteTitle.contains(titleSearch)) {
        currentNotes.add(0, note);
        type[strings.length - 1]++;
      } else {
        for (int j = 0; j < strings.length; j++) {
          if (noteTitle.contains(strings[j]))
            count++;
        }
        int position = 0;
        for (int k = strings.length - 1; k >= 0; k--) {
          position += type[k];
          if (count == (k + 1)) {
            type[k]++;
            currentNotes.add(position, note);
            break;
          }
        }
        count = 0;
      }
    }
    return currentNotes;
  }
}
