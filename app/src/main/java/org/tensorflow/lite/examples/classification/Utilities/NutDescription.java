package org.tensorflow.lite.examples.classification.Utilities;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Iterator;

public class NutDescription  extends FragmentActivity {
    String nut;
    String Desc;
    public String getNut() {
        return nut;
    }

    public void setNut(String nut) {
        this.nut = nut;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

   public static NutDescription getNutDesc(Dictionary itemRecognition, InputStream is){
        NutDescription nutDescription=new NutDescription();
        String item= (String) itemRecognition.get("item_name");
        Float itemConfidence= (Float) itemRecognition.get("item_confidence");
        String jsonStr="";
        try{
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            jsonStr=new String(buffer,"UTF-8");
            JSONObject jsonObject=new JSONObject(jsonStr);
            JSONObject jsonDes= (JSONObject) jsonObject.get("Description");
            if(item!=null & itemConfidence>0.5){
                nutDescription.setDesc(jsonDes.getString(item));
                nutDescription.setNut(item);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

       return nutDescription;
   }
}
