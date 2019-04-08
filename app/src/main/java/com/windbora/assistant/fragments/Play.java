package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.windbora.assistant.R;
import com.windbora.assistant.fragments.base.BaseFragment;

public class Play extends BaseFragment {

    private PlayViewModel mViewModel;

    public static Play newInstance() {
        return new Play();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_fragment, container, false);
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
