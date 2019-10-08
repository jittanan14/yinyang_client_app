package com.example.yinyang_taengkwa.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout date_birth;
    TextView text_view_birth;
    TextView element_name;
    TextView yhinyhang;
    ImageView profile;
    Button back_login;
    TextInputLayout Text_Email;
    TextInputLayout Pass_word;
    TextInputLayout confirm_pass;
    TextInputLayout name_edit;
    RadioButton radio_men_button;
    RadioButton radio_women_button;
    RadioButton radioButton;
    TextView text_birth;
    TextView text_element;
    TextView text_body;
    RadioGroup radioGroup_gender;
    CircleImageView pic_profile;

    ArrayList<String> item = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    TextView food_lose;
    Button okay;
    String string_element;
    int e;

    CircleImageView CircleImageViewProfile;
    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;

    private String image_user = "";

    String TAG = "register";

    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^" +
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        initItems();
        spinnerDialog = new SpinnerDialog(RegisterActivity.this,item,"กรุณาเลือกวัตถุดิบที่แพ้");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

               food_lose.setText(item);
            }
        });


       food_lose = (TextView)findViewById(R.id.food_lose_textview);
       food_lose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               spinnerDialog.showSpinerDialog();
           }
       });

        date_birth = findViewById(R.id.date_birth);
        text_view_birth = findViewById(R.id.text_date);
        element_name = findViewById(R.id.element);
        yhinyhang = findViewById(R.id.body);
        food_lose = findViewById(R.id.food_lose_textview);
        back_login = findViewById(R.id.button_back_login);
        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        CircleImageViewProfile = findViewById(R.id.pic_user);

        // user
        Text_Email = findViewById(R.id.TextEmail);
        Pass_word = findViewById(R.id.Password);
        confirm_pass = findViewById(R.id.confirm_pass);
        name_edit = findViewById(R.id.Text_name);
        radio_men_button = findViewById(R.id.radio_men);
        radio_women_button = findViewById(R.id.radio_women);
        text_birth = findViewById(R.id.text_date);
        text_element = findViewById(R.id.element);
        text_body = findViewById(R.id.body);
        food_lose = findViewById(R.id.food_lose_textview);
        pic_profile = findViewById(R.id.pic_user);

        pic_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
            }
        });
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        //Add data_member to database server
        okay = findViewById(R.id.submit_button);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("ยืนยันข้อมูลส่วนตัวของคุณหรือไม่");
                //set cancelable
                builder.setCancelable(true);
                //set positive / yes button

                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSignUp();
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //create alert dialog
                AlertDialog alertdialog = builder.create();
                //show alert dialog
                alertdialog.show();

            }
        });


        //Email
        Text_Email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override

            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    Text_Email.setError("กรุณากรอกอีเมล");
                    Text_Email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    Text_Email.setError("รูปแบบอีเมลไม่ถูกต้อง");
                    Text_Email.requestFocus();
                } else {
                    Text_Email.setError(null);
                }

            }
        });

        //Password
        Pass_word.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    Pass_word.setError("กรุณากรอกรหัสผ่าน");
                    Pass_word.requestFocus();
                } else if (!PASSWORD_PATTERN.matcher(s.toString()).matches()) {
                    Pass_word.setError("รหัสผ่านมีความยาวน้อยกว่า 6 ตัวอักษร");
                    Pass_word.requestFocus();
                } else {
                    Pass_word.setError(null);
                }
            }


        });

        //Confirmpassword
        confirm_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    confirm_pass.setError("กรุณากรอกรหัสผ่านอีกครั้ง");
                    confirm_pass.requestFocus();
                } else if (!PASSWORD_PATTERN.matcher(s.toString()).matches()) {
                    confirm_pass.setError("รหัสผ่านต้องมีความยาวน้อยกว่า 6 ตัวอักษร");
                    confirm_pass.requestFocus();
                } else if (!(s.toString().equals(confirm_pass.getEditText().getText().toString().trim()))) {
                    confirm_pass.setError("รหัสผ่านไม่ถูกต้อง");
                    confirm_pass.requestFocus();
                } else if (s.toString().equals(confirm_pass.getEditText().getText().toString().trim())) {
                    confirm_pass.setError(null);
                }
            }
        });

        //username
        name_edit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    name_edit.setError("กรุณากรอกชื่อผู้ใช้");
                    name_edit.requestFocus();
                } else if (s.toString().length() > 50) {
                    name_edit.setError("ชื่อจริงมีความยาวเกินกำหนด");
                    name_edit.requestFocus();
                } else {
                    name_edit.setError(null);
                }
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //calendar birthday
        date_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void initItems(){
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

    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        final Date today = new Date();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        c.setTime(today);
        c.add( Calendar.YEAR, -10  ); // Subtract 6 months
        long maxDate = c.getTime().getTime(); // Twice!
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        int y1 = 0;
        String m1;
        String date;

        if (String.valueOf(month).length() == 1) {
            date = (String.valueOf(dayOfMonth) + " " + "0" + String.valueOf(month) + " " + String.valueOf(year + 543));
            m1 = "0" + String.valueOf(month);
        } else {
            date = (String.valueOf(dayOfMonth) + " " + String.valueOf(month) + " " + String.valueOf(year + 543));
            m1 = String.valueOf(month);
        }
        y1 = (year + 543);
        text_view_birth.setText(date);

        Element_Cal(y1);
    }

    public void Element_Cal(int y1) {

        int element = 0;
        int element_full = y1;
        int element_sum = element_full % 10;

        int element_two = y1 % 100;

        if (element_sum == 6) {
            element = 6;
        } else if (element_sum < 6) {
            if ((element_full % 100) <= 5) {
                element_sum = (element_sum + 10) - 6;
                element = element_sum;
                if (element > 6) {
                    element = element - 6;
                }

            } else if ((element_full % 100) >= 10 && (element_full % 100) <= 99) {
                while (element_two > 6) {
                    element_two -= 6;
                    element = element_two;
                }
            }
        } else if (element_sum > 6) {
            element = element_sum - 6;
            Log.e("number ", Integer.toString(element));
        }


        if (element == 1) {
            string_element = "ธาตุไม้";
            element_name.setText("ธาตุไม้");

        } else if (element == 2) {
            string_element = "ธาตุดิน";
            element_name.setText("ธาตุดิน");

        } else if (element == 3) {
            string_element = "ธาตุไฟ";
            element_name.setText("ธาตุไฟ");

        } else if (element == 4) {
            string_element = "ธาตุน้ำ";
            element_name.setText("ธาตุน้ำ");

        } else if (element == 5) {
            string_element = "ธาตุดิน";
            element_name.setText("ธาตุดิน");

        } else if (element == 6) {
            string_element = "ธาตุโลหะ(ทอง)";
            element_name.setText("ธาตุโลหะ(ทอง)");

        }
        e = element;
        yhinORyhang(element);
    }

    public String yhinORyhang(int element) {

        if (element == 1 || element == 9) {
            yhinyhang.setText("หยิน");
            return "หยิน";
        } else if (element == 2 || element == 8) {
            yhinyhang.setText("หยาง");
            return "หยาง";
        } else if (element == 3) {
            yhinyhang.setText("หยิน");
            return "หยิน";
        } else if (element == 4) {
            yhinyhang.setText("หยิน");
            return "หยิน";
        } else if (element == 5) {
            yhinyhang.setText("หยาง");
            return "หยาง";
        } else if (element == 6 || element == 7) {
            yhinyhang.setText("หยาง");
            return "หยาง";
        }
        return "";
    }

    private boolean validateEmail() {
        String emailInput = Text_Email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return false;
        } else {
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = Pass_word.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String ConfirmPasswordInput = confirm_pass.getEditText().getText().toString().trim();

        if (ConfirmPasswordInput.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(ConfirmPasswordInput).matches()) {
            return false;
        } else if (!(ConfirmPasswordInput.equals(Pass_word.getEditText().getText().toString().trim()))) {
            Toast.makeText(this, "รหัสผ่านไม่ตรงกัน",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateUsername() {
        String UsernameInput = name_edit.getEditText().getText().toString().trim();

        if (UsernameInput.isEmpty()) {
            return false;
        } else if (UsernameInput.length() > 50) {
            return false;
        } else {
            return true;
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup_gender.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        Toast.makeText(this, "กรุณาระบุเพศ" + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }

    private boolean validateBD() {
        String birthday = text_birth.getText().toString().trim();
        if (birthday.equals("เลือกวันเกิด")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFoodLose() {
        String FoodLose = food_lose.getText().toString().trim();
        if (FoodLose.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


    private boolean validationError() {
        if (!validateEmail() || !validatePassword() || !validateConfirmPassword() || !validateUsername() || !validateBD() || image_user.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String CheckGender() {

        if (radio_men_button.isChecked()) {
            return "m";
        } else {
            return "f";
        }
    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }
            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_user = imageToString(bitmap);
                CircleImageViewProfile.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(RegisterActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void CropImage(Uri uri) {
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {
        }
    }


    private void userSignUp() {
        //System.out.println("รูป : " + image_user);
        if (!validationError()) {
            Toast.makeText(this, "กรอกข้อมูลไม่ครบ", Toast.LENGTH_LONG).show();
            return;
        } else if (food_lose.getText().toString().isEmpty()) {
            Toast.makeText(this, "กรุณาระบุวัตถุดิบอาหารที่แพ้", Toast.LENGTH_SHORT).show();
        } else {
            sendToServer();
        }
    }

    private void sendToServer() {
        String email = Text_Email.getEditText().getText().toString().trim();
        String password = Pass_word.getEditText().getText().toString().trim();
        String username = name_edit.getEditText().getText().toString().trim();
        String gender = CheckGender();
        String birthday = text_birth.getText().toString();
        String food = food_lose.getText().toString();


        Log.e("Email", email);
        Log.e("Pass", password);
        Log.e("User", username);
        Log.e("Gender", gender);
        Log.e("Birthday", birthday);
        Log.e("Food", food);
        Log.e("Element", string_element);
        Log.e("YY", yhinORyhang(e));


        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUser(email, password, username, gender, birthday, string_element, food, image_user, yhinORyhang(e), 0, 0);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body().isStatus()) {
                    Toast.makeText(RegisterActivity.this, "สมัครสมาชิกสำเร็จ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e("Sign up", t.getMessage());
            }
        });
    }

}