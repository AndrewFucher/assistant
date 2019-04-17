package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.windbora.assistant.JsonParser;
import com.windbora.assistant.R;
import com.windbora.assistant.fragments.adapters.CommandsAdapter;
import com.windbora.assistant.fragments.base.BaseFragment;

public class Commands extends BaseFragment {

    private CommandsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CommandsAdapter adapter;

    public static Commands newInstance() {
        return new Commands();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.commands_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findElements(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        try {
            adapter = new CommandsAdapter(JsonParser.getDetails(getContext()).getCommands());
            Toast.makeText(view.getContext(), "FIND ME ME ME" + JsonParser.getDetails(getContext()).getCommands().toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void findElements(View view) {
        recyclerView = view.findViewById(R.id.commandRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CommandsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public String getName() {
        return "Commands";
    }
}
