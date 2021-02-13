package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.DetailActivty;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Neha Malik.
 */
public class CircleActivity_Click extends AppCompatActivity {
    ListView dataListview;
    ArrayList<HashMap<String, String>> listData;
    private List<String> list = new ArrayList<String>();
    public LivestatusAdapter mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companydetails_circleactivity);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });

        dataListview = (ListView)findViewById(R.id.listview);
        dataListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CircleActivity_Click.this, DetailActivty.class);
                startActivity(intent);
            }
        });

        String[] stopped = {"L31702921/367","L31702921/302","L31702921/302","L31702921/302","L31702921/302","L31702921/302"};
        String[] Arrive  = {"JODA","JODA","JODA","JODA","JODA","JODA"};

        listData = new ArrayList<>();
        for(int ind=0; ind < stopped.length; ind++){
            HashMap<String,String> map = new HashMap<>();
            map.put("L31346-644",stopped[ind]);
            map.put("Joda",Arrive[ind]);
            listData.add(map);
        }

        CircleAdapter circlebase_adapter = new CircleAdapter(CircleActivity_Click.this, listData, R.layout.recyclarview_list);
        dataListview.setAdapter(circlebase_adapter);
    }
    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    private class CircleAdapter extends BaseAdapter {
        private Activity activity;
        private Context mcontext ;
        private ArrayList<HashMap<String, String>> data;
        private  LayoutInflater inflater=null;

        private int m_nlayoutID = 0;

        public CircleAdapter(Context a, ArrayList<HashMap<String, String>> d, int listRow) {
            mcontext = a ;
            activity = (Activity)a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //imageLoader=new ImageLoader(activity.getApplicationContext());

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

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(m_nlayoutID, null);

            TextView stopesTxt 	    = (TextView)vi.findViewById(R.id.etp);
            TextView arrivingTxt 	= (TextView)vi.findViewById(R.id.circle);

            HashMap<String,String> map = data.get(position);

            stopesTxt.setText(map.get("L31346-644"));
            arrivingTxt.setText(map.get("Joda"));

            return vi;
        }
    }

}
