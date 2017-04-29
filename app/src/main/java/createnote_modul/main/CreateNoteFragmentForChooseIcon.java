package createnote_modul.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import createnote_modul.models.CusView;
import createnote_modul.models.Icon;
import createnote_modul.modul_choose_icon.FragmentGridPicture;
import createnote_modul.modul_choose_icon.FragmentListPicture;
import teambandau.memo.R;

/**
 * Created by l on 4/25/2017.
 */

public class CreateNoteFragmentForChooseIcon extends Fragment implements FragmentListPicture.FragmentListPictureListener,FragmentGridPicture.FragmentGridPictureListener, FragmentLifecycle{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private android.support.v4.app.FragmentManager fm;
    private ImageView ivChosserPicture;
    private CusView cusView;
    private int idPictureWasChoose;
    private String colorWasChoosen;
    private FragmentGridPicture fragmentGridPicture;
    private FragmentListPicture fragmentListPicture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        idPictureWasChoose = sharedPreferences.getInt(CreateNoteActivity.NOTE_ICON_KEY,CreateNoteActivity.DEFAULT_ICON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chooseicon_fragment,container,false);

        Icon.load(getActivity());
        ivChosserPicture = (ImageView) v.findViewById(R.id.ivLastChooserPicture);
        cusView = (CusView) v.findViewById(R.id.cvChossenColorIconSide);
        getIconAndSetIvChosserPicture();

        fragmentListPicture = new FragmentListPicture();
        fragmentGridPicture = new FragmentGridPicture();

        fragmentListPicture.onAttachToTheParentFragment(this);
        fragmentGridPicture.onAttachToTheParentFragment(this);

        fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.lnPictureChooserForFragment,fragmentListPicture);
        ft.commit();

        return  v;
    }
    @Override
    public void sendDataFromFragmentListPicture(String itemChooser) {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

        Bundle b = new Bundle();
        b.putString("itemChooser",itemChooser);
        fragmentGridPicture.setArguments(b);

        ft.replace(R.id.lnPictureChooserForFragment,fragmentGridPicture);
        ft.commit();
    }

    @Override
    public void btnDeleteListener() {
        ivChosserPicture.setImageResource(Icon.nothingIcon);
        idPictureWasChoose = Icon.nothingIcon;
    }

    @Override
    public void sendDataFromFragmentGridPicture(int id) {
        ivChosserPicture.setImageResource(id);
        idPictureWasChoose = id;
    }

    @Override
    public void btnExitClickListener() {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.lnPictureChooserForFragment,fragmentListPicture);
        ft.commit();
    }

    @Override
    public void onResumeFragment() {
        getIconAndSetIvChosserPicture();
    }

    @Override
    public void onPauseFragment() {
        editor.putInt(CreateNoteActivity.NOTE_ICON_KEY,idPictureWasChoose);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void getIconAndSetIvChosserPicture(){
        colorWasChoosen = sharedPreferences.getString(CreateNoteActivity.NOTE_COLOR_KEY,CreateNoteActivity.DEFAULT_COLOR);
        idPictureWasChoose = sharedPreferences.getInt(CreateNoteActivity.NOTE_ICON_KEY,CreateNoteActivity.DEFAULT_ICON);
        ivChosserPicture.setImageResource(idPictureWasChoose);
        cusView.setColor(colorWasChoosen);
        cusView.invalidate();
    }

    @Override
    public String toString() {
        return "CreateNoteFragmentForChooseIcon{}";
    }
}
