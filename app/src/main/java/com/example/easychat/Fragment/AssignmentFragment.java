package com.example.easychat.Fragment;

import static com.example.easychat.utils.FirebaseUtil.currentUserId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.R;
import com.example.easychat.adapter.AssignmentAdapter;
import com.example.easychat.model.AssignmentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AssignmentFragment extends Fragment {
    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private List<AssignmentModel> assignmentList;
    private Button  submitButton;
    private static final int REQUEST_PDF_PICKER = 1;

    public AssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assignmentList = new ArrayList<>();
        adapter = new AssignmentAdapter(assignmentList);
        recyclerView.setAdapter(adapter);



        // Fetch the user's class and semester from Firestore
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
                                FirebaseFirestore.getInstance().collection("assignments")
                                        .whereEqualTo("userClass", userClass)
                                        .whereEqualTo("userSemester", userSemester)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    assignmentList.clear(); // Clear the list before adding new data
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        AssignmentModel assignment = document.toObject(AssignmentModel.class);
                                                        if (assignment != null) {
                                                            assignmentList.add(assignment);
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
