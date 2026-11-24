package com.example.myexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> list = new ArrayList<>();
    private final OnExpenseLongClickListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    public interface OnExpenseLongClickListener {
        void onExpenseLongClick(Expense expense);
    }

    public ExpenseAdapter(OnExpenseLongClickListener listener) {
        this.listener = listener;
    }

    public void setExpenses(List<Expense> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense item = list.get(position);
        holder.tvTitle.setText(item.title);
        holder.tvAmount.setText(String.format(Locale.US, "%.1f", item.amount));
        holder.tvDate.setText(sdf.format(new Date(item.createdAt)));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onExpenseLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvExpenseTitle);
            tvDate = itemView.findViewById(R.id.tvExpenseDate);
            tvAmount = itemView.findViewById(R.id.tvExpenseAmount);
        }
    }
}