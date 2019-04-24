package com.windbora.assistant.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Command;
import com.windbora.assistant.CommandActivity;
import com.windbora.assistant.R;

import java.util.List;

public class CommandsAdapter extends RecyclerView.Adapter<CommandsAdapter.ViewHolder> {

    private List<Command> results;
    private Context context;

    public CommandsAdapter(List<Command> results, Context context) {
        this.results = results;
        this.context = context;
        notifyDataSetChanged();
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

        private Button commandButton;
        private ImageView imageView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            commandButton = itemView.findViewById(R.id.linkButtonDescription);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void onBind(final Command commandData){

            if (commandData.getAble().equals("enable")) {

                Context myContext = imageView.getContext();

                int imageID = myContext.getResources().getIdentifier(commandData.getImageName(), "drawable", myContext.getPackageName());

                imageView.setImageResource(imageID);

                String indent = new String(new char[commandData.getNameBut().length() / 2 ]).replace("\0", " ");
                String title = indent + commandData.getNameBut();

                commandButton.setText(title);
                commandButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(itemView.getContext(), CommandActivity.class);
                        intent.putExtra("name", commandData.getName());
                        intent.putExtra("description", commandData.getDescription());
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                commandButton.setClickable(false);
                commandButton.setVisibility(View.INVISIBLE);
            }
        }
    }

}
