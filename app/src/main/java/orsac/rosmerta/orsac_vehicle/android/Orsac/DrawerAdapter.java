package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Rubal on 28-07-2015.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> menuName;
    private final List<Integer> imageId;

    public DrawerAdapter(Activity context, List<String> menuName, List<Integer> imageId) {
        super(context, R.layout.menu_list_item, menuName);
        this.context = context;
        this.menuName = menuName;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.menu_list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.menu_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image_icon);
        LinearLayout line_main=(LinearLayout)rowView.findViewById(R.id.line_main);
        if (position % 2 == 1) {
            line_main.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
                        line_main.setBackgroundColor(Color.parseColor("#d6d6c2"));
        }

        txtTitle.setText(menuName.get(position));
        imageView.setImageResource(imageId.get(position));
        return rowView;
    }
}
