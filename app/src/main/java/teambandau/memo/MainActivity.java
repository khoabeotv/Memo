package teambandau.memo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

import adapters.DictAdapter;
import adapters.NoteAdapter;
import application.NoteApplication;
import model.Note;
import model.NoteManager;
import util.Util;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

  private boolean doublePressBack = false;
  public static boolean isBack;

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

  private Note selectedNote;
  private View selectedView;
  private boolean selectFlag = false;
  private boolean copyFlag = false;
  private boolean cutFlag = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initFragmentStack();
    initView();
  }

  private void initView() {
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (selectFlag) {
          unSelected();
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

    toolbar = (Toolbar) findViewById(R.id.tool_bar);
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setTitle(Util.getFullDate());
    setSupportActionBar(toolbar);
    getSupportActionBar().getDisplayOptions();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.tool_bar, menu);

    //final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchItem = menu.findItem(R.id.search_view);
    searchView = (SearchView) searchItem.getActionView();
    searchView.setQueryHint("Search");
    //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setSubmitButtonEnabled(true);
//    searchView.setOnSearchClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//      }
//    });

    return super.onCreateOptionsMenu(menu);
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
      }
      setCurrentNotesByParentId(id);
      dictNotes.remove(note);
    }
    isBack = true;
  }

  private void initFragmentStack() {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.commit();
  }

  public void changeFragmentStack(Fragment fragment) {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.show(fragment);
    transaction.commit();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_my_notes:
        tvMyNotes.setTextColor(Color.parseColor("#3F51B5"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
        dictNotes.clear();
        setCurrentNotesByParentId(0);
        isBack = true;
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
    Note note = (Note) mainLv.getAdapter().getItem(position);
    setCurrentNotesByParentId(note.getId());
    tvMyNotes.setTypeface(null, Typeface.NORMAL);
    tvMyNotes.setTextColor(Color.parseColor("#9FA8DA"));
    dictNotes.add(note);
    if (dictNotes.size() > 1)
      rvDict.smoothScrollToPosition(dictNotes.size());
    isBack = false;
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    if (selectedView != null)
      selectedView.setBackgroundResource(android.R.color.white);
    selectedView = view;
    selectedView.setBackgroundResource(android.R.color.holo_blue_bright);
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
    selectFlag = false;
    copyFlag = false;
    cutFlag = false;
    toolbar.setTitle(Util.getFullDate());
    selectedView.setBackgroundResource(android.R.color.white);
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
    noteAdapter.notifyDataSetChanged();
    dictAdapter.notifyDataSetChanged();
  }
}
