package com.example.shreyas.hackathon_quark18;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class PartnerRV extends RecyclerView.Adapter<PartnerRV.ViewHolder> {
    Context context;
    List<PartnerItemFormat> partnerItemFormats;

    public PartnerRV(Context context, List<PartnerItemFormat> partnerItemFormats) {
        this.context = context;
        this.partnerItemFormats = partnerItemFormats;
    }

    @Override
    public PartnerRV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.partner_item_format, parent, false);

        // Return a new holder instance
        return new PartnerRV.ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(PartnerRV.ViewHolder holder, int position) {
        holder.image.setImageURI(Uri.parse(partnerItemFormats.get(position).getImageurl()));
        holder.name.setText(partnerItemFormats.get(position).getName());
        OfferRV adapter = new OfferRV(partnerItemFormats.get(position).getOffers());
        holder.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return partnerItemFormats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView name;
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.partner_item_format_image);
            name = (TextView) itemView.findViewById(R.id.partner_item_format_text);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.partner_item_format_rv);
            recyclerView.setHasFixedSize(true);
        }
    }
}
