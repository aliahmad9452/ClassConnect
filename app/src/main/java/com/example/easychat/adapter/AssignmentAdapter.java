package com.example.easychat.adapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.R;
import com.example.easychat.model.AssignmentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private List<AssignmentModel> assignmentList;
    private StorageReference storageRef;
    private static final int REQUEST_PDF_PICKER = 1;

    public AssignmentAdapter(List<AssignmentModel> assignmentList) {
        this.assignmentList = assignmentList;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        AssignmentModel assignment = assignmentList.get(position);
        holder.bind(assignment);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class AssignmentViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dueDateTextView;
        private TextView statusTextView;
        private TextView pdfDownloadUrl;
        private Button submitButton;
        private Uri selectedPdfUri = null; // Store the selected PDF URI
        private AssignmentModel assignment;
        private ProgressBar progressBar;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            dueDateTextView = itemView.findViewById(R.id.textViewDueDate);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
            pdfDownloadUrl = itemView.findViewById(R.id.textViewPdf);
            submitButton = itemView.findViewById(R.id.submitButton);
        }

        public void bind(final AssignmentModel assignment) {
            this.assignment = assignment;
            titleTextView.setText(assignment.getTitle());
            descriptionTextView.setText(assignment.getDescription());
            dueDateTextView.setText(assignment.getDueDate());
            statusTextView.setText(assignment.getStatus());
            pdfDownloadUrl.setText(assignment.getPdfDownloadUrl());
            pdfDownloadUrl.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark));

            final String pdfUrl = assignment.getPdfDownloadUrl();
            if (pdfUrl != null && !pdfUrl.isEmpty()) {
                pdfDownloadUrl.setText("Download PDF"); // Set the text for downloading
                pdfDownloadUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement the logic to open the PDF using the URL
                        // Example: Open PDF using an Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(pdfUrl));
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                pdfDownloadUrl.setText("PDF Not Available"); // Set text if no PDF URL is available
                pdfDownloadUrl.setOnClickListener(null); // Disable click action
            }

            // Set a click listener for the submit button
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show a file picker to select a PDF file
                    openFilePicker();
                }
            });

            // Disable the submit button if the assignment is already "Submitted"
            if ("Submitted".equals(assignment.getStatus())) {
                submitButton.setEnabled(false);
            } else {
                submitButton.setEnabled(true);
            }
        }

        private void openFilePicker() {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            ((Activity) itemView.getContext()).startActivityForResult(intent, REQUEST_PDF_PICKER);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_PDF_PICKER && resultCode == Activity.RESULT_OK && data != null) {
                selectedPdfUri = data.getData();
                uploadPdf(selectedPdfUri);
            }
        }

        private void uploadPdf(Uri pdfUri) {
            if (pdfUri != null) {
                String assignmentId = assignment.getId();
                String pdfFilename = "assignment_" + assignmentId + ".pdf"; // Unique filename

                // Create a reference to the Firebase Storage location
                StorageReference pdfRef = storageRef.child(pdfFilename);

                // Upload the PDF file to Firebase Storage
                pdfRef.putFile(pdfUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                // PDF uploaded successfully, update the assignment status and save the download URL
                                String pdfDownloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                                updateAssignmentStatus(assignmentId, "Submitted", pdfDownloadUrl);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Handle the failure to upload the PDF if needed
                            }
                        });
            }
        }

        private void updateAssignmentStatus(String assignmentId, String status, String pdfDownloadUrl) {
            FirebaseFirestore.getInstance().collection("assignments")
                    .document(assignmentId)
                    .update("status", status, "pdfDownloadUrl", pdfDownloadUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.GONE);
                            // Assignment status updated successfully.
                            // You can display a message to the user or update the UI as needed.
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the failure to update the status if needed.
                        }
                    });
        }
    }
}
