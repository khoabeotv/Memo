package teambandau.memo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.DictAdapter;
import adapters.NoteAdapter;
import application.NoteApplication;
import createnote_modul.main.CreateNoteActivity;
import createnote_modul.main.CreateNoteFragmentAttach;
import model.Note;
import model.NoteManager;
import util.Util;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener, SearchView.OnQueryTextListener {

  private boolean doublePressBack = false;
  private boolean doubleTapItem = false;
  private MenuItem searchItem;
  private Toolbar toolbar;
  private FloatingActionButton fab;
  private NoteAdapter noteAdapter;
  private ListView mainLv;
  private DictAdapter dictAdapter;
  private RecyclerView rvDict;
  private List<Note> dictNotes;
  private TextView tvMyNotes;
  private ImageView ivDelete;
  private ImageView ivCut;
  private ImageView ivCopy;
  private ImageView ivPaste;
  private TextView tvInBlank;
  private SearchView searchView;
  private LinearLayout layoutDict;

  private Note selectedNote;
  private boolean selectFlag = false;
  private boolean copyFlag = false;
  private boolean cutFlag = false;
  private boolean isSearching = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();
  }

  private void initView() {
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (selectFlag) {
          unSelected();
        } else {
          Intent i = new Intent(MainActivity.this, CreateNoteActivity.class);
          i.putExtra(CreateNoteActivity.NOTE_NEW_OR_UPDATE, 0);
          i.putExtra(CreateNoteActivity.NOTE_TITLE_KEY, "");
          i.putExtra(CreateNoteActivity.NOTE_CONTENT_KEY, "");
          i.putExtra(CreateNoteActivity.NOTE_COLOR_KEY, getResources().getString(R.color.color_cool_0));
          i.putExtra(CreateNoteActivity.NOTE_ICON_KEY, R.drawable.expressions0);
          i.putStringArrayListExtra(CreateNoteActivity.NOTE_ATTACH_KEY, new ArrayList<String>());
          startActivityForResult(i, CreateNoteActivity.REQUEST_CODE_CREATENOTE);
        }
      }
    });

    tvInBlank = (TextView) findViewById(R.id.tv_in_blank);
    NoteManager.setCurrentNotesByParentId(0);
    noteAdapter = new NoteAdapter(this, NoteManager.getCurrentNotes());
    mainLv = (ListView) findViewById(R.id.memos_lv);
    mainLv.setAdapter(noteAdapter);

    dictNotes = new ArrayList<>();
    rvDict = (RecyclerView) findViewById(R.id.rv_dict);
    dictAdapter = new DictAdapter(this, dictNotes, rvDict, noteAdapter, tvInBlank);
    rvDict.setAdapter(dictAdapter);
    rvDict.setOnClickListener(this);

    mainLv.setOnItemClickListener(this);
    mainLv.setOnItemLongClickListener(this);

    tvMyNotes = (TextView) findViewById(R.id.tv_my_notes);
    tvMyNotes.setTypeface(null, Typeface.BOLD);
    tvMyNotes.setOnClickListener(this);

    ivCopy = (ImageView) findViewById(R.id.iv_copy);
    ivDelete = (ImageView) findViewById(R.id.iv_delete);
    ivCut = (ImageView) findViewById(R.id.iv_cut);
    ivPaste = (ImageView) findViewById(R.id.iv_paste);

    ivCopy.setOnClickListener(this);
    ivDelete.setOnClickListener(this);
    ivCut.setOnClickListener(this);
    ivPaste.setOnClickListener(this);


    toolbar = (Toolbar) findViewById(R.id.tool_bar);
    toolbar.setTitleTextColor(Color.WHITE);
    new Handler();
    toolbar.setTitle(Util.getFullDate());
    setSupportActionBar(toolbar);
    getSupportActionBar().getDisplayOptions();

    layoutDict = (LinearLayout) findViewById(R.id.layout_dict);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    CreateNoteFragmentAttach.attaches.clear();
    switch (requestCode) {
      case (CreateNoteActivity.REQUEST_CODE_CREATENOTE): {
        if (resultCode == CreateNoteActivity.RESULT_CODE_CREATENOTE) {
          Toast.makeText(this, data.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY) + " " +
                  data.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY) + " " +
                  data.getStringExtra(CreateNoteActivity.NOTE_COLOR_KEY), Toast.LENGTH_SHORT).show();

          String title = data.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY);
          String icon = data.getStringExtra(CreateNoteActivity.NOTE_ICON_KEY);
          String color = data.getStringExtra(CreateNoteActivity.NOTE_COLOR_KEY);
          String content = data.getStringExtra(CreateNoteActivity.NOTE_CONTENT_KEY);
          int idNoteUpdate = data.getIntExtra(CreateNoteActivity.NOTE_NEW_OR_UPDATE, 0);

          SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
          String date = format.format(new Date());

          ArrayList<String> imgs = data.getStringArrayListExtra(CreateNoteActivity.NOTE_ATTACH_KEY);

          Note note = new Note(-1, title, icon, color, content, date, NoteManager.getParentId(), imgs);
          if (idNoteUpdate == 0) {
            NoteApplication.getInstance().getNoteDatabase().insertNote(note);
          } else {
            //TODO: update note
            //idNoteUpdate là cái id của note cần update
          }
          reloadAllNotes(NoteManager.getParentId());
        }
        break;
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.tool_bar, menu);

    final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchItem = menu.findItem(R.id.search_view);
    searchView = (SearchView) searchItem.getActionView();
    searchView.setQueryHint("Search Title");
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setOnQueryTextListener(this);
    searchView.setOnSearchClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        NoteManager.getCurrentNotes().clear();
        isSearching = true;
        fab.setVisibility(View.GONE);
        layoutDict.setVisibility(View.GONE);
      }
    });

    MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
      @Override
      public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
      }

      @Override
      public boolean onMenuItemActionCollapse(MenuItem item) {
        if (NoteManager.getParentId() != 0)
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setVisibility(View.VISIBLE);
        layoutDict.setVisibility(View.VISIBLE);
        searchView.setQuery("", false);
        isSearching = false;
        NoteManager.setCurrentNotesByParentId(NoteManager.getParentId());
        noteAdapter.notifyDataSetChanged();
        return true;
      }
    });

    return true;
  }

  @Override
  public void onBackPressed() {
    if (NoteManager.getParentId() == 0) {
      if (!doublePressBack) {
        doublePressBack = true;
        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            doublePressBack = false;
          }
        }, 2000);
      } else {
        super.onBackPressed();
      }
      return;
    }

    Note note = dictNotes.get(dictNotes.size() - 1);
    if (note != null) {
      int id = note.getIdParent();
      if (id == 0) {
        tvMyNotes.setTextColor(Color.parseColor("#3F51B5"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
        toolbar.setTitle(Util.getFullDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      }
      setCurrentNotesByParentId(id);
      dictNotes.remove(note);
    }
    noteAdapter.setAnimation(NoteAdapter.ANIM_LTR);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_my_notes:
        tvMyNotes.setTextColor(Color.parseColor("#3F51B5"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dictNotes.clear();
        setCurrentNotesByParentId(0);
        noteAdapter.setAnimation(NoteAdapter.ANIM_LTR);
        break;
      case R.id.iv_copy:
        setViewOnPaste();
        copyFlag = true;
        break;
      case R.id.iv_cut:
        setViewOnPaste();
        cutFlag = true;
        break;
      case R.id.iv_delete:
        NoteApplication.getInstance().getNoteDatabase().deleteNote(selectedNote.getId());
        reloadAllNotes(NoteManager.getParentId());
        break;
      case R.id.iv_paste:
        if (noteAdapter.getSelectedView() != null)
          noteAdapter.getSelectedView().setBackgroundResource(android.R.color.white);
        if (copyFlag) {
          NoteApplication.getInstance().getNoteDatabase().copyNote(selectedNote, NoteManager.getParentId());
        } else if (cutFlag) {
          NoteApplication.getInstance().getNoteDatabase().moveNote(selectedNote, NoteManager.getParentId());
        }
        reloadAllNotes(NoteManager.getParentId());
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    CountDownTimer countDownTimer = null;
    final Note note = (Note) mainLv.getAdapter().getItem(position);
    if (!doubleTapItem && isSearching == false) {
      doubleTapItem = true;
      countDownTimer = new CountDownTimer(200, 100) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          if (doubleTapItem) {
            setCurrentNotesByParentId(note.getId());
            tvMyNotes.setTypeface(null, Typeface.NORMAL);
            tvMyNotes.setTextColor(Color.parseColor("#9FA8DA"));
            dictNotes.add(note);
            if (dictNotes.size() > 1)
              rvDict.smoothScrollToPosition(dictNotes.size());
            noteAdapter.setAnimation(NoteAdapter.ANIM_RTL);
          }
          doubleTapItem = false;
        }
      };
      countDownTimer.start();
    } else {
      doubleTapItem = false;
      if (countDownTimer != null)
        countDownTimer.onFinish();
      Intent i = new Intent(MainActivity.this, CreateNoteActivity.class);
      i.putExtra(CreateNoteActivity.NOTE_TITLE_KEY, note.getTitle());
      i.putExtra(CreateNoteActivity.NOTE_CONTENT_KEY, note.getContent());

      int idColor = getResources().getIdentifier(note.getColor(),
              "color",
              getPackageName());
      i.putExtra(CreateNoteActivity.NOTE_COLOR_KEY, getResources().getString(idColor));

      i.putExtra(CreateNoteActivity.NOTE_ICON_KEY, getResources().getIdentifier(note.getIcon(), "drawable", getPackageName()));
      i.putStringArrayListExtra(CreateNoteActivity.NOTE_ATTACH_KEY, note.getImg());
      i.putExtra(CreateNoteActivity.NOTE_NEW_OR_UPDATE, note.getId());
      startActivityForResult(i, CreateNoteActivity.REQUEST_CODE_CREATENOTE);
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    if (noteAdapter.getSelectedView() != null)
      noteAdapter.getSelectedView().setBackgroundResource(android.R.color.white);
    noteAdapter.setSelectedView(view);
    noteAdapter.getSelectedView().setBackgroundResource(android.R.color.holo_blue_bright);
    List<Note> notes = NoteManager.getCurrentNotes();
    selectedNote = notes.get(position);
    toolbar.setTitle(selectedNote.getTitle());
    selectFlag = true;
    setViewOnSelected(true);

    return true;
  }

  private void setViewOnSelected(boolean onSelected) {
    ivPaste.setVisibility(View.GONE);
    int type;
    if (onSelected) {
      type = View.VISIBLE;
      fab.setImageResource(R.drawable.cancel_ic);
      searchItem.setVisible(false);
    } else {
      type = View.GONE;
      fab.setImageResource(R.drawable.ic_add_black_24dp);
      searchItem.setVisible(true);
    }
    ivCut.setVisibility(type);
    ivCopy.setVisibility(type);
    ivDelete.setVisibility(type);
  }

  private void setViewOnPaste() {
    ivCut.setVisibility(View.GONE);
    ivCopy.setVisibility(View.GONE);
    ivDelete.setVisibility(View.GONE);
    ivPaste.setVisibility(View.VISIBLE);
  }

  private void unSelected() {
    selectedNote = null;
    selectFlag = false;
    copyFlag = false;
    cutFlag = false;
    toolbar.setTitle(Util.getFullDate());
    if (noteAdapter.getSelectedView() != null)
      noteAdapter.getSelectedView().setBackgroundResource(android.R.color.white);
    setViewOnSelected(false);
  }

  private void reloadAllNotes(int idParent) {
    NoteManager.reloadAllNotes();
    setCurrentNotesByParentId(idParent);
    unSelected();
  }

  private void setCurrentNotesByParentId(int parentId) {
    NoteManager.setCurrentNotesByParentId(parentId);
    if (NoteManager.getCurrentNotes().size() == 0) {
      tvInBlank.setVisibility(View.VISIBLE);
    } else {
      tvInBlank.setVisibility(View.GONE);
    }
    noteAdapter.notifyDataSetChanged(selectedNote);
    dictAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    if (newText.equals("")) {
      NoteManager.getCurrentNotes().clear();
      noteAdapter.notifyDataSetChanged();
    } else {
      NoteManager.getNoteFromTitle(newText);
      noteAdapter.notifyDataSetChanged();
    }
    return true;
  }
}
