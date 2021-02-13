package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Diwash Choudhary on 3/23/2017.
 */
public class StaticDynamicAdapter extends
        RecyclerView.Adapter<StaticDynamicAdapter.MyViewHolder> {

    private List<StaticDynamicModel> list_item;
    public Context mcontext;


    public StaticDynamicAdapter(List<StaticDynamicModel> list, Context context) {
        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public StaticDynamicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.static_item, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final StaticDynamicModel dash_nrda = list_item.get(position);
        viewHolder.etp.setText(dash_nrda.getEta_no());

        viewHolder.vegregno.setText(dash_nrda.getVehregno());

        viewHolder.card_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MapTrackingActivity.class);
                intent.putExtra("etpNo", dash_nrda.getEta_no());
                mcontext.startActivity(intent);

            }
        });
        AndyUtils.removeCustomProgressDialog();
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

        public  TextView vegregno;

        public CardView card_header;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            etp = (TextView) itemLayoutView.findViewById(R.id.etp);

            vegregno= (TextView) itemLayoutView.findViewById(R.id.vegregno);

            card_header = ( CardView ) itemLayoutView.findViewById(R.id.card_header);
        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

}