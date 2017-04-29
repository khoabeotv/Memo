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

import java.util.ArrayList;

import teambandau.memo.R;


/**
 * Created by l on 4/23/2017.
 */

public class CusPictureIconAdapterForGidView extends ArrayAdapter<Integer> {
    private Activity activity;
    private int res;
    private ArrayList<Integer> objects;

    public CusPictureIconAdapterForGidView(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<Integer> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.res = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(res,null);
        ImageView iv = (ImageView) v.findViewById(R.id.ivGridPicture);

        iv.setImageResource(objects.get(position));
        return v;
    }
}
