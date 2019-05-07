package com.windbora.assistant;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.windbora.assistant.base.CommandsList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonParser {

    public static CommandsList getDetails(Context context) {

        String fileName = "CommandsList.json";

        /*File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }

        Gson gson = new Gson();
        CommandsList list = gson.fromJson(new FileReader(file), CommandsList.class);

        Toast.makeText(context, "FIND ME ME " + list.toString(), Toast.LENGTH_LONG).show();*/

        CommandsList result = null;

        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = gson.fromJson(stringBuilder.toString(), CommandsList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
