package createnote_modul.main;

import android.app.Activity;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import createnote_modul.animViewPager.DepthAnimation;
import teambandau.memo.R;


public class CreateNoteActivity extends AppCompatActivity implements CreateNoteFragmentForSaveNote.CreateNoteFragmentForSaveNoteListener {
    public static final int REQUEST_CODE_CREATENOTE = 123;
    public static final int RESULT_CODE_CREATENOTE = 124;

    public static String NOTE_CONTENT_KEY = "notetext";
    public static String NOTE_TITLE_KEY = "notetitle";
    public static String NOTE_COLOR_KEY = "notecolor";
    public static String NOTE_ICON_KEY = "noteicon";
    public static String NOTE_OLDCOLOR_KEY = "noteoldcolor";
    public static String NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY = "shared_in_CreateNodeActivity";

    public static String DEFAULT_TITLE = "";
    public static String DEFAULT_CONTENT = "";
    public static String DEFAULT_COLOR = "#01d8f9";
    public static int DEFAULT_ICON = R.drawable.expressions3;

    private Toolbar toolbar;
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
        int icon = i.getIntExtra(CreateNoteActivity.NOTE_ICON_KEY,R.drawable.expressions0);

        sharedPreferences = getSharedPreferences(NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOTE_TITLE_KEY,title);
        editor.putString(NOTE_CONTENT_KEY,content);
        editor.putString(NOTE_COLOR_KEY,color);
        editor.putString(NOTE_OLDCOLOR_KEY,color);
        editor.putInt(NOTE_ICON_KEY,icon);

        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

        addControls();
        addEvents();
    }

    private void addEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPagePosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position != 0){
                    hideKeyboard();
                }
                FragmentLifecycle fragmentLifecycleOfReleasedFragment = (FragmentLifecycle)viewPagerAdapter.getItem(currentPagePosition);
                fragmentLifecycleOfReleasedFragment.onPauseFragment();


                FragmentLifecycle fragmentLifecycleOfSelectedFragment = (FragmentLifecycle)viewPagerAdapter.getItem(position);
                fragmentLifecycleOfSelectedFragment.onResumeFragment();

                currentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(),0);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        CreateNoteFragmentForWriteText createNoteFragmentForWriteText = new CreateNoteFragmentForWriteText();
        CreateNoteFragmentForChooseColor createNoteFragmentForChooseColor = new CreateNoteFragmentForChooseColor();
        CreateNoteFragmentForChooseIcon createNoteFragmentForChooseIcon = new CreateNoteFragmentForChooseIcon();
        CreateNoteFragmentForSaveNote createNoteFragmentForSaveNote = new CreateNoteFragmentForSaveNote();

        viewPagerAdapter.addFragment(createNoteFragmentForWriteText, "Write");
        viewPagerAdapter.addFragment(createNoteFragmentForChooseColor, "Color");
        viewPagerAdapter.addFragment(createNoteFragmentForChooseIcon, "Icon");
        viewPagerAdapter.addFragment(createNoteFragmentForSaveNote, "Save");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true,new DepthAnimation());
    }

    @Override
    public void btnSaveWasClickedListener() {
        Intent i = new Intent();
        i.putExtra(NOTE_TITLE_KEY,sharedPreferences.getString(NOTE_TITLE_KEY,DEFAULT_TITLE));
        i.putExtra(NOTE_CONTENT_KEY,sharedPreferences.getString(NOTE_CONTENT_KEY,DEFAULT_CONTENT));
        i.putExtra(NOTE_COLOR_KEY,sharedPreferences.getString(NOTE_COLOR_KEY,DEFAULT_COLOR));
        i.putExtra(NOTE_ICON_KEY,sharedPreferences.getInt(NOTE_ICON_KEY,DEFAULT_ICON));


        setResult(RESULT_CODE_CREATENOTE,i);
        finish();
        Log.d("abc","ketthuc");
    }

    @Override
    public void tabAnimationListener() {
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
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

    public void setupTabsIcon(){
        tabLayout.getTabAt(0).setIcon(R.drawable.write);
        tabLayout.getTabAt(1).setIcon(R.drawable.color);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.save1);
    }

}

