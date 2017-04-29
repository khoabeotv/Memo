package createnote_modul.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by l on 4/22/2017.
 */

public class Color {

    public static final ArrayList<Color> coolColors;
    public static final HashMap<String,Integer> hashMapCoolColors;
    public static final HashMap<String,Integer> hashMapWarmColors;
    public static final ArrayList<Color> warmColors;
    static{
        coolColors = new ArrayList<>();
        warmColors = new ArrayList<>();
        hashMapCoolColors = new HashMap<>();
        hashMapWarmColors = new HashMap<>();

        warmColors.add(new Color("#ff0505",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ff0505",0);
        warmColors.add(new Color("#ad0101",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ad0101",1);
        warmColors.add(new Color("#7a0000",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#7a0000",2);
        warmColors.add(new Color("#8e0b0b",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#8e0b0b",3);
        warmColors.add(new Color("#db3939",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#db3939",4);
        warmColors.add(new Color("#c14e2e",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#c14e2e",5);
        warmColors.add(new Color("#f94600",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#f94600",6);
        warmColors.add(new Color("#f95b1d",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#f95b1d",7);
        warmColors.add(new Color("#a53408",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#a53408",8);
        warmColors.add(new Color("#a5071c",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#a5071c",9);
        warmColors.add(new Color("#e20b53",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#e20b53",10);
        warmColors.add(new Color("#e20a96",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#e20a96",11);
        warmColors.add(new Color("#e504f9",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#e504f9",12);
        warmColors.add(new Color("#d003f9",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#d003f9",13);
        warmColors.add(new Color("#a720ea",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#a720ea",14);
        warmColors.add(new Color("#6b33cc",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#6b33cc",15);

        coolColors.add(new Color("#faff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#faff00",0);
        coolColors.add(new Color("#d0ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#d0ff00",1);
        coolColors.add(new Color("#376d3c",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#376d3c",2);
        coolColors.add(new Color("#76ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#76ff00",3);
        coolColors.add(new Color("#26ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#26ff00",4);
        coolColors.add(new Color("#00ff55",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#00ff55",5);
        coolColors.add(new Color("#079135",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#079135",6);
        coolColors.add(new Color("#02f9a3",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#02f9a3",7);
        coolColors.add(new Color("#01d8f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#01d8f9",8);
        coolColors.add(new Color("#67a7bc",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#67a7bc",9);
        coolColors.add(new Color("#01a2f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#01a2f9",10);
        coolColors.add(new Color("#0160f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#0160f9",11);
        coolColors.add(new Color("#0132f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#0132f9",12);
        coolColors.add(new Color("#6c01f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#6c01f9",13);
        coolColors.add(new Color("#6b7008",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#6b7008",14);
        coolColors.add(new Color("#077010",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#077010",15);
    }

    private String color;
    private int drawStyle;

    public Color(String color, int drawStyle) {
        this.color = color;
        this.drawStyle = drawStyle;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDrawStyle(int drawStyle) {
        this.drawStyle = drawStyle;
    }

    public String getColor() {
        return color;
    }

    public int getDrawStyle() {
        return drawStyle;
    }

    public static void clearChange(String s){
        if(hashMapCoolColors.get(s) != null){
            coolColors.get(hashMapCoolColors.get(s)).setDrawStyle(CusView.DRAW_CIRCLE);
        }else{
            warmColors.get(hashMapWarmColors.get(s)).setDrawStyle(CusView.DRAW_CIRCLE);
        }
    }
}
