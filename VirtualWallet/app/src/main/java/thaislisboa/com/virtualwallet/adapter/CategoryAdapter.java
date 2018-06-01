package thaislisboa.com.virtualwallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview__item_category, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int position) {

        if (categories != null && categories.size() > 0) {

            Category category = categories.get(position);

            holder.tvName.setText(category.getName());
        }
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public void addCategory(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() + 1);
    }


    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        ImageView ivDelete;

        public CategoryHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_category_item);
            ivDelete = itemView.findViewById(R.id.iv_delete_item_category);
            ivDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            ((ImageView) v).setEnabled(false);
            FirebaseDB.deleteCategory(categories.get(getAdapterPosition()), context);
            categories.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}
