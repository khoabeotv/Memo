package createnote_modul.modul_choose_icon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import createnote_modul.models.Icon;
import teambandau.memo.R;

/**
 * Created by l on 4/23/2017.
 */

public class FragmentListPicture extends Fragment {
    private  ListView lv;
    private ImageButton imgbDelete;
    private CusPictureIconAdapterForListView cusPictureIconAdapterForListView;
    private FragmentListPictureListener fragmentListPictureListener;

    public static interface FragmentListPictureListener{
        public void sendDataFromFragmentListPicture(String itemChooser);
        public void btnDeleteListener();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttachToTheParentFragment(Fragment fragment){
        fragmentListPictureListener = (FragmentListPictureListener) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_picture,container,false);

        lv = (ListView) view.findViewById(R.id.lvfragment_list_picture);
        imgbDelete = (ImageButton) view.findViewById(R.id.btnDeletePicture);
        cusPictureIconAdapterForListView = new CusPictureIconAdapterForListView(getActivity(), R.layout.item_list_picture, Icon.iconTypes);
        lv.setAdapter(cusPictureIconAdapterForListView);

        addEvents();

        return view;
    }

    private void addEvents() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragmentListPictureListener.sendDataFromFragmentListPicture(Icon.iconTypes.get(i));
            }
        });
        imgbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentListPictureListener.btnDeleteListener();
            }
        });
    }


}
