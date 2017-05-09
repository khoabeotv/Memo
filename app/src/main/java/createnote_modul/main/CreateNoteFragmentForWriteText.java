package createnote_modul.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private FloatingActionButton fabRecord,fabRecording,fabPlaying;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;

    private View.OnClickListener onClickListener;

    private String mFile;
    public CreateNoteFragmentForWriteText() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id){
                    case R.id.fab_record:
                        animateFabRecord();
                        break;
                    case R.id.fab_recording:
//                        recordProcessing();
                        break;
                    case R.id.fab_playing:
//                        playProcessing();
                        break;
                }
            }
        };
    }

    private void animateFabRecord() {

        if(isFabOpen){

            fabRecord.startAnimation(rotate_backward);
            fabPlaying.startAnimation(fab_close);
            fabRecording.startAnimation(fab_close);
            fabRecording.setClickable(false);
            fabPlaying.setClickable(false);
            isFabOpen = false;
        } else {
            fabRecord.startAnimation(rotate_forward);
            fabPlaying.startAnimation(fab_open);
            fabRecording.startAnimation(fab_open);
            fabPlaying.setClickable(true);
            fabRecording.setClickable(true);
            isFabOpen = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.writetext_fragment, container, false);

        edTitle = (EditText) view.findViewById(R.id.edTitle);
        edContent = (EditText) view.findViewById(R.id.edContent);
        fabRecord = (FloatingActionButton) view.findViewById(R.id.fab_record);
        fabRecording = (FloatingActionButton) view.findViewById(R.id.fab_recording);
        fabPlaying = (FloatingActionButton) view.findViewById(R.id.fab_playing);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_backward);
        
        //edContent.setMovementMethod(new ScrollingMovementMethod());
        fabPlaying.setOnClickListener(onClickListener);
        fabRecord.setOnClickListener(onClickListener);

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
