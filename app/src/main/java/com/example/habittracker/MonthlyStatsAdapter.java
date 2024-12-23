package com.example.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MonthlyStatsAdapter extends RecyclerView.Adapter<MonthlyStatsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public MonthlyStatsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex("month"));
            @SuppressLint("Range") int achievementCount = cursor.getInt(cursor.getColumnIndex("achievement_count"));
            holder.monthTextView.setText("Month " + month);
            holder.achievementCountTextView.setText("Achievements: " + achievementCount);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView, achievementCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            achievementCountTextView = itemView.findViewById(R.id.achievementCountTextView);
        }
    }
}
