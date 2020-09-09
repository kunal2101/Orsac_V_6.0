package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Diwash Choudhary on 3/23/2017.
 */
public class SimpleAdapter_Sec extends
        RecyclerView.Adapter<SimpleAdapter_Sec.MyViewHolder> {

    private List<Admin_sec_bean> list_item;
    public Context mcontext;


    public SimpleAdapter_Sec(List<Admin_sec_bean> list, Context context) {
        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public SimpleAdapter_Sec.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.orsac_circle_sec_new, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final Admin_sec_bean dash_nrda = list_item.get(position);
        viewHolder.etp.setText(dash_nrda.getEta_no());
        viewHolder.circle.setText(dash_nrda.getCircle());
        if(position%2 == 0){
            viewHolder.card_header.setBackgroundColor(Color.parseColor("#eceff1"));
        }else {
            viewHolder.card_header.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        viewHolder.card_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MapTrackingActivity.class);
                intent.putExtra("etpNo",dash_nrda.getEta_no());
                mcontext.startActivity(intent);

            }
        });

/*
        viewHolder.mores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, DetailActivty.class);
                mcontext.startActivity(intent);

            */
/*    Toast.makeText(mcontext, list_item.get(position),
                        Toast.LENGTH_LONG).show();*//*

            }
        });
*/

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView etp;
        public TextView circle;
        public TextView mores;
        public CardView card_header;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            etp = (TextView) itemLayoutView.findViewById(R.id.etp);
            circle = (TextView) itemLayoutView.findViewById(R.id.circle);
            mores = (TextView) itemLayoutView.findViewById(R.id.mores);
            card_header = (CardView) itemLayoutView.findViewById(R.id.card_header);
        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

}