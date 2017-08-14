package com.example.thuhuong.appmyimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ImageActivity extends AppCompatActivity {
    ImageView btnChoose, btnCapture;
    EditText nameImage;
    Button btnSave, btnShow, btnShareImage;
    ListView lvPhoto;
    ImageView imgView;
    Bundle bundle;

    private Button btnLoginFB;
    private TextView txtnameUser, txtemail;
    private ImageView profilePictureView;

    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    public static int select_Image = 1;
    Bitmap bitmap;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    //ArrayList<Image> arrayImage;
    ArrayList<String> arrName;
    ArrayList<String> urlImg;
    ArrayList<String > timeImg;
    DatabaseReference mDB;
    ImageAdapter adapter = null;
    int REQUEST_CODE_IMG_CAPTURE=1;
    int REQUEST_CODE_IMG_CHOOSE=0;
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://appmyimage.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mDB = FirebaseDatabase.getInstance().getReference();

        shareDialog = new ShareDialog(ImageActivity.this);

        Mapping();
        getDataOfIntent();
        //arrayImage = new ArrayList<Image>();
        arrName = new ArrayList<String>();
        urlImg = new ArrayList<String>();
        timeImg = new ArrayList<String>();
        //adapter = new ImageAdapter(this, R.layout.rowlayout, arrayImage);
        //lvPhoto.setAdapter(adapter);
        loadData();
        setChooseImagxe();
        setCaptureImage();
        shareImageOnFB();
        saveImage();
        onClickShow();
        onClickLogout();
    }

    private void onClickShow() {
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, listImage.class);
                intent.putExtra("Packet", bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putStringArrayList("nameImg", arrName);
                bundle1.putStringArrayList("urlImg", urlImg);
                bundle1.putStringArrayList("timeImg", timeImg);
                intent.putExtra("List", bundle1);
                startActivity(intent);
            }
        });
    }

    private void saveImage() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from an ImageView as bytes
                Calendar calendar = Calendar.getInstance();

                StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                imgView.setDrawingCacheEnabled(true);
                imgView.buildDrawingCache();
                Bitmap bitmap = imgView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ImageActivity.this, "Lỗi up ảnh", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ImageActivity.this, "Upload Thành công", Toast.LENGTH_SHORT).show();
                        Log.d("AAAA", downloadUrl+"");

                        Image hinhAnh = new Image(nameImage.getText().toString(), String.valueOf(downloadUrl));
                        hinhAnh.setCreateTime();
                        mDB.child("Image").push().setValue(hinhAnh, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    nameImage.setText("");
                                    imgView.setImageResource(R.drawable.myphoto);
                                    Toast.makeText(ImageActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ImageActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void loadData() {
        mDB.child("Image").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Image hinhAnh = (Image)dataSnapshot.getValue(Image.class);
                //arrayImage.add(new Image(hinhAnh.getName(), hinhAnh.getUrl(), hinhAnh.getTimeCreate()));
                arrName.add(hinhAnh.getName());
                urlImg.add(hinhAnh.getUrl());
                timeImg.add(hinhAnh.getTimeCreate());
               // adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setChooseImagxe() {
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, 0);
                startActivityForResult(intent, REQUEST_CODE_IMG_CHOOSE);
            }
        });
    }

    private void setCaptureImage() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_IMG_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_IMG_CAPTURE && resultCode == RESULT_OK && data!=null){
            bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
        }

        if(requestCode == REQUEST_CODE_IMG_CHOOSE && resultCode == RESULT_OK && data!=null){
            Uri targetUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imgView.setImageBitmap(bitmap);
            //imgView.setImageURI(targetUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Mapping() {
        btnCapture = (ImageView) findViewById(R.id.imgCapture);
        btnChoose = (ImageView) findViewById(R.id.imgChooseImage);
        btnSave = (Button) findViewById(R.id.btnSaveImg);
        btnShow = (Button) findViewById(R.id.btnShow);
        btnShareImage = (Button) findViewById(R.id.btnShare);
        nameImage = (EditText) findViewById(R.id.edtName);
        imgView = (ImageView) findViewById(R.id.imgView);

        profilePictureView = (ImageView) findViewById(R.id.imageProfilePictureView);
        txtnameUser = (TextView) findViewById(R.id.tvName);
        txtemail = (TextView) findViewById(R.id.tvEmail);
        btnLoginFB = (Button) findViewById(R.id.btnLogout);

    }

    public void getDataOfIntent() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("Packet");

        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String uri = bundle.getString("uri");

        txtemail.setText(email);
        txtnameUser.setText(name);
        Picasso.with(ImageActivity.this).load(uri).into(profilePictureView);
    }

    private void onClickLogout() {
        btnLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ImageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void shareImageOnFB(){
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });
    }
}
