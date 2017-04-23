package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import model.Note;
import teambandau.memo.MainFragment;
import teambandau.memo.R;

/**
 * Created by KhoaBeo on 4/18/2017.
 */

public class NoteAdapter extends BaseAdapter {

    private List<Note> notes;
    private LayoutInflater inflater;
    private boolean[] animationStates;

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
        animationStates = new boolean[notes.size()];
        view = inflater.inflate(R.layout.note_item, null, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        View icon_v = view.findViewById(R.id.icon_v);
        if (!animationStates[position]) {
            animationStates[position] = true;
            int typeAnim;
            if (MainFragment.isBack) {
                typeAnim = R.anim.left_to_right;
            } else {
                typeAnim = R.anim.right_to_left;
            }
            Animation animation = AnimationUtils.loadAnimation(parent.getContext(), typeAnim);
            animation.setStartOffset(0);
            view.startAnimation(animation);
        }

        tvTitle.setText(notes.get(position).getTitle());
        tvContent.setText(notes.get(position).getContent());

        if (notes.get(position).isSelect()) {
            view.setBackgroundResource(android.R.color.holo_blue_bright);
        }

        return view;
    }
}
