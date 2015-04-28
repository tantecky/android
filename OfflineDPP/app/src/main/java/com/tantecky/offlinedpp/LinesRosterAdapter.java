package com.tantecky.offlinedpp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tantecky.offlinedpp.model.Line;
import com.tantecky.offlinedpp.model.LinesRoster;

public class LinesRosterAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private LinesRoster mRoster;

    @Override
    public int getCount() {
        return mRoster.size();
    }

    @Override
    public Object getItem(int position) {
        return mRoster.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.listitem_line, null);

        Line line = mRoster.get(position);
        TextView lineNumber = (TextView) view.findViewById(R.id.line_number);
        lineNumber.setText(Integer.toString(line.getNumber()));

        return view;
    }

    public LinesRosterAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mRoster = LinesRoster.getInstance();

    }
}
