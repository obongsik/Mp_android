package com.example.habittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<String> goalList;
    private HabitDatabaseHelper dbHelper;

    public GoalAdapter(ArrayList<String> goalList) {
        this.goalList = goalList;
        this.dbHelper = dbHelper;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // goal_item.xml을 뷰홀더에 연결
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        // 현재 목표 가져오기
        String goal = goalList.get(position);
        holder.goalText.setText(goal);
        holder.goalCheckBox.setChecked(goal.isEmpty());

        // 체크박스 상태 초기화
        holder.goalCheckBox.setChecked(false);

        // 체크박스 이벤트 처리
        holder.goalCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 목표가 완료되었을 때의 동작
                Toast.makeText(holder.itemView.getContext(), "목표를 달성했습니다!!!", Toast.LENGTH_SHORT).show();
                //dbHelper.
            } else {
                // 목표가 미완료 상태로 돌아갔을 때
                Toast.makeText(holder.itemView.getContext(), "목표 달성 취소 .. ", Toast.LENGTH_SHORT).show();
                System.out.println("Goal unchecked: " + goal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        // 뷰홀더에서 TextView와 CheckBox를 연결
        TextView goalText;
        CheckBox goalCheckBox;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalText =itemView.findViewById(R.id.goalText);
            goalCheckBox = itemView.findViewById(R.id.goalCheckBox); // `goal_item.xml`의 CheckBox
        }
    }
}