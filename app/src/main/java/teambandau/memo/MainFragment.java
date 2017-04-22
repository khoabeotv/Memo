package teambandau.memo;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.DictAdapter;
import adapters.NoteAdapter;
import model.Note;
import model.NoteManager;

/**
 * Created by KhoaBeo on 4/21/2017.
 */

public class MainFragment extends Fragment {

  private NoteAdapter noteAdapter;
  private ListView mainLv;
  private DictAdapter dictAdapter;
  private RecyclerView rvDict;
  private List<Note> dictNotes;
  private TextView tvMyNotes;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    initView(view);
    return view;
  }

  private void initView(View view) {
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    ((MainActivity) getActivity()).setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

    noteAdapter = new NoteAdapter(getActivity(), NoteManager.getCurrentNotesByParentId(0));
    mainLv = (ListView) view.findViewById(R.id.memos_lv);
    mainLv.setAdapter(noteAdapter);

    dictNotes = new ArrayList<>();
    rvDict = (RecyclerView) view.findViewById(R.id.rv_dict);
    dictAdapter = new DictAdapter(getActivity(), dictNotes, rvDict, noteAdapter);
    rvDict.setAdapter(dictAdapter);

    mainLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note note = (Note) mainLv.getAdapter().getItem(position);
        List<Note> currentNotes = NoteManager.getCurrentNotesByParentId(note.getId());
        if (currentNotes.size() == 0) {
          NoteManager.getCurrentNotesByParentId(note.getIdParent());
        } else {
          tvMyNotes.setTypeface(null, Typeface.NORMAL);
          tvMyNotes.setTextColor(Color.parseColor("#BDBDBD"));
          dictNotes.add(note);
          dictAdapter.notifyDataSetChanged();
          if (dictNotes.size() > 1)
            rvDict.smoothScrollToPosition(dictNotes.size());
        }
        noteAdapter.notifyDataSetChanged();
      }
    });

    tvMyNotes = (TextView) view.findViewById(R.id.tv_my_notes);
    tvMyNotes.setTypeface(null, Typeface.BOLD);
    tvMyNotes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tvMyNotes.setTextColor(Color.parseColor("#FFFFFF"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
        dictNotes.clear();
        NoteManager.getCurrentNotesByParentId(0);
        noteAdapter.notifyDataSetChanged();
        dictAdapter.notifyDataSetChanged();
      }
    });
  }

  public void onBackPressed() {
    Note note = NoteManager.findNoteById(((Note) mainLv.getAdapter().getItem(0)).getIdParent());
    if (note != null) {
      int id = note.getIdParent();
      if (id == 0) {
        tvMyNotes.setTextColor(Color.parseColor("#FFFFFF"));
        tvMyNotes.setTypeface(null, Typeface.BOLD);
      }
      NoteManager.getCurrentNotesByParentId(id);
      dictNotes.remove(note);
      dictAdapter.notifyDataSetChanged();
      noteAdapter.notifyDataSetChanged();
    }
  }
}
