package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.content.Context;
import android.content.Intent;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by anupamchugh on 26/12/15.
 */
public class CustomPagerAdapter extends PagerAdapter {
    String getTemp;
    String value_inst[];
    int getindex;
    private Context mContext;
    ArrayList<HashMap<String,String>> mData ;
    private String val;

    public CustomPagerAdapter(Context context, ArrayList<HashMap<String, String>> datalist) {
        mContext = context;
        mData = datalist;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.demoview_white, collection, false);
        collection.addView(layout);
        randomColor();
        LinearLayout layout_one   = (LinearLayout)layout.findViewById(R.id.layout_one);
        LinearLayout layout_two   = (LinearLayout)layout.findViewById(R.id.layout_two);
        LinearLayout layout_three = (LinearLayout)layout.findViewById(R.id.layout_three);
        LinearLayout layout_four  = (LinearLayout)layout.findViewById(R.id.layout_four);

        TextView text_one   = (TextView)layout.findViewById(R.id.text_one);
        TextView text_two   = (TextView)layout.findViewById(R.id.text_two);
        TextView text_three = (TextView)layout.findViewById(R.id.text_three);
        TextView text_four  = (TextView)layout.findViewById(R.id.text_four);

        final TextView company1   = (TextView)layout.findViewById(R.id.company1);
        final TextView company2   = (TextView)layout.findViewById(R.id.company2);
        final TextView company3   = (TextView)layout.findViewById(R.id.company3);
        final TextView company4   = (TextView)layout.findViewById(R.id.company4);

        layout_one.setVisibility(View.VISIBLE);
        layout_two.setVisibility(View.VISIBLE);
        layout_three.setVisibility(View.VISIBLE);
        layout_four.setVisibility(View.VISIBLE);

        layout_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,company1.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), Orsac_circle_detail.class);
                i.putExtra("circle",company1.getText().toString());
                mContext.startActivity(i);

            }
        });

        layout_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,company2.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), Orsac_circle_detail.class);
                i.putExtra("circle",company2.getText().toString());
                mContext.startActivity(i);
            }
        });

        layout_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,company3.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), Orsac_circle_detail.class);
                i.putExtra("circle",company3.getText().toString());
                mContext.startActivity(i);
            }
        });

        layout_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,company4.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(), Orsac_circle_detail.class);
                i.putExtra("circle",company4.getText().toString());
                mContext.startActivity(i);
            }
        });

        if(position == 0){
            HashMap<String,String> map0 = mData.get(0);
            HashMap<String,String> map1 = mData.get(1);
            HashMap<String,String> map2 = mData.get(2);
            HashMap<String,String> map3 = mData.get(3);

            company1.setText(map0.get("title"));
            company2.setText(map1.get("title"));
            company3.setText(map2.get("title"));
            company4.setText(map3.get("title"));

            text_one.setText(map0.get("value"));
            text_two.setText(map1.get("value"));
            text_three.setText(map2.get("value"));
            text_four.setText(map3.get("value"));


        }else if(position == 1){
            HashMap<String,String> map4 = mData.get(4);
            HashMap<String,String> map5 = mData.get(5);
            HashMap<String,String> map6 = mData.get(6);
            HashMap<String,String> map7 = mData.get(7);

            company1.setText(map4.get("title"));
            company2.setText(map5.get("title"));
            company3.setText(map6.get("title"));
            company4.setText(map7.get("title"));

            text_one.setText(map4.get("value"));
            text_two.setText(map5.get("value"));
            text_three.setText(map6.get("value"));
            text_four.setText(map7.get("value"));

        }else if(position == 2){
            HashMap<String,String> map8 = mData.get(8);
            HashMap<String,String> map9 = mData.get(9);
            HashMap<String,String> map10 = mData.get(10);
            HashMap<String,String> map11 = mData.get(11);

            company1.setText(map8.get("title"));
            company2.setText(map9.get("title"));
            company3.setText(map10.get("title"));
            company4.setText(map11.get("title"));

            text_one.setText(map8.get("value"));
            text_two.setText(map9.get("value"));
            text_three.setText(map10.get("value"));
            text_four.setText(map11.get("value"));

        }else if(position == 3){
            HashMap<String,String> map12 = mData.get(12);
            HashMap<String,String> map13 = mData.get(13);
            HashMap<String,String> map14 = mData.get(14);
            layout_four.setVisibility(View.INVISIBLE);

            company1.setText(map12.get("title"));
            company2.setText(map13.get("title"));
            company3.setText(map14.get("title"));

            text_one.setText(map12.get("value"));
            text_two.setText(map13.get("value"));
            text_three.setText(map14.get("value"));

        }

        return layout;
    }
    public static int randomColor(){
        float[] TEMP_HSL = new float[]{0, 0, 0};
        float[] hsl = TEMP_HSL;
        hsl[0] = (float) (Math.random() * 360);
        hsl[1] = (float) (40 + (Math.random() * 60));
        hsl[2] = (float) (40 + (Math.random() * 60));
        return ColorUtils.HSLToColor(hsl);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 4;//mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

   /* @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];

        return mContext.getString(customPagerEnum.getTitleResId());
    }*/

}
