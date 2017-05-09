package createnote_modul.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import createnote_modul.animViewPager.DepthAnimation;
import createnote_modul.models.Color;
import createnote_modul.models.Icon;
import teambandau.memo.R;


public class CreateNoteActivity extends AppCompatActivity implements CreateNoteFragmentForSaveNote.CreateNoteFragmentForSaveNoteListener {
  public static final int REQUEST_CODE_CREATENOTE = 123;
  public static final int RESULT_CODE_CREATENOTE = 124;


  public static final String NOTE_CONTENT_KEY = "note_text";
  public static final String NOTE_NEW_OR_UPDATE = "new_or_update";
  public static final String NOTE_TITLE_KEY = "note_title";
  public static final String NOTE_COLOR_KEY = "note_color";
  public static final String NOTE_ICON_KEY = "note_icon";
  public static final String NOTE_OLD_COLOR_KEY = "note_old_color";
  public static final String NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY = "shared_in_CreateNodeActivity";
  public static final String NOTE_ATTACH_KEY = "note_attach_key";

  public static final Set<String> DEFAULT_ATTACH = new HashSet<>();
  public static String DEFAULT_TITLE = "";
  public static String DEFAULT_CONTENT = "";
  public static String DEFAULT_COLOR = "#01d8f9";
  public static int DEFAULT_ICON = R.drawable.expressions3;

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private ViewPagerAdapter viewPagerAdapter;

