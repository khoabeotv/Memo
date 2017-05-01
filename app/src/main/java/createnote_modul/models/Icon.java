package createnote_modul.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by l on 4/23/2017.
 */

public class Icon {

    public static final ArrayList<String> iconTypes;

    public static final String ICON_EXPRESSIONS = "expressions";
    public static final int ICON_EXPRESSIONS_NUMS = 35;

    public static final String ICON_LOVE = "love";
    public static final String ICON_PAPERS = "laper";


    public static HashMap<String,ArrayList<Integer>> hashMapIcon;
    public static HashMap<Integer,String> hashMapIconName;
    public static int nothingIcon;
    static {
        iconTypes = new ArrayList<>();
        iconTypes.add(ICON_EXPRESSIONS);
    }

    public static void load(Context context){
        nothingIcon = context.getResources().getIdentifier("Nothing","drawable",context.getPackageName());

        hashMapIcon = new HashMap<>();
        hashMapIconName = new HashMap<>();
        ArrayList<Integer> icons1 = new ArrayList<>();
        for (int i = 0;i <= ICON_EXPRESSIONS_NUMS;i++){
            int resId = context.getResources().getIdentifier(ICON_EXPRESSIONS+i
                    ,"drawable"
                    ,context.getPackageName());
            icons1.add(resId);
            hashMapIconName.put(resId,ICON_EXPRESSIONS+i);
        }
        hashMapIcon.put(ICON_EXPRESSIONS,icons1);
    }

}
