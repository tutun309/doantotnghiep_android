package com.nmt.minhtu.doan.activity.booking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.managebooking.ManageBookingDialog;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.api.ResponsePOST;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Booking;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBookingActivity extends AppCompatActivity {
    LinearLayout txtStatusUser, spnStatusAdmin;
    TextView iconBack, txtId, txtNameUser, txtAddress, txtPlaceName, txtPrice, txtDateBooking, txtStatus;
    Button btnPayment, btnCancelTour;
    RadioGroup radioGroupStatus;
    RadioButton radioBtnNow, radioBtnFinish;
    Button btnManageBooking;


    Booking currentBooking = new Booking();
    int idVaitro = DataLocalManager.getUser().getVaitroId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking_v2);

        initView();
        initListener();
        /*initView();
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //--------------xu ly voi vai tro là admin -----------------
        if(idVaitro == 0){
            Intent intent = getIntent();
            int idCurrentBooking = intent.getIntExtra("currentBookingId", 1);
            setCurrentBooking(idCurrentBooking);
            btnPayment.setText("Lưu");
            //ẩn textview status
            txtStatusUser.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txtStatusUser.getLayoutParams();
            params.height = 0;
            params.width = 0;
            txtStatusUser.setLayoutParams(params);
            //ẩn button hủy chuyến
            btnCancelTour.setVisibility(View.INVISIBLE);

            btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String status = "" ;
                    int id = radioGroupStatus.getCheckedRadioButtonId();
                    if(id == R.id.radio_now){
                        currentBooking.setStatus(radioBtnNow.getText().toString());
                    } else if(id == R.id.radio_finish){
                        currentBooking.setStatus(radioBtnFinish.getText().toString());
                    }

                    ApiService.apiService.updateStatusBooking(currentBooking).enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            Toast.makeText(DetailBookingActivity.this,""+responsePOST.getMess(), Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            Toast.makeText(DetailBookingActivity.this,"Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


        }
        //--------------xu ly voi vai tro là user -----------------
        else if(idVaitro == 1){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spnStatusAdmin.getLayoutParams();
            params.height = 0;
            params.width = 0;
            spnStatusAdmin.setLayoutParams(params);
            spnStatusAdmin.setVisibility(View.INVISIBLE);
            Intent intent = getIntent();
            int idCurrentBooking = intent.getIntExtra("currentBookingId", 1);
            setCurrentBooking(idCurrentBooking);

            btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleDialogPayment();

                }
            });

            btnCancelTour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentBooking.setStatus("Đã hủy");
                    ApiService.apiService.updateStatusBooking(currentBooking).enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            Toast.makeText(DetailBookingActivity.this,"Hủy chuyến đi thành công", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            Toast.makeText(DetailBookingActivity.this,"Có lỗi xảy ra", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });


        }*/
    }

    private void initListener() {
        /*btnManageBooking.setOnClickListener(v -> {
            DialogFragment dialogFragment = ManageBookingDialog.newInstance();
            dialogFragment.show(getSupportFragmentManager(),"");
        });*/
    }

    private void handleDialogPayment() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_smile);


            Window window = dialog.getWindow();
            if(window == null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);


            Button btnSure = (Button) dialog.findViewById(R.id.btn_sure);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            TextView tvContent = (TextView) dialog.findViewById(R.id.tv_content);

            tvContent.setText("Bạn chắc chắn muốn thực hiện thanh toán chứ!");

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentBooking.setStatus("Đã thanh toán");
                    /*ApiService.apiService.updateStatusBooking(currentBooking).enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            Toast.makeText(DetailBookingActivity.this,"Thanh toán thành công", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            Toast.makeText(DetailBookingActivity.this,"Có lỗi xảy ra", Toast.LENGTH_LONG).show();

                        }
                    });*/

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView(){
        btnManageBooking = findViewById(R.id.btn_manage_booking);
        /*txtStatusUser = findViewById(R.id.txt_status_user);
        spnStatusAdmin = findViewById(R.id.spinner_status_admin);
        iconBack = findViewById(R.id.icon_back);
        txtId = findViewById(R.id.txt_id);
        txtNameUser = findViewById(R.id.txt_name_user);
        txtAddress = findViewById(R.id.txt_address_user);
        txtPlaceName = findViewById(R.id.txt_name_tour);
        txtPrice = findViewById(R.id.txt_price);
        txtDateBooking = findViewById(R.id.txt_date_booking);
        txtStatus = findViewById(R.id.txt_status);
        btnPayment = findViewById(R.id.btn_payment);
        btnCancelTour = findViewById(R.id.btn_cancel_tour);
        radioGroupStatus = findViewById(R.id.radio_group_status);
        radioBtnNow = findViewById(R.id.radio_now);
        radioBtnFinish = findViewById(R.id.radio_finish);*/

    }

    private void setCurrentBooking(int idBooking){
        ApiService.apiService.getBookingById(idBooking).enqueue(new Callback<Booking>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                currentBooking = response.body();
                try {
                    txtId.setText(String.valueOf(currentBooking.getId()));
                    txtNameUser.setText(currentBooking.getUser().getName());
                    txtAddress.setText(currentBooking.getUser().getAddress());
                    txtPlaceName.setText(currentBooking.getTour().getName());
                    txtPrice.setText("$"+currentBooking.getTour().getPrice());
                    txtDateBooking.setText(dateFormat.format(currentBooking.getDate()));
                    txtStatus.setText(currentBooking.getStatus());
                    if(currentBooking.getStatus().equals("Chờ thanh toán")){
                        btnCancelTour.setEnabled(true);
                        btnPayment.setEnabled(true);
                    } else{
                        if(idVaitro == 1){
                            btnPayment.setEnabled(false);
                            btnPayment.setBackgroundColor(R.color.color_bottom_nav);
                        } else{
                            btnPayment.setEnabled(true);
                        }
                        btnCancelTour.setEnabled(false);
                        btnCancelTour.setBackgroundColor(R.color.color_bottom_nav);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(DetailBookingActivity.this,"Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }
}