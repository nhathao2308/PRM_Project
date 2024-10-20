package com.example.workshop.View.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workshop.Model.Workshop;
import com.example.workshop.R;

import java.util.List;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {

    private Context context;
    private List<Workshop> workshopList;

    public WorkshopAdapter(Context context, List<Workshop> workshopList) {
        this.context = context;
        this.workshopList = workshopList;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workshop_card, parent, false);
        return new WorkshopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, int position) {
        Workshop workshop = workshopList.get(position);
        Glide.with(context)
                .load(workshop.getImageUrl()) // Load the image URL from the Workshop object
                .into(holder.workshopImage);
        holder.workshopName.setText(workshop.getName() != null ? workshop.getName() : "N/A");
        holder.workshopLocation.setText(workshop.getLocation() != null ? workshop.getLocation() : "N/A");

        if (workshop.getStartTime() != null) {
            holder.workshopStartTime.setText(workshop.getStartTime().toString());
        } else {
            holder.workshopStartTime.setText("Start time not available");
        }

        holder.workshopPrice.setText(String.valueOf(workshop.getPrice()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WorkshopDetailActivity.class);
            // Pass data to WorkshopDetailActivity
            intent.putExtra("workshop_id", workshop.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    public static class WorkshopViewHolder extends RecyclerView.ViewHolder {
        ImageView workshopImage;
        TextView workshopName, workshopLocation, workshopStartTime, workshopPrice;

        public WorkshopViewHolder(@NonNull View itemView) {
            super(itemView);
            workshopImage = itemView.findViewById(R.id.workshop_image);
            workshopName = itemView.findViewById(R.id.workshop_name);
            workshopLocation = itemView.findViewById(R.id.workshop_location);
            workshopStartTime = itemView.findViewById(R.id.workshop_start_time);
            workshopPrice = itemView.findViewById(R.id.workshop_price);
        }

    }
}
