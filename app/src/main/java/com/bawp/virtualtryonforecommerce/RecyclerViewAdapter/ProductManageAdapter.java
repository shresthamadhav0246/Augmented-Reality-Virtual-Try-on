package com.bawp.virtualtryonforecommerce.RecyclerViewAdapter;


        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bawp.virtualtryonforecommerce.R;
        import com.bawp.virtualtryonforecommerce.model.Product;

        import java.util.List;

public class ProductManageAdapter extends RecyclerView.Adapter<ProductManageAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductListener onProductListener;

    public ProductManageAdapter(List<Product> productList, OnProductListener onProductListener) {
        this.productList = productList;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_manage_item, parent, false);
        return new ProductViewHolder(itemView, onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.titleTextView.setText(product.getProductName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        Button editButton, deleteButton;

        public ProductViewHolder(@NonNull View itemView, OnProductListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.productTitleTextView);
            editButton = itemView.findViewById(R.id.editProductButton);
            deleteButton = itemView.findViewById(R.id.deleteProductButton);

            editButton.setOnClickListener(v -> listener.onEditClicked(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> listener.onDeleteClicked(getAdapterPosition()));
        }
    }

    public interface OnProductListener {
        void onEditClicked(int position);
        void onDeleteClicked(int position);
    }
}
