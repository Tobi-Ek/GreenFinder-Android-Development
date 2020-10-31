package org.tensorflow.lite.examples.classification.Utilities;


import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Iterator;

public class Nutrients extends FragmentActivity {
    public String nut;


    public String nut1;
    public String nut2;
    public String nut3;
    public String nut4;
    public String nut5;
    public String nut6;
    public String nut7;
    public String nut8;
    public String nut9;
    public String nut10;

    public String nut_val;
    public String nut1_val;
    public String nut2_val;
    public String nut3_val;
    public String nut4_val;
    public String nut5_val;
    public String nut6_val;
    public String nut7_val;
    public String nut8_val;
    public String nut9_val;
    public String nut10_val;

    public String getNut() {
        return nut;
    }

    public void setNut(String nut) {
        this.nut = nut;
    }
    public String getNut1() {
        return nut1;
    }

    public void setNut1(String nut1) {
        this.nut1 = nut1;
    }

    public String getNut1_val() {
        return nut1_val;
    }

    public void setNut1_val(String nut_val1) {
        this.nut1_val = nut_val1;
    }


    public String getNut2() {
        return nut2;
    }

    public void setNut2(String nut2) {
        this.nut2 = nut2;
    }

    public String getNut3() {
        return nut3;
    }

    public void setNut3(String nut3) {
        this.nut3 = nut3;
    }

    public String getNut4() {
        return nut4;
    }

    public void setNut4(String nut4) {
        this.nut4 = nut4;
    }

    public String getNut5() {
        return nut5;
    }

    public void setNut5(String nut5) {
        this.nut5 = nut5;
    }

    public String getNut6() {
        return nut6;
    }

    public void setNut6(String nut6) {
        this.nut6 = nut6;
    }

    public String getNut7() {
        return nut7;
    }

    public void setNut7(String nut7) {
        this.nut7 = nut7;
    }

    public String getNut8() {
        return nut8;
    }

    public void setNut8(String nut8) {
        this.nut8 = nut8;
    }

    public String getNut9() {
        return nut9;
    }

    public void setNut9(String nut9) {
        this.nut9 = nut9;
    }
    public String getNut10() {
        return nut10;
    }

    public void setNut10(String nut10) {
        this.nut10 = nut10;
    }


    public String getNut_val() {
        return nut_val;
    }

    public void setNut_val(String nut_val) {
        this.nut_val = nut_val;
    }

    public String getNut2_val() {
        return nut2_val;
    }

    public void setNut2_val(String nut2_val) {
        this.nut2_val = nut2_val;
    }

    public String getNut3_val() {
        return nut3_val;
    }

    public void setNut3_val(String nut3_val) {
        this.nut3_val = nut3_val;
    }

    public String getNut4_val() {
        return nut4_val;
    }

    public void setNut4_val(String nut4_val) {
        this.nut4_val = nut4_val;
    }

    public String getNut5_val() {
        return nut5_val;
    }

    public void setNut5_val(String nut5_val) {
        this.nut5_val = nut5_val;
    }

    public String getNut6_val() {
        return nut6_val;
    }

    public void setNut6_val(String nut6_val) {
        this.nut6_val = nut6_val;
    }

    public String getNut7_val() {
        return nut7_val;
    }

    public void setNut7_val(String nut7_val) {
        this.nut7_val = nut7_val;
    }

    public String getNut8_val() {
        return nut8_val;
    }

    public void setNut8_val(String nut8_val) {
        this.nut8_val = nut8_val;
    }

    public String getNut9_val() {
        return nut9_val;
    }

    public void setNut9_val(String nut9_val) {
        this.nut9_val = nut9_val;
    }
    public String getNut10_val() {
        return nut10_val;
    }

    public void setNut10_val(String nut10_val) {
        this.nut10_val = nut10_val;
    }




    public static void Nutrient(){

    }



    public static Nutrients getNutrients(Dictionary topRecognitionDic, InputStream is){
    Nutrients nutrients=new Nutrients();
    //comment this line
    String itemName= (String) topRecognitionDic.get("item_name");
    Float itemConfidence= (Float) topRecognitionDic.get("item_confidence");

    String jsonStr=null;
    try {
        int size=is.available();
        byte[] buffer=new byte[size];
        is.read(buffer);
        is.close();
        jsonStr=new String(buffer,"UTF-8");
        JSONObject jsonObject =new JSONObject(jsonStr);
        JSONObject jsonGreens= (JSONObject) jsonObject.get("Greens");

        if(itemName!=null & itemConfidence >0.3){
            JSONObject jsonNut=(JSONObject) jsonGreens.get(itemName);
            Iterator iter=jsonNut.keys();
            int i=0;
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = jsonNut.getString(key);
                if (i == 0) {
                    nutrients.setNut(key);
                    nutrients.setNut_val(value);
                } else if (i == 1) {
                    nutrients.setNut1((key));
                    nutrients.setNut1_val((value));
                } else if (i == 2) {
                    nutrients.setNut2((key));
                    nutrients.setNut2_val((value));
                } else if (i == 3) {
                    nutrients.setNut3((key));
                    nutrients.setNut3_val((value));
                } else if (i == 4) {
                    nutrients.setNut4((key));
                    nutrients.setNut4_val((value));
                } else if (i == 5) {
                    nutrients.setNut5((key));
                    nutrients.setNut5_val((value));
                } else if (i == 6) {
                    nutrients.setNut6((key));
                    nutrients.setNut6_val((value));
                } else if (i == 7) {
                    nutrients.setNut7((key));
                    nutrients.setNut7_val((value));
                } else if (i == 8) {
                    nutrients.setNut8((key));
                    nutrients.setNut8_val((value));
                } else if (i == 9) {
                    nutrients.setNut9((key));
                    nutrients.setNut9_val((value));
                }
                else if(i==10){
                    nutrients.setNut10(key);
                    nutrients.setNut10(value);
                }
                i+=1;
            }
        }
    }catch (FileNotFoundException e){
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
        return nutrients;

    }

}
