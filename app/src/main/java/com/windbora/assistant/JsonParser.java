package com.windbora.assistant;

import com.google.gson.Gson;
import com.windbora.assistant.fragments.base.CommandsList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {

    public static CommandsList getDetails(String path) throws IOException {

        File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }

        Gson gson = new Gson();
        CommandsList list = gson.fromJson(new FileReader(file), CommandsList.class);
        return list;
    }
}
