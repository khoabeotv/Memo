package teambandau.memo;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
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

  private Note selectedNote;
  private boolean selectFlag = false;
  private boolean copyFlag = false;
  private boolean cutFlag = false;
  private int countExit = 0;

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

    tvTitle = (TextView) view.findViewById(R.id.tv_date);
    tvTitle.setText(Util.getFullDate());

    noteAdapter = new NoteAdapter(getActivity(), NoteManager.getCurrentNotesByParentId(0));
    mainLv = (ListView) view.findViewById(R.id.memos_lv);
    mainLv.setAdapter(noteAdapter);

    dictNotes = new ArrayList<>();
    rvDict = (RecyclerView) view.findViewById(R.id.rv_dict);
    dictAdapter = new DictAdapter(getActivity(), dictNotes, rvDict, noteAdapter);
    rvDict.setAdapter(dictAdapter);

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
    if (NoteManager.getParentId() == 0) {
      if (countExit == 0) {
        countExit++;
        Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {

          }

          @Override
          public void onFinish() {
            countExit = 0;
          }
        };
        countDownTimer.start();
        return;
      }
      if (countExit == 1) {
        getActivity().finish();
        unSelected();
      }
    }

    isBack = true;
    Note note = NoteManager.findNoteById(((Note) mainLv.getAdapter().getItem(0)).getIdParent());
    if (note != null) {
      int id = note.getIdParent();
      if (id == 0) {
        tvMyNotes.setTextColor(Color.parseColor("#3F51B5"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
      }
      NoteManager.getCurrentNotesByParentId(id);
      dictNotes.remove(note);
      dictAdapter.notifyDataSetChanged();
      noteAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Note note = (Note) mainLv.getAdapter().getItem(position);
    List<Note> currentNotes = NoteManager.getCurrentNotesByParentId(note.getId());
    if (currentNotes.size() == 0) {
      NoteManager.getCurrentNotesByParentId(note.getIdParent());
    } else {
      tvMyNotes.setTypeface(null, Typeface.NORMAL);
      tvMyNotes.setTextColor(Color.parseColor("#9FA8DA"));
      dictNotes.add(note);
      dictAdapter.notifyDataSetChanged();
      if (dictNotes.size() > 1)
        rvDict.smoothScrollToPosition(dictNotes.size());
      noteAdapter.notifyDataSetChanged();
    }
    isBack = false;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_my_notes:
        tvMyNotes.setTextColor(Color.parseColor("#3F51B5"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
        dictNotes.clear();
        NoteManager.getCurrentNotesByParentId(0);
        noteAdapter.notifyDataSetChanged();
        dictAdapter.notifyDataSetChanged();
        break;
      case R.id.iv_copy:
        //TODO: xu ly copy
        setViewOnPaste();
        copyFlag = true;
        break;
      case R.id.iv_cut:
        //TODO: xu ly cut
        setViewOnPaste();
        cutFlag = true;
        break;
      case R.id.iv_delete:
        NoteApplication.getInstance().getNoteDatabase().deleteNote(selectedNote.getId());
        reloadAllNotes();
        unSelected();
        //TODO: xu ly delete
        break;
      case R.id.iv_paste:
        if (copyFlag) {
          NoteApplication.getInstance().getNoteDatabase().copyNote(selectedNote, NoteManager.getParentId());
        } else if (cutFlag) {
          NoteApplication.getInstance().getNoteDatabase().moveNote(selectedNote, NoteManager.getParentId());
        }
        reloadAllNotes();
        unSelected();
        //TODO: xu ly paste;
        break;
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    List<Note> notes = NoteManager.getCurrentNotes();
    selectedNote = notes.get(position);
    for (Note note : NoteManager.getAllNotes()) {
      note.setSelect(false);
    }
    selectedNote.setSelect(true);
    tvTitle.setText(selectedNote.getTitle());
    noteAdapter.notifyDataSetChanged();
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
    for (Note note : NoteManager.getAllNotes()) {
      if (note.isSelect()) {
        note.setSelect(false);
        break;
      }
    }
    noteAdapter.notifyDataSetChanged();
    setViewOnSelected(false);
  }

  private void reloadAllNotes() {
    NoteManager.reloadAllNotes();
    NoteManager.getCurrentNotesByParentId(NoteManager.getParentId());
    noteAdapter.notifyDataSetChanged();
  }
}
