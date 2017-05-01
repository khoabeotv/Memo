package adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Note;
import model.NoteManager;
import teambandau.memo.R;

/**
 * Created by KhoaBeo on 4/18/2017.
 */

public class NoteAdapter extends BaseAdapter {

    public static final String ANIM_LTR = "left_to_right";
    public static final String ANIM_RTL = "right_to_left";

    private List<Note> notes;
    private LayoutInflater inflater;
    private Context context;
    private int animType;
    private Note selectedNote;
    private View selectedView;

    public NoteAdapter(Context context, List<Note> memos) {
        this.context = context;
        this.notes = memos;
        this.inflater = LayoutInflater.from(context);
        this.animType = R.anim.right_to_left;
    }


    public void notifyDataSetChanged(Note selectedNote) {
        this.selectedNote = selectedNote;
        notifyDataSetChanged();
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

    public void setAnimation(String animationType) {
        switch (animationType) {
            case ANIM_LTR:
                animType = R.anim.left_to_right;
                break;
            case ANIM_RTL:
                animType = R.anim.right_to_left;
                break;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.note_item, null, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);

        View ivColor = view.findViewById(R.id.ivColor);
        GradientDrawable bgShape = (GradientDrawable) ivColor.getBackground();
        int idColor = context.getResources().getIdentifier(notes.get(position).getColor(), "color", context.getPackageName());
        bgShape.setColor(context.getResources().getColor(idColor));

        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        int idIcon = context.getResources().getIdentifier(notes.get(position).getIcon(), "drawable", context.getPackageName());
        ivIcon.setImageResource(idIcon);

        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setText(notes.get(position).getDate());

        Animation animation = AnimationUtils.loadAnimation(parent.getContext(), animType);
        animation.setStartOffset(0);
        view.startAnimation(animation);

        TextView tvHasChild = (TextView) view.findViewById(R.id.tvHasChild);
        if (NoteManager.checkHasChilds(notes.get(position))) {
            tvHasChild.setText("...");
        } else {
            tvHasChild.setText("");
        }

        tvTitle.setText(notes.get(position).getTitle());
        tvContent.setText(notes.get(position).getContent());


        if (selectedNote != null) {
            if (notes.get(position).getId() == selectedNote.getId()) {
                selectedView = view;
                view.setBackgroundResource(android.R.color.holo_blue_bright);
            }
        }

        return view;
    }

    public View getSelectedView() {
        return selectedView;
    }

    public void setSelectedView(View selectedView) {
        this.selectedView = selectedView;
    }
}
