package com.windbora.assistant.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Command;
import com.windbora.assistant.CommandActivity;
import com.windbora.assistant.R;

import java.util.List;

public class CommandsAdapter extends RecyclerView.Adapter<CommandsAdapter.ViewHolder> {
    private Button commandButton;
    private List<Command> results;

    public CommandsAdapter(List<Command> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public CommandsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.command_button, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommandsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.onBind(results.get(i));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            commandButton = itemView.findViewById(R.id.command_button);
        }

        public void onBind(final Command commandData){

            commandButton.setText(commandData.getName());
            commandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), CommandActivity.class);
                    intent.putExtra("name", commandData.getName());
                    intent.putExtra("description", commandData.getDescription());
                }
            });

        }
    }

}
