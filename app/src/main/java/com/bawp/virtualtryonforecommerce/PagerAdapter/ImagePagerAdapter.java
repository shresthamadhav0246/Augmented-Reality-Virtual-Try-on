package com.bawp.virtualtryonforecommerce.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bawp.virtualtryonforecommerce.R;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mImageUrls;

    public ImagePagerAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(mContext).load(mImageUrls.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mImageUrls != null ? mImageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    // Method to set image URLs
    public void setImageUrls(List<String> imageUrls) {
        mImageUrls = imageUrls;
        notifyDataSetChanged();
    }
}
