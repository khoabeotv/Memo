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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import createnote_modul.models.CusView;
import teambandau.memo.R;


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
  private ScrollView scvContent;

  private String currentColor;
  private String oldColor;
  private String title;
  private String content;
  private int icon;

  private boolean btnSaveWasPressed = false;

  private CreateNoteFragmentForSaveNoteListener createNoteFragmentForSaveNoteListener;


  public interface CreateNoteFragmentForSaveNoteListener {
    void btnSaveWasClickedListener();

    void tabAnimationListener();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();

    createNoteFragmentForSaveNoteListener = (CreateNoteFragmentForSaveNoteListener) getActivity();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.savenote_fragment, container, false);

    cusView = (CusView) view.findViewById(R.id.cusViewSave);
    btnSave = (Button) view.findViewById(R.id.btnSave);
    tvSaveTitle = (TextView) view.findViewById(R.id.tvSaveTitle);
    tvSaveContent = (TextView) view.findViewById(R.id.tvSaveContent);
    ivIconSave = (ImageView) view.findViewById(R.id.ivIconSave);
    rl = (RelativeLayout) view.findViewById(R.id.rlSave);
    scvContent = (ScrollView) view.findViewById(R.id.scvContent);

    getAllInfoAndSet();
    startAnimation();
    addEvents();
    return view;
  }

  private void addEvents() {
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!btnSaveWasPressed) {
          cusView.setStyleDraw(cusView.DRAW_AUTOFILL_CIRCLE);
          cusView.invalidate();

          ViewGroup parent = (ViewGroup) tvSaveTitle.getParent();
          parent.removeView(tvSaveTitle);

          ViewGroup parentOfContent = (ViewGroup) scvContent.getParent();
          parentOfContent.removeView(scvContent);


          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tvSaveTitle.getWidth(), tvSaveTitle.getHeight());
          rl.addView(tvSaveTitle, params);
          RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(tvSaveContent.getWidth(), tvSaveContent.getHeight());
          params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,cusView.getId());
          rl.addView(scvContent,params1);


          AnimationSet animationSetColor = new AnimationSet(true);
          TranslateAnimation translateAnimationForColor = new TranslateAnimation(0, rl.getWidth() / 2 - cusView.getWidth() / 2, 0, 0);
          translateAnimationForColor.setDuration(2000);
          AlphaAnimation alphaAnimationForColor = new AlphaAnimation(1, 0);
          alphaAnimationForColor.setDuration(2000);
          animationSetColor.addAnimation(translateAnimationForColor);
          animationSetColor.addAnimation(alphaAnimationForColor);

          AnimationSet animationSetIcon = new AnimationSet(true);
          TranslateAnimation translateAnimationForIcon = new TranslateAnimation(0, -(rl.getWidth() / 2 + ivIconSave.getWidth() / 2), 0, -(ivIconSave.getY() - cusView.getY()));
          translateAnimationForIcon.setDuration(2000);
          AlphaAnimation alphaAnimationForIcon = new AlphaAnimation(1, 0);
          alphaAnimationForIcon.setDuration(2000);
          ScaleAnimation scaleAnimationForIcon = new ScaleAnimation(1f, 0.5f, 1f, 0.5f);
          scaleAnimationForIcon.setDuration(2000);
          animationSetIcon.addAnimation(translateAnimationForIcon);
          animationSetIcon.addAnimation(alphaAnimationForIcon);
          animationSetIcon.addAnimation(scaleAnimationForIcon);

          AnimationSet animationSetTitle = new AnimationSet(true);
          TranslateAnimation translateAnimationForTitle = new TranslateAnimation(tvSaveContent.getX(), rl.getWidth() / 2, 0, (float) (cusView.getY() + cusView.getHeight() / 2));
          translateAnimationForTitle.setDuration(2000);
          AlphaAnimation alphaAnimationForTitle = new AlphaAnimation(1, 0);
          alphaAnimationForTitle.setDuration(2000);
          ScaleAnimation scaleAnimationForTitle = new ScaleAnimation(1f, 0.2f, 1f, 0.3f);
          scaleAnimationForTitle.setDuration(2000);
          animationSetTitle.addAnimation(translateAnimationForTitle);
          animationSetTitle.addAnimation(alphaAnimationForTitle);
          animationSetTitle.addAnimation(scaleAnimationForTitle);

          AnimationSet animationSetContent = new AnimationSet(true);
          TranslateAnimation translateAnimationForContent = new TranslateAnimation(0,rl.getWidth()/2,0,-(scvContent.getY() - cusView.getY()));
          translateAnimationForContent.setDuration(2000);
          AlphaAnimation alphaAnimationForContent = new AlphaAnimation(1, 0);
          alphaAnimationForContent.setDuration(2000);
          ScaleAnimation scaleAnimationForContent = new ScaleAnimation(1f, 0.5f, 1f, 0.5f);
          scaleAnimationForTitle.setDuration(2000);
          animationSetContent.addAnimation(translateAnimationForContent);
          animationSetContent.addAnimation(scaleAnimationForContent);
          animationSetContent.addAnimation(alphaAnimationForContent);

          animationSetColor.setFillAfter(true);
          animationSetIcon.setFillAfter(true);
          animationSetTitle.setFillAfter(true);
          animationSetContent.setFillAfter(true);

          cusView.startAnimation(animationSetColor);
          ivIconSave.startAnimation(animationSetIcon);
          tvSaveTitle.startAnimation(animationSetTitle);
          scvContent.startAnimation(animationSetContent);
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
        }
      }
    });
  }

  private void startAnimation() {
    Animation animationCusview = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_cusview);
    Animation animationIcon = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_icon);
    cusView.startAnimation(animationCusview);
    ivIconSave.startAnimation(animationIcon);
  }

  private void getAllInfoAndSet() {

    icon = sharedPreferences.getInt(CreateNoteActivity.NOTE_ICON_KEY, CreateNoteActivity.DEFAULT_ICON);
    currentColor = sharedPreferences.getString(CreateNoteActivity.NOTE_COLOR_KEY, CreateNoteActivity.DEFAULT_COLOR);
    oldColor = sharedPreferences.getString(CreateNoteActivity.NOTE_OLD_COLOR_KEY, CreateNoteActivity.DEFAULT_COLOR);
    title = sharedPreferences.getString(CreateNoteActivity.NOTE_TITLE_KEY, CreateNoteActivity.DEFAULT_TITLE);
    content = sharedPreferences.getString(CreateNoteActivity.NOTE_CONTENT_KEY, CreateNoteActivity.DEFAULT_CONTENT);

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
