package com.tantecky.offlinedpp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tantecky.offlinedpp.model.LinesRoster;

public class LinesFragment extends ListFragment {
    public LinesFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinesRoster.getInstance().load();
        setListAdapter(new LinesRosterAdapter(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lines, container, false);
    }
}
