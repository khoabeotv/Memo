package teambandau.memo;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

/**
 * Created by KhoaBeo on 4/21/2017.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

  public static boolean isBack;

  private FloatingActionButton fab;
  private NoteAdapter noteAdapter;
  private ListView mainLv;
  private DictAdapter dictAdapter;
  private RecyclerView rvDict;
  private List<Note> dictNotes;
  private TextView tvMyNotes;
  private TextView tvTitle;
  private ImageView ivDelete;
  private ImageView ivCut;
  private ImageView ivCopy;
  private ImageView ivSearch;
  private ImageView ivPaste;
  private TextView tvInBlank;

  private Note selectedNote;
  private View selectedView;
  private boolean selectFlag = false;
  private boolean copyFlag = false;
  private boolean cutFlag = false;
  private boolean doublePressBack = false;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    initView(view);
    return view;
  }

  private void initView(View view) {
    fab = (FloatingActionButton) view.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (selectFlag) {
          unSelected();
        }
      }
    });

    tvInBlank = (TextView) view.findViewById(R.id.tv_in_blank);
    tvTitle = (TextView) view.findViewById(R.id.tv_date);
    tvTitle.setText(Util.getFullDate());
    NoteManager.setCurrentNotesByParentId(0);
    noteAdapter = new NoteAdapter(getActivity(), NoteManager.getCurrentNotes());
    mainLv = (ListView) view.findViewById(R.id.memos_lv);
    mainLv.setAdapter(noteAdapter);

    dictNotes = new ArrayList<>();
    rvDict = (RecyclerView) view.findViewById(R.id.rv_dict);
    dictAdapter = new DictAdapter(getActivity(), dictNotes, rvDict, noteAdapter, tvInBlank);
    rvDict.setAdapter(dictAdapter);
    rvDict.setOnClickListener(this);

    mainLv.setOnItemClickListener(this);
    mainLv.setOnItemLongClickListener(this);

    tvMyNotes = (TextView) view.findViewById(R.id.tv_my_notes);
    tvMyNotes.setTypeface(null, Typeface.BOLD);
    tvMyNotes.setOnClickListener(this);

    ivCopy = (ImageView) view.findViewById(R.id.iv_copy);
    ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
    ivCut = (ImageView) view.findViewById(R.id.iv_cut);
    ivSearch = (ImageView) view.findViewById(R.id.iv_search);
    ivPaste = (ImageView) view.findViewById(R.id.iv_paste);

    ivCopy.setOnClickListener(this);
    ivDelete.setOnClickListener(this);
    ivCut.setOnClickListener(this);
    ivSearch.setOnClickListener(this);
    ivPaste.setOnClickListener(this);
  }

  public void onBackPressed() {
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
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    if (selectedView != null)
      selectedView.setBackgroundResource(android.R.color.white);
    selectedView = view;
    selectedView.setBackgroundResource(android.R.color.holo_blue_bright);
    List<Note> notes = NoteManager.getCurrentNotes();
    selectedNote = notes.get(position);
    tvTitle.setText(selectedNote.getTitle());
    selectFlag = true;
    setViewOnSelected(true);

    return true;
  }

  private void setViewOnSelected(boolean onSelected) {
    ivPaste.setVisibility(View.GONE);
    int type;
    if (onSelected) {
      type = View.VISIBLE;
      ivSearch.setVisibility(View.GONE);
      fab.setImageResource(R.drawable.cancel_ic);
    } else {
      type = View.GONE;
      ivSearch.setVisibility(View.VISIBLE);
      fab.setImageResource(R.drawable.add_ic);
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
    tvTitle.setText(Util.getFullDate());
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
