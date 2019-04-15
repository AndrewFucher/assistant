package com.windbora.assistant.fragments.base;

import com.example.Command;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommandsList {

    @SerializedName("commands")
    @Expose
    private List<Command> commands = null;

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
