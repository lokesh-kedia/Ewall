package com.lokeshkedia.e_wall;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.provider.Settings.System.DATE_FORMAT;


public class PFSecond extends Fragment {
    private final int PICK_IMAGE_REQUEST = 71;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
        private Uri filePath;
    private View v;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int STORAGE_PERMISSION_CODE = 8;
    private Bitmap bitmap = null;
    private File file;
    private File sourceFile;
    int points = 0, level = 0;
    private File destFile;

    private DatabaseReference databaseReference;

    public static PFSecond newInstance(String text) {

        PFSecond f = new PFSecond();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pfsecond, container, false);
        FloatingActionButton floatingActionButton = v.findViewById(R.id.cp);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        FloatingActionButton floatingActionButton1 = v.findViewById(R.id.upload);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return v;
    }

    public void uploadImage() {
        if (checkWriteExternalPermission()) {
            ImageView imageView = v.findViewById(R.id.ip);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 75, baos);
            final byte[] data = baos.toByteArray();

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            if (data != null) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final StorageReference ref = storageReference.child("Photographers/" + uid + "/" + UUID.randomUUID().toString());
                ref.putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
//                            String url = taskSnapshot.getDownloadUrl().toString();
                                UploadTask uploadTask = ref.putBytes(data);
                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return ref.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Uri url = task.getResult();

                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(uid).child("Uploads");
                                            String key = databaseReference.push().getKey();
                                            TextInputEditText editText = v.findViewById(R.id.tagsedit);
                                            String tags = String.valueOf(editText.getText());
                                            String[] tags1 = tags.split(",");
                                            Map userInfo1 = new HashMap<>();
                                            userInfo1.put("Img", url.toString());
                                            userInfo1.put("tags", tags);
                                            databaseReference.child(key).updateChildren(userInfo1);
                                        /*databaseReference.child(key).child("Img").setValue(url.toString());
                                        databaseReference.child(key).child("tags").setValue(tags);*/
                                            /* databaseReference.child(key).setValue(new Upload(url.toString(),tags));*/
                                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Tags");

                                            for (String i : tags1) {
                                                databaseReference.child(i).child(key).child("Img").setValue(url.toString());
                                                databaseReference.child(i).child(key).child("Owner").setValue(uid);

                                            }
                                            databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key);
                                            /*databaseReference.setValue(new Upload(url.toString(),tags));*/
                                            //mArea.child("url").setValue(url.toString());
                                            Map userInfo2 = new HashMap<>();
                                            userInfo2.put("Img", url.toString());
                                            userInfo2.put("tags", tags);
                                            userInfo2.put("Owner", uid);
                                            userInfo2.put("likecount", 0);
                                            databaseReference.updateChildren(userInfo2);
                                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(uid).child("Profile");

                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    points = dataSnapshot.child("Points").getValue(Integer.class);
                                                    level = dataSnapshot.child("Level").getValue(Integer.class);
                                                    //Toast.makeText(getActivity(), String.valueOf(points), Toast.LENGTH_LONG).show();
                                                    //Toast.makeText(getActivity(), String.valueOf(level), Toast.LENGTH_LONG).show();
                                                    databaseReference.child("Points").setValue(points + 5);
                                                    points = points + 5;
                                                    if (points < 100) {
                                                        level = 0;
                                                    } else if (points < 1000) {
                                                        level = 1;
                                                    } else if (points < 10000) {
                                                        level = 2;
                                                    } else if (points < 100000) {
                                                        level = 3;
                                                    } else if (points < 1000000) {
                                                        level = 4;
                                                    } else if (points < 10000000) {
                                                        level = 5;
                                                    }
                                                    databaseReference.child("Level").setValue(level);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Handle failures
                                            // ...
                                        }
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }
        } else {
            requestStoragePermission();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = v.findViewById(R.id.ip);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                //Cursor cursor = MediaStore.Images.Media.query(getActivity().getContentResolver(), filePath, new String[]{MediaStore.Images.Media.DATA});

                /*if (cursor != null && cursor.moveToFirst()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                    //Create ImageCompressTask and execute with Executor.
                    imageCompressTask = new ImageCompressTask(getActivity(), path, iImageCompressTaskListener);

                    mExecutorService.execute(imageCompressTask);
                }
*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
                //shareWall();

            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkWriteExternalPermission() {
        boolean r;
        r = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return r;
    }


}
