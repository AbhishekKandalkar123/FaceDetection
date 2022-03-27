package com.akan.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.face.Face;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends DialogFragment {
    private final String success = "Face got detected !";
    private final String failure = "Face couldn't be detected !";

    ImageView imageView;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    TextView textView;

        Button okBtn;
        Context context;
        List<FaceModel> faceModels;

        public AlertActivity(Context ctx, List<FaceModel> fModelList) {
            faceModels = fModelList;
            context = ctx;
        }

        @Nullable
        @Override
        public View
        onCreateView(@NonNull LayoutInflater inflater,
                     @Nullable ViewGroup container,
                     @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.dialog_activity, container, false);
            okBtn = view.findViewById(R.id.result_ok_button);
            recyclerView = view.findViewById(R.id.faces_rv);
            imageView = view.findViewById(R.id.thumbsUp);
            recyclerView = view.findViewById(R.id.faces_rv);
            textView = view.findViewById(R.id.IdTextView);

            Bundle bundle = getArguments();
            boolean facedDetected = bundle.getBoolean("faceDetected",false);

            if(!facedDetected){
                textView.setText(failure);
                imageView.setImageResource(R.drawable.thumb_down);
            }
            else {
                textView.setText(success);
                imageView.setImageResource(R.drawable.thumb_up);
                ShowData();
            }

            okBtn.setOnClickListener(
                    v -> dismiss());

            return view;
        }

        void ShowData() {
            rvAdapter = new RVAdapter(faceModels);
            recyclerView.setAdapter(rvAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);

        }
}
