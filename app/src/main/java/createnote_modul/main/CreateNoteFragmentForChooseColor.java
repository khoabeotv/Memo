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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import createnote_modul.models.Color;
import createnote_modul.models.CusView;
import createnote_modul.modul_choose_color.CusGribAdapter;
import teambandau.memo.R;


/**
 * Created by l on 4/25/2017.
 */

public class CreateNoteFragmentForChooseColor extends Fragment implements FragmentLifecycle{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private CusView cusView;
    private String colorWasChosen;
    private ToggleButton tbChangeColorType;

    private CusGribAdapter cusGribAdapterForCoolColor;
    private CusGribAdapter cusGribAdapterForWarmColor;

    private CusGribAdapter cusGribAdapterNow;
    private ArrayList<Color> listColorNow;
    private HashMap<String,Integer> hashMapColorNow;
    private GridView gbColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //View chi chay vao dung 1 lan luc vao view pager
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choosecolor_fragment,container,false);
        tbChangeColorType = (ToggleButton) v.findViewById(R.id.tbChangeColorType);
        cusView = (CusView) v.findViewById(R.id.cvchossenColor);

        gbColor = (GridView) v.findViewById(R.id.gvChoosenColor);

        getColorAndSetCusviewAndGridView();

        gbColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Color.clearChange(colorWasChosen);

                listColorNow.get(i).setDrawStyle(CusView.DRAW_CIRCLE_VS_STROKE);
                colorWasChosen = listColorNow.get(i).getColor();
                cusView.setColor(colorWasChosen);

                cusView.invalidate();
                cusGribAdapterNow.notifyDataSetChanged();
            }
        });

        tbChangeColorType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    listColorNow = Color.warmColors;
                    hashMapColorNow = Color.hashMapWarmColors;
                    cusGribAdapterNow = cusGribAdapterForWarmColor;
                    gbColor.setAdapter(cusGribAdapterNow);
                }else{
                    //cool
                    listColorNow = Color.coolColors;
                    hashMapColorNow = Color.hashMapCoolColors;
                    cusGribAdapterNow = cusGribAdapterForCoolColor;
                    gbColor.setAdapter(cusGribAdapterNow);
                }
            }
        });

        return v;
    }

    @Override
    public void onResumeFragment() {
        colorWasChosen = sharedPreferences.getString(CreateNoteActivity.NOTE_COLOR_KEY,CreateNoteActivity.DEFAULT_COLOR);
        getColorAndSetCusviewAndGridView();
    }

    @Override
    public void onPauseFragment() {
        editor.putString(CreateNoteActivity.NOTE_COLOR_KEY,colorWasChosen);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void getColorAndSetCusviewAndGridView(){
        colorWasChosen = sharedPreferences.getString(CreateNoteActivity.NOTE_COLOR_KEY,CreateNoteActivity.DEFAULT_COLOR);

        if(Color.hashMapCoolColors.containsKey(colorWasChosen)){
            tbChangeColorType.setChecked(false);
            hashMapColorNow = Color.hashMapCoolColors;
            listColorNow = Color.coolColors;
            Color.coolColors.get(Color.hashMapCoolColors.get(colorWasChosen)).setDrawStyle(CusView.DRAW_CIRCLE_VS_STROKE);
            cusView.setColor(colorWasChosen);
            cusGribAdapterForWarmColor = new CusGribAdapter(getActivity(),R.layout.item_color_chossen,Color.warmColors);
            cusGribAdapterForCoolColor = new CusGribAdapter(getActivity(),R.layout.item_color_chossen,Color.coolColors);
            cusGribAdapterNow = cusGribAdapterForCoolColor;
        }else{
            tbChangeColorType.setChecked(true);
            hashMapColorNow = Color.hashMapWarmColors;
            listColorNow = Color.warmColors;
            Color.warmColors.get(Color.hashMapWarmColors.get(colorWasChosen)).setDrawStyle(CusView.DRAW_CIRCLE_VS_STROKE);
            cusView.setColor(colorWasChosen);
            cusGribAdapterForWarmColor = new CusGribAdapter(getActivity(),R.layout.item_color_chossen,Color.warmColors);
            cusGribAdapterForCoolColor = new CusGribAdapter(getActivity(),R.layout.item_color_chossen,Color.coolColors);
            cusGribAdapterNow = cusGribAdapterForWarmColor;
        }

        gbColor.setAdapter(cusGribAdapterNow);
    }

    @Override
    public String toString() {
        return "CreateNoteFragmentForChooseColor{}";
    }
}
