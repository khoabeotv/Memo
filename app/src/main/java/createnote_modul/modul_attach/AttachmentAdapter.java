package createnote_modul.modul_attach;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.util.List;

import teambandau.memo.R;

/**
 * Created by KhoaBeo on 5/5/2017.
 */

public class AttachmentAdapter extends BaseAdapter {

  private Context context;
  private List<String> images;
  private LayoutInflater inflater;

  public AttachmentAdapter(Context context, List<String> images) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.images = images;
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @Override
  public Object getItem(int position) {
    return images.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View view, ViewGroup parent) {
    ViewHolder holder = null;
    if (view == null) {
      view = inflater.inflate(R.layout.attach_item, null, false);
      holder = new ViewHolder();
      holder.imageView = (ImageView) view.findViewById(R.id.im_attach);
      holder.imDelete = (ImageView) view.findViewById(R.id.im_delete);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    holder.imageView.setImageURI(Uri.parse(images.get(position)));
    holder.imDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        images.remove(position);
        notifyDataSetChanged();
      }
    });

    return view;
  }

  static class ViewHolder {
    ImageView imageView;
    ImageView imDelete;
  }
}
