package com.example.myexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etBudget;
    private ExpenseDatabase db;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etCategoryName);
        etBudget = findViewById(R.id.etCategoryBudget);
        Button btnAdd = findViewById(R.id.btnAddCategory);
        RecyclerView rv = findViewById(R.id.rvCategories);

        db = ExpenseDatabase.getDatabase(this);

        adapter = new CategoryAdapter(new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onCategoryClick(CategoryTotal category) {
                Intent intent = new Intent(MainActivity.this, ExpensesActivity.class);
                intent.putExtra("CATEGORY_ID", category.id);
                intent.putExtra("CATEGORY_NAME", category.name);
                startActivity(intent);
            }

            @Override
            public void onCategoryLongClick(CategoryTotal category) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("حذف التصنيف")
                        .setMessage("هل أنت متأكد من حذف \"" + category.name + "\"؟ سيتم حذف جميع مصاريفه أيضاً.")
                        .setPositiveButton("حذف", (dialog, which) -> {
                            Category c = new Category(category.name, category.budget);
                            c.id = category.id;
                            ExpenseDatabase.databaseWriteExecutor.execute(() -> db.categoryDao().delete(c));
                        })
                        .setNegativeButton("إلغاء", null)
                        .show();
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // مراقبة البيانات وتحديث القائمة وشريط العنوان تلقائياً
        db.categoryDao().getCategoriesWithTotal().observe(this, categories -> {
            adapter.setCategories(categories);

            double grandTotal = 0;
            for (CategoryTotal item : categories) {
                grandTotal += item.total;
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(String.format(Locale.US, "Total: %.1f", grandTotal));
            }
        });

        btnAdd.setOnClickListener(v -> addCategory());
    }

    private void addCategory() {
        String name = etName.getText().toString().trim();
        String budgetStr = etBudget.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(budgetStr)) {
            Toast.makeText(this, "الرجاء إدخال الاسم والميزانية", Toast.LENGTH_SHORT).show();
            return;
        }

        double budget = Double.parseDouble(budgetStr);
        Category category = new Category(name, budget);

        ExpenseDatabase.databaseWriteExecutor.execute(() -> db.categoryDao().insert(category));

        etName.setText("");
        etBudget.setText("");
    }
}