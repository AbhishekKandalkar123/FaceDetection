package com.akan.myapplication;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView ;
    private AlertActivity alertActivity;
    final private int PHOTO_CLICK = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.IdImageView);

        Intent intent = getIntent();
        String OpenCamera = intent.getStringExtra("OpenCamera");
        if(OpenCamera.equals("true")){
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent1, PHOTO_CLICK);
        }
    }

    public void detectFace(Bitmap bitmap) throws IOException {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();


        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        List<FaceModel> list = new ArrayList<>();

                                        imageView.setImageBitmap(mutableBitmap);
                                        int i=1;
                                        for (Face face : faces) {
                                            FaceModel faceModel = new FaceModel();

                                            faceModel.setFaceId(i);
                                            faceModel.setAngleX(face.getHeadEulerAngleX());
                                            faceModel.setAngleY(face.getHeadEulerAngleY());
                                            faceModel.setAngleZ(face.getHeadEulerAngleZ());
                                            faceModel.setSmile(face.getSmilingProbability()*100);
                                            faceModel.setLeftEye(face.getLeftEyeOpenProbability()*100);
                                            faceModel.setRightEye(face.getRightEyeOpenProbability()*100);
                                            list.add(faceModel);
                                            i++;

                                            Rect bounds = face.getBoundingBox();
                                            Paint paint = new Paint();
                                            paint.setColor(Color.GREEN);
                                            paint.setStrokeWidth(2);
                                            paint.setStyle(Paint.Style.STROKE);
                                            canvas.drawRect(bounds, paint);
                                        }
                                        if (faces.size() == 0) {
                                            alertActivity = new AlertActivity(MainActivity.this,list);
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean("faceDetected", false);
                                            alertActivity.setArguments(bundle);
                                            alertActivity.show((MainActivity.this).getSupportFragmentManager(), "Image Dialog");

                                        }else {
                                            alertActivity = new AlertActivity(MainActivity.this, list);
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean("faceDetected", true);
                                            alertActivity.setArguments(bundle);
                                            alertActivity.show((MainActivity.this).getSupportFragmentManager(), "Image Dialog");
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        alertActivity = new AlertActivity(MainActivity.this,new ArrayList<>());
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("faceDetected",false);
                                        alertActivity.setArguments(bundle);
                                        alertActivity.show((MainActivity.this).getSupportFragmentManager(),"Image Dialog");
                                    }
                                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_CLICK){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                try{
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    detectFace(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "No Image clicked", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Operation Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
