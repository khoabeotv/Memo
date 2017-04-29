package createnote_modul.modul_choose_color;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import createnote_modul.models.Color;
import createnote_modul.models.CusView;
import teambandau.memo.R;

/**
 * Created by l on 4/22/2017.
 */

public class CusGribAdapter extends ArrayAdapter<Color> {

    private Activity activity;
    private ArrayList<Color> objects;
    private int res;

    public CusGribAdapter(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<Color> objects) {
        super(activity, resource, objects);

        res = resource;
        this.activity = activity;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(res,null);
//        v.setMinimumHeight(height/5);
        CusView cusView = (CusView) v.findViewById(R.id.cvItem);
        cusView.setStyleDraw(objects.get(position).getDrawStyle());
        cusView.setColor(objects.get(position).getColor());

        cusView.invalidate();

        return v;
    }
}
