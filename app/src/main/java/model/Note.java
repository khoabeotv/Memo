package model;

import java.util.ArrayList;

/**
 * Created by TrKaJv on 19-Apr-17.
 */

public class Note {
  private int id;
  private String title;
  private String icon;
  private String color;
  private String content;
  private String date;
  private int idParent;
  private ArrayList<String> img;

  public Note(int id, String title, String icon, String color, String content, String date, int idParent, ArrayList<String> img) {
    this.id = id;
    this.title = title;
    this.icon = icon;
    this.color = color;
    this.content = content;
    this.date = date;
    this.idParent = idParent;
    this.img = img;
  }

  public Note(int id, String title, String icon, String color, String content, String date, int idParent) {
    this.id = id;
    this.title = title;
    this.icon = icon;
    this.color = color;
    this.content = content;
    this.date = date;
    this.idParent = idParent;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getIdParent() {
    return idParent;
  }

  public void setIdParent(int idParent) {
    this.idParent = idParent;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
