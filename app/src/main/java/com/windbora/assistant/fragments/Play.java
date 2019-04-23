package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.windbora.assistant.R;
import com.windbora.assistant.RunVoiceRecognition;
import com.windbora.assistant.fragments.base.BaseFragment;

public class Play extends BaseFragment {

    private PlayViewModel mViewModel;
    private ImageView microphone;

    public static Play newInstance() {
        return new Play();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findElements(view);

        setListeners();
        // microphone = view.findViewById(R.id.microphone);
    }

    private void setListeners() {
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RunVoiceRecognition.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void findElements(View view) {
        microphone = view.findViewById(R.id.microphone);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlayViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public String getName() {
        return "Play";
    }
}
