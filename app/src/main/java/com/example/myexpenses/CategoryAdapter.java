package com.example.myexpenses;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryTotal> list = new ArrayList<>();
    private final OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onCategoryClick(CategoryTotal category);
        void onCategoryLongClick(CategoryTotal category);
    }

    public CategoryAdapter(OnCategoryActionListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<CategoryTotal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryTotal item = list.get(position);
        holder.tvName.setText(item.name);

        // عرض المجموع / الميزانية مثل 85 / 100
        String text = String.format(Locale.US, "%.1f / %.1f", item.total, item.budget);
        holder.tvAmount.setText(text);

        // إذا تجاوز المجموع الميزانية يظهر باللون الأحمر
        if (item.total > item.budget) {
            holder.tvAmount.setTextColor(Color.RED);
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#16A34A"));
        }

        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(item));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onCategoryLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvAmount = itemView.findViewById(R.id.tvCategoryAmount);
        }
    }
}