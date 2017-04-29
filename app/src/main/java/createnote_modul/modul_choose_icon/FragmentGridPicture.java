package createnote_modul.modul_choose_icon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import createnote_modul.models.Icon;
import teambandau.memo.R;

/**
 * Created by l on 4/23/2017.
 */

public class FragmentGridPicture extends Fragment {

    private GridView gvPictures;
    private ImageButton btnExit;
    private CusPictureIconAdapterForGidView cusPictureIconAdapterForGidView;
    private FragmentGridPictureListener fragmentGridPictureListener;
    private String chooserItem;

    public static interface FragmentGridPictureListener{
        public void sendDataFromFragmentGridPicture(int id);
        public void btnExitClickListener();
    }

    public void onAttachToTheParentFragment(Fragment fragment){
        fragmentGridPictureListener = (FragmentGridPictureListener) fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_picture, container, false);
        gvPictures = (GridView) view.findViewById(R.id.gvChoosenPicture);
        btnExit = (ImageButton) view.findViewById(R.id.btnExit);

        Bundle b = getArguments();
        chooserItem = b.getString("itemChooser");
        cusPictureIconAdapterForGidView = new CusPictureIconAdapterForGidView(getActivity(),R.layout.item_grid_picture, Icon.hashMapIcon.get(chooserItem));
        gvPictures.setAdapter(cusPictureIconAdapterForGidView);

        addEvents();
        return view;
    }

    private void addEvents() {
        gvPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragmentGridPictureListener.sendDataFromFragmentGridPicture(Icon.hashMapIcon.get(chooserItem).get(i));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentGridPictureListener.btnExitClickListener();
            }
        });
    }
}
