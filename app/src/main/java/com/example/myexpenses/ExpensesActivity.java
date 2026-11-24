package com.example.myexpenses;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExpensesActivity extends AppCompatActivity {

    private EditText etTitle, etAmount;
    private ExpenseDatabase db;
    private ExpenseAdapter adapter;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName != null ? categoryName : "المصاريف");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTitle = findViewById(R.id.etExpenseTitle);
        etAmount = findViewById(R.id.etExpenseAmount);
        Button btnAdd = findViewById(R.id.btnAddExpense);
        RecyclerView rv = findViewById(R.id.rvExpenses);

        db = ExpenseDatabase.getDatabase(this);

        adapter = new ExpenseAdapter(expense -> {
            new AlertDialog.Builder(ExpensesActivity.this)
                    .setTitle("حذف المصروف")
                    .setMessage("هل أنت متأكد من حذف \"" + expense.title + "\"؟")
                    .setPositiveButton("حذف", (dialog, which) ->
                            ExpenseDatabase.databaseWriteExecutor.execute(() -> db.expenseDao().delete(expense)))
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        db.expenseDao().getByCategory(categoryId).observe(this, expenses -> adapter.setExpenses(expenses));

        btnAdd.setOnClickListener(v -> addExpense());
    }

    private void addExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "الرجاء إدخال الوصف والمبلغ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoryId == -1) {
            Toast.makeText(this, "خطأ في تحديد التصنيف", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(categoryId, title, amount, System.currentTimeMillis());

        ExpenseDatabase.databaseWriteExecutor.execute(() -> db.expenseDao().insert(expense));

        etTitle.setText("");
        etAmount.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}