package com.example.foodtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class AddFood extends AppCompatActivity {
    //private Button scan;
    private ImageButton back;
    String food = null;


    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1889;

    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Uri outputFileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        //scan = (Button) findViewById(R.id.button6);
        back = (ImageButton) findViewById(R.id.imageButton3);


/*
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    startActivityForResult(pickPhoto, GALLERY_REQUEST);
                }
            }
        });

*/
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                openactivity2();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    food = callModel(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                openactivity7(food);
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    food = callModel(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                openactivity7(food);
            }
            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
           // Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                food = callModel(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            openactivity7(food);
        }
    }

*/
    public void openactivity2() {
        Intent intent = new Intent(this, activity2.class);
        startActivity(intent);
    }

    public void openactivity6(String newItem) {
        Intent intent = new Intent(this, AddDate.class);
        intent.putExtra("FoodString", newItem);
        startActivity(intent);
    }

    public void openactivity7(String food) {
        Intent intent = new Intent(this, AddDate.class);
        intent.putExtra("FoodString", food);
        startActivity(intent);
    }

    public void btn_showDialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddFood.this);
        View myview = getLayoutInflater().inflate(R.layout.dialog_food, null);
        final EditText inputText = (EditText)myview.findViewById(R.id.input);
        Button cancel_button = (Button)myview.findViewById(R.id.cancel);
        Button add_button = (Button)myview.findViewById(R.id.add);
        alert.setView(myview);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);

        cancel_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String newItem = inputText.getText().toString();
                if(newItem.isEmpty()){
                    dialog.dismiss();
                }
                openactivity6(newItem);
            }
        });

        dialog.show();
    }


    public void btn_showDialog2(View view) {
        final AlertDialog.Builder alert2 = new AlertDialog.Builder(AddFood.this);
        View myview2 = getLayoutInflater().inflate(R.layout.camera, null);
        ImageButton camera_button = (ImageButton)myview2.findViewById(R.id.camera1);
        ImageButton gallery_button = (ImageButton)myview2.findViewById(R.id.gallery);
        alert2.setView(myview2);
        final AlertDialog dialog2 = alert2.create();
        dialog2.setCanceledOnTouchOutside(false);

        camera_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        gallery_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, GALLERY_REQUEST);
                }
            }
        });

        dialog2.show();
    }




    public String callModel (Bitmap image) throws IOException {
        Module module = null;

        try {

            module = Module.load(assetFilePath(this, "NewModel.pt"));
        } catch (IOException e) {
            Log.e("PytorchHelloWorld", "Error reading assets", e);
            finish();
        }


        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                image, 0, 0, image.getWidth(), image.getHeight(), new float[] {0.485f, 0.456f, 0.406f}, new float[] {0.229f, 0.224f, 0.225f});

        // running the model
        IValue inputs = IValue.from(inputTensor);
        IValue[] outputs = module.forward(inputs).toTuple();
        final Tensor outputTensor = outputs[0].toTensor();

        // getting tensor content as java array of floats
        final float[] scores = outputTensor.getDataAsFloatArray();

        // searching for the index with maximum score
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }

        String className = ImageClasses.IMAGENET_CLASSES[maxScoreIdx];

        return className;
    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

}