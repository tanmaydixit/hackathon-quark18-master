package com.example.shreyas.hackathon_quark18;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class OfferRV extends RecyclerView.Adapter<OfferRV.ViewHolder> {
    private List<OfferItemFormat> offerItemFormats;

    OfferRV(List<OfferItemFormat> offerItemFormats) {
        this.offerItemFormats = offerItemFormats;
    }

    @Override
    public OfferRV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item_format, parent, false));
    }

    @Override
    public void onBindViewHolder(OfferRV.ViewHolder holder, int position) {
        if (holder != null) {
            holder.desc.setText(offerItemFormats.get(position).getDesc());
            holder.code.setText(offerItemFormats.get(position).getCode());
        }
    }

    @Override
    public int getItemCount() {
        if (offerItemFormats != null) {
            return offerItemFormats.size();
        } else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView code;
        TextView desc;

        ViewHolder(View itemView) {
            super(itemView);
            code = (TextView) itemView.findViewById(R.id.offer_code);
            desc = (TextView) itemView.findViewById(R.id.offer_desc);
        }
    }
}
