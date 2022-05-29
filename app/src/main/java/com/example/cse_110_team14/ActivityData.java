package com.example.cse_110_team14;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;

public class ActivityData {

    public static void setActivity(Context context, String filename, String activity) {
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            String temp = "{\"activity\":\""+activity+"\"}";
            outputStream.write(temp.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getActivity(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        if(file.exists()) {
            Log.d("ActivityData", "file exists");
            try {
                Reader reader = new FileReader(file);
                JsonObject parser = JsonParser.parseReader(reader).getAsJsonObject();
                return parser.get("activity").getAsString();
            } catch (Exception e) {
                Log.d("ActivityData", "file broken");
                e.printStackTrace();
            }
        }
        else {
            Log.d("ActivityData", "file DNE");
        }

        return "";
    }


}
