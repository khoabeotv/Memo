package adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Note;
import model.NoteManager;
import teambandau.memo.MainActivity;
import teambandau.memo.MainFragment;
import teambandau.memo.R;

/**
 * Created by KhoaBeo on 4/22/2017.
 */

public class DictAdapter extends RecyclerView.Adapter<DictAdapter.ViewHolder> implements OnClickListener {

  private RecyclerView recyclerView;
  private List<Note> notes;
  private LayoutInflater inflater;
  private NoteAdapter noteAdapter;
  private TextView tvInBlank;

  public DictAdapter(Context context, List<Note> memos, RecyclerView recyclerView, NoteAdapter noteAdapter, TextView textView) {
    this.notes = memos;
    this.noteAdapter = noteAdapter;
    this.inflater = LayoutInflater.from(context);
    this.recyclerView = recyclerView;
    this.tvInBlank = textView;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.dict_item, null, false);
    view.setOnClickListener(this);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.tvTitle.setText(notes.get(position).getTitle());
    if (position == notes.size() - 1) {
      holder.tvTitle.setTypeface(null, Typeface.BOLD);
      holder.tvTitle.setTextColor(Color.parseColor("#3F51B5"));
      holder.imageView.setVisibility(View.INVISIBLE);
    } else {
      holder.tvTitle.setTypeface(null, Typeface.NORMAL);
      holder.tvTitle.setTextColor(Color.parseColor("#9FA8DA"));
      holder.imageView.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  @Override
  public void onClick(View v) {
    int itemPosition = recyclerView.getChildLayoutPosition(v);
    if (itemPosition != notes.size() - 1) {
      for (int i = itemPosition + 1; i < notes.size(); i++) {
        notes.remove(i);
        i--;
      }
      tvInBlank.setVisibility(View.GONE);
      NoteManager.setCurrentNotesByParentId(notes.get(itemPosition).getId());
      notifyDataSetChanged();
      noteAdapter.notifyDataSetChanged();
      MainFragment.isBack = true;
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    ImageView imageView;

    public ViewHolder(View itemView) {
      super(itemView);
      tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
      imageView = (ImageView) itemView.findViewById(R.id.im_next);
    }
  }
}
