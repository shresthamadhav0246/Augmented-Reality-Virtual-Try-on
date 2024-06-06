package com.bawp.virtualtryonforecommerce.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import java.util.List;

public class SunglassesAdapter extends RecyclerView.Adapter<SunglassesAdapter.SunglassesViewHolder> {

    private Context context;
    private List<String> sunglassesList;
    private SunglassesClickListener listener;

    public SunglassesAdapter(Context context, List<String> sunglassesList, SunglassesClickListener listener) {
        this.context = context;
        this.sunglassesList = sunglassesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SunglassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sunglasses, parent, false);
        return new SunglassesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SunglassesViewHolder holder, int position) {
        String sunglassesImageUrl = sunglassesList.get(position);
        Glide.with(context)
                .load("file:///android_asset/" + sunglassesImageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.sunglassesImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSunglassesClick(sunglassesImageUrl);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sunglassesList.size();
    }

    public interface SunglassesClickListener {
        void onSunglassesClick(String sunglassesImageUrl);
    }

    public class SunglassesViewHolder extends RecyclerView.ViewHolder {
        ImageView sunglassesImageView;

        public SunglassesViewHolder(@NonNull View itemView) {
            super(itemView);
            sunglassesImageView = itemView.findViewById(R.id.imageViewSunglasses);
        }
    }
}
