package createnote_modul.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import teambandau.memo.R;


/**
 * Created by l on 4/25/2017.
 */

public class CreateNoteFragmentForWriteText extends Fragment implements FragmentLifecycle {
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  private String title;
  private String content;

  private EditText edTitle;
  private EditText edContent;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.writetext_fragment, container, false);

    edTitle = (EditText) view.findViewById(R.id.edTitle);
    edContent = (EditText) view.findViewById(R.id.edContent);
    //edContent.setMovementMethod(new ScrollingMovementMethod());

    getTitle_ContentAndSetTitle_ContentEditText();

    return view;
  }


  @Override
  public void onResumeFragment() {
    getTitle_ContentAndSetTitle_ContentEditText();
  }

  @Override
  public void onPauseFragment() {
    editor.putString(CreateNoteActivity.NOTE_TITLE_KEY, edTitle.getText().toString());
    editor.putString(CreateNoteActivity.NOTE_CONTENT_KEY, edContent.getText().toString());
    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
  }

  public void getTitle_ContentAndSetTitle_ContentEditText() {
    title = sharedPreferences.getString(CreateNoteActivity.NOTE_TITLE_KEY, CreateNoteActivity.DEFAULT_TITLE);
    content = sharedPreferences.getString(CreateNoteActivity.NOTE_CONTENT_KEY, CreateNoteActivity.DEFAULT_CONTENT);
    edTitle.setText(title);
    edContent.setText(content);
    if (title.equals("")) {
      edContent.requestFocus();
    }
  }

  @Override
  public String toString() {
    return "CreateNoteFragmentForWriteText{}";
  }
}
