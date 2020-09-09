package orsac.rosmerta.orsac_vehicle.android.Orsac;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Diwash Choudhary on 04-04-2017.
 */


public class SpinnerAdaptor extends BaseAdapter{

    private Activity mactivity;
    private Context mcontext;
    private List<CircelBean> data;
    private LayoutInflater inflater = null;
    private int m_nlayoutID = 0;

    public SpinnerAdaptor(Context context, List<CircelBean> d, int listRow) {
        mcontext = context;
        mactivity = (Activity) context;
        data = d;
        inflater = (LayoutInflater) mactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_nlayoutID = listRow;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(m_nlayoutID, null);

      /*  TextView sr_source = (TextView) vi.findViewById(R.id.sr_source);

        HashMap<String, String> fragValue = data.get(position);*/

        //sr_source.setText(fragValue.get("KEY_NAME"));

     /*if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor(Const.row_one_color));
        } else {
            vi.setBackgroundColor(Color.parseColor(Const.row_two_color));
        }*/
        CircelBean adminModel = data.get(position);

        TextView txtValue = (TextView) vi.findViewById(R.id.txtValue);
        TextView txtLabel = (TextView) vi.findViewById(R.id.txtLabel);
        ImageView img = (ImageView)vi.findViewById(R.id.img);

if(position==0){
    txtLabel.setBackgroundColor(Color.parseColor("#d1395c"));
    txtValue.setTextColor(Color.parseColor("#d1395c"));
    img.setBackgroundResource(R.drawable.com_pink);

}else if(position == 3) {
    txtLabel.setBackgroundColor(Color.parseColor("#0d5bd8"));
    img.setBackgroundResource(R.drawable.com_blue);

    txtValue.setTextColor(Color.parseColor("#0d5bd8"));
} else  if(position ==1){
    txtLabel.setBackgroundColor(Color.parseColor("#F68159"));
    txtValue.setTextColor(Color.parseColor("#F68159"));
    img.setBackgroundResource(R.drawable.com_org);


}
        txtLabel.setText(adminModel.getTitle());
        txtValue.setText(adminModel.getNum());

        return vi;
    }


}