package com.experimental.rxinteraction.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.experimental.rxinteraction.R;

/**
 * A fragment containing the views for giving the user the ability to choose a specific class
 * to start a given arena draft
 */
public class ClassChoiceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_arena_class, container, false);
    }
}