  private SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_note);

    Intent i = getIntent();
    String title = i.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY);
    String content = i.getStringExtra(CreateNoteActivity.NOTE_CONTENT_KEY);
    String color = i.getStringExtra(CreateNoteActivity.NOTE_COLOR_KEY);
    int icon = i.getIntExtra(CreateNoteActivity.NOTE_ICON_KEY, R.drawable.expressions0);
    ArrayList<String> images = i.getStringArrayListExtra(CreateNoteActivity.NOTE_ATTACH_KEY);
    boolean checkNewOrUpdate = i.getBooleanExtra(NOTE_NEW_OR_UPDATE, true);

    sharedPreferences = getSharedPreferences(NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(NOTE_TITLE_KEY, title);
    editor.putBoolean(NOTE_NEW_OR_UPDATE, checkNewOrUpdate);
    editor.putString(NOTE_CONTENT_KEY, content);
    editor.putString(NOTE_COLOR_KEY, color);
    editor.putString(NOTE_OLD_COLOR_KEY, color);
    editor.putInt(NOTE_ICON_KEY, icon);
    editor.putStringSet(NOTE_ATTACH_KEY, new HashSet<>(images));

    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

    addControls();
    addEvents();

    if (content.equals("")) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  private void addEvents() {
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      private int currentPagePosition = 0;

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        setupTabsIcon(currentPagePosition, false);
        setupTabsIcon(position, true);
        if (position != 0) {
          hideKeyboard();
        }
        FragmentLifecycle fragmentLifecycleOfReleasedFragment = (FragmentLifecycle) viewPagerAdapter.getItem(currentPagePosition);
        fragmentLifecycleOfReleasedFragment.onPauseFragment();


        FragmentLifecycle fragmentLifecycleOfSelectedFragment = (FragmentLifecycle) viewPagerAdapter.getItem(position);
        fragmentLifecycleOfSelectedFragment.onResumeFragment();

        currentPagePosition = position;
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  private void addControls() {

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    setupViewPager(viewPager);

    tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    initTabsIcon();
  }

  private void hideKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
  }

  private void setupViewPager(ViewPager viewPager) {
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    CreateNoteFragmentForWriteText createNoteFragmentForWriteText = new CreateNoteFragmentForWriteText();
    CreateNoteFragmentForChooseColor createNoteFragmentForChooseColor = new CreateNoteFragmentForChooseColor();
    CreateNoteFragmentForChooseIcon createNoteFragmentForChooseIcon = new CreateNoteFragmentForChooseIcon();
    CreateNoteFragmentForSaveNote createNoteFragmentForSaveNote = new CreateNoteFragmentForSaveNote();
    CreateNoteFragmentAttach createNoteFragmentAttach = new CreateNoteFragmentAttach();

    viewPagerAdapter.addFragment(createNoteFragmentForWriteText, "Write");
    viewPagerAdapter.addFragment(createNoteFragmentAttach,"Attach");
    viewPagerAdapter.addFragment(createNoteFragmentForChooseColor, "Color");
    viewPagerAdapter.addFragment(createNoteFragmentForChooseIcon, "Icon");
    viewPagerAdapter.addFragment(createNoteFragmentForSaveNote, "Save");

    viewPager.setAdapter(viewPagerAdapter);
    viewPager.setPageTransformer(true, new DepthAnimation());
  }

  @Override
  public void btnSaveWasClickedListener() {
    Intent i = new Intent();
    i.putExtra(NOTE_TITLE_KEY, sharedPreferences.getString(NOTE_TITLE_KEY, DEFAULT_TITLE));
    i.putExtra(NOTE_CONTENT_KEY, sharedPreferences.getString(NOTE_CONTENT_KEY, DEFAULT_CONTENT));
    i.putExtra(NOTE_COLOR_KEY, getColorName(sharedPreferences.getString(NOTE_COLOR_KEY, DEFAULT_COLOR)));
    i.putExtra(NOTE_ICON_KEY, Icon.hashMapIconName.get(sharedPreferences.getInt(NOTE_ICON_KEY, DEFAULT_ICON)));
    ArrayList<String> attachments = new ArrayList<>();
    attachments.addAll(sharedPreferences.getStringSet(NOTE_ATTACH_KEY, DEFAULT_ATTACH));
    i.putStringArrayListExtra(NOTE_ATTACH_KEY, attachments);
    i.putExtra(NOTE_NEW_OR_UPDATE, sharedPreferences.getBoolean(NOTE_NEW_OR_UPDATE, true));
    setResult(RESULT_CODE_CREATENOTE, i);
    finish();
  }

  public String getColorName(String s){
    if(Color.hashMapWarmColors.containsKey(s)){
      return "color_warm_" + Color.hashMapWarmColors.get(s);
    }else{
      return "color_cool_" + Color.hashMapCoolColors.get(s);
    }
  }

  @Override
  public void tabAnimationListener() {
    LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
    tabStrip.setEnabled(false);
    for (int i = 0; i < tabStrip.getChildCount(); i++) {
      tabStrip.getChildAt(i).setClickable(false);
      viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {

        }
      });

      viewPager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
          return true;
        }
      });
    }
  }

  class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
      return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }


  }

  public void initTabsIcon() {
    tabLayout.getTabAt(0).setText("");
    tabLayout.getTabAt(1).setText("");
    tabLayout.getTabAt(2).setText("");
    tabLayout.getTabAt(3).setText("");
    tabLayout.getTabAt(4).setText("");

    tabLayout.getTabAt(0).setIcon(R.drawable.ic_write_selected);
    tabLayout.getTabAt(2).setIcon(R.drawable.ic_color);
    tabLayout.getTabAt(3).setIcon(R.drawable.ic_icon);
    tabLayout.getTabAt(4).setIcon(R.drawable.ic_save);
    tabLayout.getTabAt(1).setIcon(R.drawable.ic_attachment);
  }

  public void setupTabsIcon(int position, boolean selected) {
    if (selected) {
      switch (position) {
        case 0:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_write_selected);
          break;
        case 2:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_color_selected);
          break;
        case 3:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_icon_selected);
          break;
        case 4:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_save_selected);
          break;
        case 1:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_attachment_selected);
          break;
      }
    } else {
      switch (position) {
        case 0:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_write);
          break;
        case 2:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_color);
          break;
        case 3:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_icon);
          break;
        case 4:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_save);
          break;
        case 1:
          tabLayout.getTabAt(position).setIcon(R.drawable.ic_attachment);
          break;
      }
    }
  }
}

