package com.example.habittracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyStatsAdapter extends RecyclerView.Adapter<WeeklyStatsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public WeeklyStatsAdapter(Context context, Cursor cursor) {
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
            String week = cursor.getString(cursor.getColumnIndex("week"));
            int achievementCount = cursor.getInt(cursor.getColumnIndex("achievement_count"));
            holder.weekTextView.setText("Week " + week);
            holder.achievementCountTextView.setText("Achievements: " + achievementCount);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView weekTextView, achievementCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            weekTextView = itemView.findViewById(R.id.weekTextView);
            achievementCountTextView = itemView.findViewById(R.id.achievementCountTextView);
        }
    }
}

