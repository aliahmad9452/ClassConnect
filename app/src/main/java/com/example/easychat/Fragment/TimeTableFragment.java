package com.example.easychat.Fragment;

import static com.example.easychat.utils.FirebaseUtil.currentUserId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.R;
import com.example.easychat.adapter.TimeTableAdapter;
import com.example.easychat.model.AssignmentModel;
import com.example.easychat.model.TimeTableEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TimeTableFragment extends Fragment {

    private RecyclerView recyclerView;
    private TimeTableAdapter adapter;
    private List<TimeTableEntry> timeTableList;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        timeTableList = new ArrayList<>();
        adapter = new TimeTableAdapter(timeTableList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String userClass = document.getString("userClass");
                                String userSemester = document.getString("userSemester");

                                // Use the user's class and semester to query Firestore for assignments
                                FirebaseFirestore.getInstance().collection("timetable")
                                        .whereEqualTo("userClass", userClass)
                                        .whereEqualTo("userSemester", userSemester)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    timeTableList.clear(); // Clear the list before adding new data
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        TimeTableEntry timeTableEntry = document.toObject(TimeTableEntry.class);
                                                        if (timeTableEntry != null) {
                                                            timeTableList.add(timeTableEntry);
                                                        }
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }

                                            }
                                        });
                            }
                        }
                    }
                });

        return view;
    }
}
