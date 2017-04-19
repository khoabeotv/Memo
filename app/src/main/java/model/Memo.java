package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KhoaBeo on 4/18/2017.
 */

public class Memo {

  private List<Memo> childMemos;
  private String title;
  private String dateCreated;
  private int color;

  public Memo(String title, String dateCreated, int color) {
    this.title = title;
    this.dateCreated = dateCreated;
    this.color = color;
    this.childMemos = new ArrayList<>();
  }

  public void addChildMemo(Memo memo) {
    childMemos.add(memo);
  }

  public String getTitle() {
    return title;
  }

  public String getDateCreated() {
    return dateCreated;
  }

  public int getColor() {
    return color;
  }
}
