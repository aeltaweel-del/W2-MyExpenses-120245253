# MyExpenses Android App

A personal expense and budget tracking Android application built using Java, Room Database, LiveData, and RecyclerView.

## Architecture & Features
- **Room Database**: Local persistence with `Category` and `Expense` entities, DAOs, and background execution via `ExecutorService`.
- **LiveData**: Reactive UI updates for category totals and expense lists.
- **RecyclerView**: Custom adapters (`CategoryAdapter`, `ExpenseAdapter`) for displaying budgets and spending.
