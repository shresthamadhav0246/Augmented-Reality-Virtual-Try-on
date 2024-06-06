package com.bawp.virtualtryonforecommerce.RecyclerViewAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.R;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private Context context;
    private List<Uri> imageUris;
    private OnImageRemovedListener listener;

    public interface OnImageRemovedListener {
        void onImageRemoved(Uri uri);
    }

    public ImagesAdapter(Context context, List<Uri> imageUris, OnImageRemovedListener listener) {
        this.context = context;
        this.imageUris = imageUris;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        holder.imageView.setImageURI(imageUri);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity or fragment that an image has been removed
                if (listener != null) {
                    listener.onImageRemoved(imageUri);
                }
                // Remove the image from the list and notify the adapter
                imageUris.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageUris.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView deleteIcon;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItemView);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
