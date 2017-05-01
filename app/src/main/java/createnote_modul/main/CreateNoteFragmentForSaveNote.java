package createnote_modul.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import application.NoteApplication;
import createnote_modul.models.CusView;
import databases.NoteDatabase;
import model.Note;
import teambandau.memo.R;
import util.Util;


/**
 * Created by l on 4/29/2017.
 */

public class CreateNoteFragmentForSaveNote extends Fragment implements FragmentLifecycle {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CusView cusView;
    private Button btnSave;
    private TextView tvSaveTitle;
    private ImageView ivIconSave;
    private TextView tvSaveContent;
    private RelativeLayout rl;

    private String currentColor;
    private String oldColor;
    private String title;
    private String content;
    private int icon;

    private boolean btnSaveWasPressed = false;

    private CreateNoteFragmentForSaveNoteListener createNoteFragmentForSaveNoteListener;


    public static interface CreateNoteFragmentForSaveNoteListener{
        public void btnSaveWasClickedListener();
        public void tabAnimationListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        createNoteFragmentForSaveNoteListener = (CreateNoteFragmentForSaveNoteListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.savenote_fragment,container,false);

        cusView = (CusView) view.findViewById(R.id.cusViewSave);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        tvSaveTitle = (TextView) view.findViewById(R.id.tvSaveTitle);
        tvSaveContent = (TextView) view.findViewById(R.id.tvSaveContent);
        ivIconSave = (ImageView) view.findViewById(R.id.ivIconSave);
        rl = (RelativeLayout) view.findViewById(R.id.rlSave);

        getAllInfoAndSet();
        startAnimation();
        addEvents();
        return view;
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnSaveWasPressed){
                    cusView.setStyleDraw(cusView.DRAW_AUTOFILL_CIRCLE);
                    cusView.invalidate();

                    ViewGroup parent = (ViewGroup) tvSaveTitle.getParent();
                    parent.removeView(tvSaveTitle);


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tvSaveTitle.getWidth(),tvSaveTitle.getHeight());
                    rl.addView(tvSaveTitle,params);

                    AnimationSet animationSetColor = new AnimationSet(true);
                    TranslateAnimation translateAnimationForColor = new TranslateAnimation(0,rl.getWidth()/2 - cusView.getWidth()/2,0,0);
                    translateAnimationForColor.setDuration(3000);
                    AlphaAnimation alphaAnimationForColor = new AlphaAnimation(1,0);
                    alphaAnimationForColor.setDuration(3000);
                    animationSetColor.addAnimation(translateAnimationForColor);
                    animationSetColor.addAnimation(alphaAnimationForColor);

                    AnimationSet animationSetIcon = new AnimationSet(true);
                    TranslateAnimation translateAnimationForIcon = new TranslateAnimation(0,-(rl.getWidth()/2 + ivIconSave.getWidth()/2),0,-(ivIconSave.getY()-cusView.getY()));
                    translateAnimationForIcon.setDuration(3000);
                    AlphaAnimation alphaAnimationForIcon = new AlphaAnimation(1,0);
                    alphaAnimationForIcon.setDuration(3000);
                    ScaleAnimation scaleAnimationForIcon = new ScaleAnimation(1f,0.5f,1f,0.5f);
                    scaleAnimationForIcon.setDuration(3000);
                    animationSetIcon.addAnimation(translateAnimationForIcon);
                    animationSetIcon.addAnimation(alphaAnimationForIcon);
                    animationSetIcon.addAnimation(scaleAnimationForIcon);

                    AnimationSet animationSetTitle = new AnimationSet(true);
                    TranslateAnimation translateAnimationForTitle = new TranslateAnimation(tvSaveContent.getX(),rl.getWidth()/2,0,(float)(cusView.getY() + cusView.getHeight()/2-tvSaveTitle.getY()));
                    translateAnimationForTitle.setDuration(3000);
                    AlphaAnimation alphaAnimationForTitle = new AlphaAnimation(1,0);
                    alphaAnimationForTitle.setDuration(3000);
                    ScaleAnimation scaleAnimationForTitle = new ScaleAnimation(1f,0.2f,1f,0.3f);
                    scaleAnimationForTitle.setDuration(3000);
                    animationSetTitle.addAnimation(translateAnimationForTitle);
                    animationSetTitle.addAnimation(alphaAnimationForTitle);
                    animationSetTitle.addAnimation(scaleAnimationForTitle);

                    animationSetColor.setFillAfter(true);
                    animationSetIcon.setFillAfter(true);
                    animationSetTitle.setFillAfter(true);

                    cusView.startAnimation(animationSetColor);
                    ivIconSave.startAnimation(animationSetIcon);
                    tvSaveTitle.startAnimation(animationSetTitle);
                    animationSetTitle.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            createNoteFragmentForSaveNoteListener.tabAnimationListener();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            createNoteFragmentForSaveNoteListener.btnSaveWasClickedListener();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    btnSaveWasPressed = true;


                    // TODO:    CẦN TÊN CỦA ICON(String), ID_PARENT_NOTE (int),
                    // Cái currentColor : là tên màu rồi hả anh
                    Note note = new Note(0,title,"expressions22", currentColor, content, Util.getFullDate(),1);
                    NoteApplication.getInstance().getNoteDatabase().insertNote(note);
                }

            }
        });
    }

    private void startAnimation() {
        Animation animationCusview = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_cusview);
        Animation animationIcon = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_icon);
        cusView.startAnimation(animationCusview);
        ivIconSave.startAnimation(animationIcon);
    }

    private void getAllInfoAndSet() {

        icon = sharedPreferences.getInt(CreateNoteActivity.NOTE_ICON_KEY,CreateNoteActivity.DEFAULT_ICON);
        currentColor = sharedPreferences.getString(CreateNoteActivity.NOTE_COLOR_KEY,CreateNoteActivity.DEFAULT_COLOR);
        oldColor = sharedPreferences.getString(CreateNoteActivity.NOTE_OLDCOLOR_KEY,CreateNoteActivity.DEFAULT_COLOR);
        title = sharedPreferences.getString(CreateNoteActivity.NOTE_TITLE_KEY,CreateNoteActivity.DEFAULT_TITLE);
        content = sharedPreferences.getString(CreateNoteActivity.NOTE_CONTENT_KEY,CreateNoteActivity.DEFAULT_CONTENT);

        ivIconSave.setImageResource(icon);
        tvSaveTitle.setText(title);
        tvSaveContent.setText(content);

        cusView.setColor(currentColor);
        cusView.setColorOld(oldColor);
        cusView.setStyleDraw(cusView.DRAW_TWO_HALF_COLOR);
        cusView.invalidate();
    }

    @Override
    public void onResumeFragment() {
        startAnimation();
        getAllInfoAndSet();
    }

    @Override
    public void onPauseFragment() {
    }
}
