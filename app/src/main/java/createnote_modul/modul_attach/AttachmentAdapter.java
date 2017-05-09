package createnote_modul.modul_attach;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import teambandau.memo.R;

/**
 * Created by KhoaBeo on 5/5/2017.
 */

public class AttachmentAdapter extends BaseAdapter {

  private Context context;
  private List<String> attaches;
  private LayoutInflater inflater;

  public AttachmentAdapter(Context context, List<String> images) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.attaches = images;
  }

  @Override
  public int getCount() {
    return attaches.size();
  }

  @Override
  public Object getItem(int position) {
    return attaches.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view == null) {
      view = inflater.inflate(R.layout.attach_item, null, false);
      holder = new ViewHolder();
      holder.imageView = (ImageView) view.findViewById(R.id.im_attach);
      holder.tvName = (TextView) view.findViewById(R.id.tv_attach_name);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }


    String fileName = Uri.parse(attaches.get(position)).getLastPathSegment();
    if (fileName.contains(".pdf") || fileName.contains(".txt") || fileName.contains(".docx")) {
      holder.tvName.setText(fileName);
      holder.imageView.setImageResource(R.drawable.ic_document);
    } else {
      holder.imageView.setImageURI(Uri.parse(attaches.get(position)));
    }
    return view;
  }

  static class ViewHolder {
    ImageView imageView;
    TextView tvName;
  }
}
