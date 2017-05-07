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

        warmColors.add(new Color("#ffff0505",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffff0505",0);
        warmColors.add(new Color("#ffad0101",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffad0101",1);
        warmColors.add(new Color("#ff7a0000",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ff7a0000",2);
        warmColors.add(new Color("#ff8e0b0b",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ff8e0b0b",3);
        warmColors.add(new Color("#ffdb3939",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffdb3939",4);
        warmColors.add(new Color("#ffc14e2e",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffc14e2e",5);
        warmColors.add(new Color("#fff94600",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#fff94600",6);
        warmColors.add(new Color("#fff95b1d",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#fff95b1d",7);
        warmColors.add(new Color("#ffa53408",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffa53408",8);
        warmColors.add(new Color("#ffa5071c",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffa5071c",9);
        warmColors.add(new Color("#ffe20b53",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffe20b53",10);
        warmColors.add(new Color("#ffe20a96",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffe20a96",11);
        warmColors.add(new Color("#ffe504f9",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffe504f9",12);
        warmColors.add(new Color("#ffd003f9",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffd003f9",13);
        warmColors.add(new Color("#ffa720ea",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ffa720ea",14);
        warmColors.add(new Color("#ff6b33cc",CusView.DRAW_CIRCLE));
        hashMapWarmColors.put("#ff6b33cc",15);

        coolColors.add(new Color("#fffaff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#fffaff00",0);
        coolColors.add(new Color("#ffd0ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ffd0ff00",1);
        coolColors.add(new Color("#ff376d3c",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff376d3c",2);
        coolColors.add(new Color("#ff76ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff76ff00",3);
        coolColors.add(new Color("#ff26ff00",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff26ff00",4);
        coolColors.add(new Color("#ff00ff55",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff00ff55",5);
        coolColors.add(new Color("#ff079135",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff079135",6);
        coolColors.add(new Color("#ff02f9a3",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff02f9a3",7);
        coolColors.add(new Color("#ff01d8f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff01d8f9",8);
        coolColors.add(new Color("#ff67a7bc",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff67a7bc",9);
        coolColors.add(new Color("#ff01a2f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff01a2f9",10);
        coolColors.add(new Color("#ff0160f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff0160f9",11);
        coolColors.add(new Color("#ff0132f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff0132f9",12);
        coolColors.add(new Color("#ff6c01f9",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff6c01f9",13);
        coolColors.add(new Color("#ff6b7008",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff6b7008",14);
        coolColors.add(new Color("#ff077010",CusView.DRAW_CIRCLE));
        hashMapCoolColors.put("#ff077010",15);
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
