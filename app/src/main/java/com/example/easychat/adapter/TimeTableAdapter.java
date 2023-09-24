package com.example.easychat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.R;
import com.example.easychat.model.TimeTableEntry;

import java.util.List;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.TimeTableViewHolder> {
    private List<TimeTableEntry> timeTableList;

    public TimeTableAdapter(List<TimeTableEntry> timeTableList) {
        this.timeTableList = timeTableList;
    }

    @NonNull
    @Override
    public TimeTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_table, parent, false);
        return new TimeTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableViewHolder holder, int position) {
        TimeTableEntry entry = timeTableList.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return timeTableList.size();
    }

    public class TimeTableViewHolder extends RecyclerView.ViewHolder {
        private TextView lectureDetailsTextView;
        private TextView timeTextView;
        private TextView teacherNameTextView;

        public TimeTableViewHolder(@NonNull View itemView) {
            super(itemView);
            lectureDetailsTextView = itemView.findViewById(R.id.textViewLectureName);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            teacherNameTextView = itemView.findViewById(R.id.textViewTeacherName);
        }

        public void bind(TimeTableEntry entry) {
            lectureDetailsTextView.setText(entry.getLectureDetails());
            timeTextView.setText(entry.getTime());
            teacherNameTextView.setText(entry.getTeacherName());
        }
    }
}
