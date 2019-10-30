package com.example.yinyang_taengkwa.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.skyhope.materialtagview.TagView;
import com.skyhope.materialtagview.TagView_me;
import com.skyhope.materialtagview.model.TagModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditprofileActivity extends AppCompatActivity {

    Button back_profile;
    TextView edit_image;
    TextView confirm;
    TextView Text_food;
    EditText editTextUsername;
    CircleImageView CircleImageViewProfile;
    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;
    SpinnerDialog spinnerDialog;
    ArrayList<String> item = new ArrayList<>();
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    String PREF_NAME = "Log in";
    String image_user = "";
    TagView_me tagview;
    int clickimg = 0;
    String textfood = "";

    String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/profile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().hide();


        final Intent intent = getIntent();
        final String image = intent.getStringExtra("image");
        final String username = intent.getStringExtra("username");
        final String foodLose = intent.getStringExtra("foodLose");


        back_profile = findViewById(R.id.button_back_profile);
        edit_image = findViewById(R.id.edit_image);
        CircleImageViewProfile = findViewById(R.id.user_profile);

        Text_food = findViewById(R.id.TextviewFood);

        tagview = findViewById(R.id.tagview);

        confirm = findViewById(R.id.confirm);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextUsername.setText(username);

        tagview.addTagLimit(10);
        tagview.setTagBackgroundColor("#FF9B9B9B");
        tagview.setTagTextColor("#FF070707");

        if(!foodLose.contains("ไม่มี"))
            setDataTagView(tagview, foodLose);

        if (image.isEmpty()) {
            CircleImageViewProfile.setImageResource(R.drawable.ic_user);
        } else {

            Picasso.get().load(url.concat(image)).into(CircleImageViewProfile);
        }

        sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditprofileActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("ยืนยันการแก้ไขข้อมูลส่วนตัวของคุณหรือไม่");
                //set cancelable
                builder.setCancelable(true);
                //set positive / yes button


                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = sp.getString("email", "");
                        final String username2 = editTextUsername.getText().toString().trim();


                        List<TagModel> foodd = tagview.getSelectedTags();

                        for (int i = 0; i < foodd.size(); i++) {
                            textfood += foodd.get(i).getTagText();
                            if(!(i == (foodd.size() - 1)))
                                textfood += ",";
                        }

                        Log.e("Food Lose", textfood);


                        if (image_user.isEmpty()) {
                            image_user = imageToString(((BitmapDrawable) CircleImageViewProfile.getDrawable()).getBitmap());

                        }

                        if (textfood.isEmpty()) {
                            textfood = "ไม่มี";
                        }

                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateProfile(email, image_user, username2, textfood);
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse de = response.body();

                                if (de.isStatus()) {
                                    Toast.makeText(EditprofileActivity.this, "อัพเดทข้อมูลเรียบร้อย", Toast.LENGTH_SHORT);

                                    edit = sp.edit();

                                    edit.putString("image", image_user);
                                    edit.putString("username", username2);
                                    edit.putString("foodLose", textfood);
                                    edit.commit();

                                    tagview = null;

                                    finish();

                                } else {
                                    Toast.makeText(EditprofileActivity.this, "ไม่ผ่าน", Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

            }
        });

        back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(EditprofileActivity.this, Fragment_profile.class));
                finish();

            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditprofileActivity.this);

            }
        });


        initItems();
        spinnerDialog = new SpinnerDialog(EditprofileActivity.this, item, "กรุณาเลือกวัตถุดิบที่แพ้");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tagview.addTag(item, false);

            }
        });


        Text_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });
    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    image_user = imageToString(bitmap);
                    CircleImageViewProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void initItems() {
        item.add("ไม่มี");
        item.add("เนื้อปู");
        item.add("เนื้อเป็ด");
        item.add("เนื้อปลาหมึก");
        item.add("เนื้อหอยนางรม");
        item.add("เนื้อหอยโข่ง");
        item.add("เนื้อห่าน");
        item.add("เนื้อตะพาบ");
        item.add("เนื้อเต่า");
        item.add("เนื้อกระต่าย");
        item.add("เนื้อปลาไหล");
        item.add("เนื้อหอยเชลล์");
        item.add("เนื้อปลา");
        item.add("กล้วย");
        item.add("ส้ม");
        item.add("สาลี่");
        item.add("อ้อย");
        item.add("แตงโม");
        item.add("สับปะรด");
        item.add("น้ำส้ม");
        item.add("องุ่น");
        item.add("มะพร้าว");
        item.add("มะละกอ");
        item.add("ทับทิม");
        item.add("ส้มโอ");
        item.add("มะขาม");
        item.add("แอ๊ปเปิ้ล");
        item.add("มังคุด");
        item.add("มะเฟือง");
        item.add("มะม่วงดิบ");
        item.add("ถั่วเขียว");
        item.add("ถั่วเหลือง");
        item.add("เต้าหู้");
        item.add("ข้าวฟ่าง");
        item.add("ลูกเดือย");
        item.add("ถั่วแดง");
        item.add("ข้าวโพด");
        item.add("แตงกวา");
        item.add("มะเขือยาว");
        item.add("สาหร่ายทะเล");
        item.add("ผักกาดหอม");
        item.add("ฟัก");
        item.add("มะเขือเทศ");
        item.add("ฟักทอง");
        item.add("ผักกระเฉด");
        item.add("ขึ้นฉ่าย");
        item.add("ใบตำลึง");
        item.add("บวบ");
        item.add("ชา");
        item.add("มะระ");
        item.add("ผักกาดขาว");
        item.add("ผักบุ้ง");
        item.add("เห็ดฟาง");
        item.add("หัวไช้เท้า");
        item.add("หน่อไม้");
        item.add("เหง้าบัว");
        item.add("เห็ดหูหนูขาว");
        item.add("เห็ดหูหนูดำ");
        item.add("เก๊กฮวย");
        item.add("บัวบก");
        item.add("เม็ดแมงลัก");
        item.add("ดอกไม้จีน");
        item.add("เห็ดหอม");
        item.add("ผักกาดแก้ว");
        item.add("ถั่วลันเตา");
        item.add("บีตรู้ต");
        item.add("เซเลอรี่");
        item.add("เยื่อไผ่");
        item.add("หน่อไม้ฝรั่ง");
        item.add("ดอกขจร");
        item.add("ยอดฟักแม้ว");
        item.add("สายบัว");
        item.add("ถั่วฝักยาว");
        item.add("แห้ว");
        item.add("เห็ดเข็มทอง");
        item.add("ผักสลัด");
        item.add("รากบัว");
        item.add("ถั่วงอก");
        item.add("สะระแหน่");
        item.add("เนื้อวัว");
        item.add("เนื้อไก่");
        item.add("เนื้อกุ้ง");
        item.add("เนื้อแพะ");
        item.add("เนื้องู");
        item.add("ไข่");
        item.add("ปลาหมึกแห้ง");
        item.add("เงาะ");
        item.add("ลำไย");
        item.add("มะม่วง");
        item.add("เนื้อมะพร้าว");
        item.add("ลิ้นจี่");
        item.add("ทุเรียน");
        item.add("ขนุน");
        item.add("มะกอก");
        item.add("พริก");
        item.add("ผักชี");
        item.add("พริกไทย");
        item.add("ใบแมงลัก");
        item.add("ขิง");
        item.add("กระเทียม");
        item.add("งาดำ");
        item.add("หอมหัวใหญ่");
        item.add("หอมเล็ก");
        item.add("ข้าวเหนียว");
        item.add("ข่า");
        item.add("กะเพรา");
        item.add("โหระพา");
        item.add("ตะไคร้");
        item.add("ต้นหอม");
        item.add("ใบยี่หร่า");
        item.add("ชะอม");
        item.add("มัน");
        item.add("เหล้า");
        item.add("เครื่องเทศ");
        item.add("น้ำพริกแกง");
        item.add("เนย");
        item.add("เนยถั่ว");
        item.add("น้ำพริกเผา");
        item.add("ซอสพริก");
        item.add("น้ำมัน");

    }

    private void setDataTagView(TagView_me tagView, String text) {
        String[] textList = text.split(",");
        for (String t : textList) {
            tagView.addTag(t, false);
        }
    }
}
