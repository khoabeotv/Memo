package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import model.Memo;
import teambandau.memo.R;

/**
 * Created by KhoaBeo on 4/18/2017.
 */

public class MainAdapter extends BaseAdapter {

  private List<Memo> memos;
  private LayoutInflater inflater;

  public MainAdapter(Context context, List<Memo> memos) {
    this.memos = memos;
    this.inflater = LayoutInflater.from(context);
  }


  @Override
  public int getCount() {
    return memos.size();
  }

  @Override
  public Object getItem(int position) {
    return memos.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    ViewHolder viewHolder;
    if (view == null) {
      viewHolder = new ViewHolder();
      view = inflater.inflate(R.layout.memo_item, null, false);
      viewHolder.title = (TextView) view.findViewById(R.id.title_tv);
      viewHolder.date = (TextView) view.findViewById(R.id.date_tv);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }

    viewHolder.title.setText(memos.get(position).getTitle());
    viewHolder.date.setText(memos.get(position).getDateCreated());

    return view;
  }

  private class ViewHolder {
    TextView title;
    TextView date;
  }
}
