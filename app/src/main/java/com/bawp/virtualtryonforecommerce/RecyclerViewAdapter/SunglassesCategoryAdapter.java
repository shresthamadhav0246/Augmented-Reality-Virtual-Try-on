package com.bawp.virtualtryonforecommerce.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.activity.SunglassesVirtualTryOn;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.bawp.virtualtryonforecommerce.model.Sunglasses;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SunglassesCategoryAdapter extends RecyclerView.Adapter<SunglassesCategoryAdapter.ViewHolder> {
    private Context context;
    private List<Sunglasses> productList;

    public SunglassesCategoryAdapter(Context context, List<Sunglasses> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_sunglasses_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sunglasses product = productList.get(position);
        holder.bind(product);

        holder.btnVirtualTryOn.setOnClickListener(view -> {
            // Using the context from the view holder's itemView
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, SunglassesVirtualTryOn.class);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        ImageView image;
        Button btnVirtualTryOn;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sunglasses_name);
            price = itemView.findViewById(R.id.sunglasses_price);
            image = itemView.findViewById(R.id.sunglasses_image);
            btnVirtualTryOn = itemView.findViewById(R.id.btnVirtualTryOn);
        }

        void bind(Sunglasses product) {
            name.setText(product.getName());
            price.setText(String.format("$%.2f", product.getPrice()));
            try {
                AssetManager assetManager = context.getAssets();
                InputStream ims = assetManager.open(product.getImageUrl());
                Drawable d = Drawable.createFromStream(ims, null);
                image.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
