package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import is.hi.hbv601g.matbjorg_app.R;

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.ViewHolder> {
    private static final String TAG = "CategoryItemsAdapter";

    private String[] mTags;
    private Context context;
    private CategoryItemsAdapter.OnAdListener mOnAdListener;

    public CategoryItemsAdapter(String[] tags, Context context, CategoryItemsAdapter.OnAdListener onAdListener) {
        this.mTags = tags;
        this.context = context;
        this.mOnAdListener = onAdListener;
    }

    @NonNull
    @Override
    public CategoryItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
        CategoryItemsAdapter.ViewHolder holder = new CategoryItemsAdapter.ViewHolder(view, mOnAdListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemsAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(mTags[position]);
    }

    @Override
    public int getItemCount() {
        return mTags.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName;
        CategoryItemsAdapter.OnAdListener onAdListener;

        public ViewHolder(@NonNull View itemView, CategoryItemsAdapter.OnAdListener onAdListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.category_item_name);
            this.onAdListener = onAdListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdListener.onAdClick(getAdapterPosition());
        }
    }

    public interface OnAdListener {
        void onAdClick(int position);
    }
}
