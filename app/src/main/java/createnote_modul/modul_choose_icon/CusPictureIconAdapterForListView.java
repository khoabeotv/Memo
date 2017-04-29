package createnote_modul.modul_choose_icon;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import createnote_modul.models.Icon;
import teambandau.memo.R;


/**
 * Created by l on 4/23/2017.
 */

public class CusPictureIconAdapterForListView extends ArrayAdapter<String> {
    private Activity activity;
    private int res;
    private ArrayList<String> objects;

    public CusPictureIconAdapterForListView(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.res = resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_picture,null);

        TextView tv = (TextView) view.findViewById(R.id.tvNameTypeInList);
        ImageView iv1 = (ImageView) view.findViewById(R.id.ivIcon1);
        ImageView iv2 = (ImageView) view.findViewById(R.id.ivIcon2);
        ImageView iv3 = (ImageView) view.findViewById(R.id.ivIcon3);

        tv.setText(objects.get(position));
        iv1.setImageResource(Icon.hashMapIcon.get(Icon.ICON_EXPRESSIONS).get(0));
        iv2.setImageResource(Icon.hashMapIcon.get(Icon.ICON_EXPRESSIONS).get(1));
        iv3.setImageResource(Icon.hashMapIcon.get(Icon.ICON_EXPRESSIONS).get(2));

        return view;
    }
}
