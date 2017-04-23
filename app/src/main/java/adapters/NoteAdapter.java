package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import model.Note;
import teambandau.memo.R;

/**
 * Created by KhoaBeo on 4/18/2017.
 */

public class NoteAdapter extends BaseAdapter {

  private List<Note> notes;
  private LayoutInflater inflater;

  public NoteAdapter(Context context, List<Note> memos) {
    this.notes = memos;
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return notes.size();
  }

  @Override
  public Object getItem(int position) {
    return notes.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    view = inflater.inflate(R.layout.note_item, null, false);
    TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
    TextView tvContent = (TextView) view.findViewById(R.id.tvContent);

    tvTitle.setText(notes.get(position).getTitle());
    tvContent.setText(notes.get(position).getContent());

    if (notes.get(position).isSelect()) {
      Log.d("dddddd","zzzzzzzzz");
      view.setBackgroundResource(android.R.color.holo_blue_bright);
    }

    return view;
  }
}
