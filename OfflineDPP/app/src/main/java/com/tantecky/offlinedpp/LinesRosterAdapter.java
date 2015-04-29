package com.tantecky.offlinedpp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tantecky.offlinedpp.model.Line;
import com.tantecky.offlinedpp.model.LinesRoster;

public class LinesRosterAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private LinesRoster mRoster;
    private int[] mIconsIds = new int[3];

    public LinesRosterAdapter(Context context, Resources resources) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mRoster = LinesRoster.getInstance();

        // icons loader from drawable
        loadIcon(context, resources, Line.Type.BUS, "bus_w");
        loadIcon(context, resources, Line.Type.TRAM, "tram_w");
        loadIcon(context, resources, Line.Type.METRO, "metro_w");
    }

    private void loadIcon(Context context,
                          Resources resources, Line.Type type, String iconFilename) {
        int id = resources.getIdentifier(iconFilename, "drawable", context.getPackageName());

        if (id == 0) {
            Utils.logWTF(String.format("Drawable resource %s not found", iconFilename));
        }

        mIconsIds[type.getValue()] = id;
    }

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
        // TODO see View holder
        View view = mInflater.inflate(R.layout.listitem_line, null);

        Line line = mRoster.get(position);
        TextView lineNumber = (TextView) view.findViewById(R.id.line_number);
        lineNumber.setText(line.getNumberAsString());

        ImageView icon = (ImageView) view.findViewById(R.id.line_icon);
        icon.setImageResource(mIconsIds[line.getType().getValue()]);

        return view;
    }
}
