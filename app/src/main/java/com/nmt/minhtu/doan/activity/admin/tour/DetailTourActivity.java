package com.nmt.minhtu.doan.activity.admin.tour;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.ListTourActivity;
import com.nmt.minhtu.doan.activity.booking.ConfirmInfoBookingDialog;
import com.nmt.minhtu.doan.adapter.AdminListTourAdapter;
import com.nmt.minhtu.doan.adapter.CommentAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.api.BaseResponse;
import com.nmt.minhtu.doan.api.BaseResponsePost;
import com.nmt.minhtu.doan.api.ResponsePOST;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Booking;
import com.nmt.minhtu.doan.model.Comment;
import com.nmt.minhtu.doan.model.Favorite;
import com.nmt.minhtu.doan.model.Ticket;
import com.nmt.minhtu.doan.model.TicketBooking;
import com.nmt.minhtu.doan.model.TicketModel;
import com.nmt.minhtu.doan.model.Tour;
import com.nmt.minhtu.doan.model.User;
import com.nmt.minhtu.doan.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTourActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView iconBack, txtName, txtCategory, txtPeriodTime, txtDesc, iconFavorite, tvDate, tvComment;
    private TextView tvPriceAdult, tvPriceChildren, tvPlusAdult, tvMinusAdult, tvPlusChildren, tvMinusChildren, tvTotalPrice, tvNumberTicketAdult, tvNumberTicketChildren;
    private ImageView imgTour;
    private ImageView btnFavorite;
    private Button btnBooking;
    private RelativeLayout grPickDate;
    private RecyclerView rcvComment;

    private Tour tour;
    private TicketModel ticket;
    private String timeStart = "", timeBooking = "";
    private int numberTicketAdult = 0, numberTicketChildren = 0, priceTotal = 0;
    private List<Comment> comments = new ArrayList<>();
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour);
        showProgress();
        initView();
        setupView();
        loadData();
        initListener();

    }

    private void setupView() {
        setCurrentDate();
    }

    private void loadData() {
        setCurrentTour();
        getTicketPrice();
        getComment();
        checkIsFavorite();
    }

    private void getComment() {
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("currentTourId", 1);
        ApiService.apiService.getCommentByBookingTourId(tourId).enqueue(new Callback<BaseResponsePost<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseResponsePost<List<Comment>>> call, Response<BaseResponsePost<List<Comment>>> response) {
                assert response.body() != null;
                if(!response.body().getData().isEmpty()) {
                    comments = response.body().getData();
                    mapUserToComment();
                    tvComment.setVisibility(View.VISIBLE);
                } else {
                    tvComment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponsePost<List<Comment>>> call, Throwable t) {

            }
        });
    }

    private void mapUserToComment() {
        ApiService.apiService.getListUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                assert response.body() != null;
                if(!response.body().isEmpty()) {
                    for(User item : response.body()) {
                        for(Comment comment: comments) {
                            if(comment.getUserId() == item.getId()) {
                                comment.setImgUser(item.getImg());
                                comment.setUserName(item.getName());
                            }
                        }
                    }
                    CommentAdapter adapter = new CommentAdapter(comments);
                    rcvComment.setAdapter(adapter);
                    rcvComment.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentDate() {
        long currentMillis = System.currentTimeMillis();
        timeStart = Utils.INSTANCE.convertMillisToDate(currentMillis + 24 * 60 * 60 * 1000 * 2);
        tvDate.setText("Ngày " + timeStart);
    }

    @SuppressLint("SetTextI18n")
    private void initListener() {
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.INSTANCE.isLogin()) {
                   // (User user, Tour tour, String timeStart, String timeBooking, int statusId, int priceTotal, TicketBooking ticketBooking)
                    Booking booking = new Booking(
                            DataLocalManager.getUser(),
                            tour,
                            timeStart,
                            Utils.INSTANCE.convertMillisToDate(System.currentTimeMillis()),
                            1,
                            priceTotal,
                            new TicketBooking(null, numberTicketAdult, numberTicketChildren, null)
                    );
                    Log.d("TAG", "onClick: " + booking);
                    DialogFragment dialog = ConfirmInfoBookingDialog.newInstance(booking);
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    Toast.makeText(DetailTourActivity.this, "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnFavorite.setOnClickListener(v -> {
            if (Utils.INSTANCE.isLogin()) {
                if (isFavorite) {
                    cancelFavorite();
                } else {
                    saveFavoriteTour();
                }
            } else {
                Toast.makeText(DetailTourActivity.this, "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
            }

        });

        tvPlusAdult.setOnClickListener(v -> {
            if (numberTicketAdult >= 0) {
                numberTicketAdult++;
                tvNumberTicketAdult.setText("" + numberTicketAdult);
                updateTotalPrice();
            }
        });
        tvMinusAdult.setOnClickListener(v -> {
            if (numberTicketAdult > 0) {
                numberTicketAdult--;
                tvNumberTicketAdult.setText("" + numberTicketAdult);
                updateTotalPrice();
            }
        });
        tvPlusChildren.setOnClickListener(v -> {
            if (numberTicketChildren >= 0) {
                numberTicketChildren++;
                tvNumberTicketChildren.setText("" + numberTicketChildren);
                updateTotalPrice();
            }
        });
        tvMinusChildren.setOnClickListener(v -> {
            if (numberTicketChildren > 0) {
                numberTicketChildren--;
                tvNumberTicketChildren.setText("" + numberTicketChildren);
                updateTotalPrice();
            }
        });

        grPickDate.setOnClickListener(v -> {
            showPickDateDialog();
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalPrice() {
        if (ticket != null) {
            priceTotal = numberTicketAdult * ticket.getAdultPrice() + numberTicketChildren * ticket.getChildrenPrice();
            tvTotalPrice.setText("" + Utils.INSTANCE.formatMoney(priceTotal) + " VND");
            if (priceTotal > 0) {
                btnBooking.setEnabled(true);
                btnBooking.setBackgroundResource(R.drawable.custom_button);
            } else {
                btnBooking.setEnabled(false);
                btnBooking.setBackgroundResource(R.drawable.bg_button_gray);
            }
        }
    }

    private void cancelFavorite() {
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("currentTourId", 1);
        int userId = DataLocalManager.getUser().getId();
        ApiService.apiService.cancelFavorite(userId, tourId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        isFavorite = false;
                        btnFavorite.setColorFilter(Color.parseColor("#5A5858"));
                        Toast.makeText(DetailTourActivity.this, "Đã xóa địa điểm này khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailTourActivity.this, response.body().getMess(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(DetailTourActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveFavoriteTour() {
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("currentTourId", 1);
        Tour tour = new Tour(tourId);
        User user = DataLocalManager.getUser();
        if (user != null) {
            Favorite favorite = new Favorite(user, tour);
            ApiService.apiService.saveFavoriteTour(favorite).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            isFavorite = true;
                            btnFavorite.setColorFilter(Color.parseColor("#D82020"));
                            Toast.makeText(DetailTourActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailTourActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(DetailTourActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setCurrentTour() {
        Intent intent = getIntent();
        int idTour = intent.getIntExtra("currentTourId", 1);
        ApiService.apiService.getTourById(idTour).enqueue(new Callback<Tour>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Tour> call, Response<Tour> response) {
                tour = response.body();
                if (tour != null) {
                    imgTour.setImageBitmap(ImgFromGrallery.deCodeToBase64(tour.getImg()));
                    txtName.setText(tour.getName());
                    txtCategory.setText("Thành phố: " + tour.getCategory().getName());
                    txtPeriodTime.setText("" + tour.getPeriodTime());
                    txtDesc.setText(tour.getDesc());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Tour> call, Throwable t) {
                Toast.makeText(DetailTourActivity.this, "call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTicketPrice() {
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("currentTourId", 1);
        ApiService.apiService.getTicketByTourId(tourId).enqueue(new Callback<Ticket>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.body() != null) {
                    ticket = response.body().getTicketModel();
                    tvPriceAdult.setText("" + Utils.INSTANCE.formatMoney(ticket.getAdultPrice()) + " VND");
                    tvPriceChildren.setText("" + Utils.INSTANCE.formatMoney(ticket.getChildrenPrice()) + " VND");
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Toast.makeText(DetailTourActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkIsFavorite() {
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("currentTourId", 1);
        User user = DataLocalManager.getUser();
        if (user != null) {
            int userId = DataLocalManager.getUser().getId();
            ApiService.apiService.checkIsFavorite(userId, tourId).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            isFavorite = true;
                            btnFavorite.setColorFilter(Color.parseColor("#D82020"));
                        } else {
                            isFavorite = false;
                            btnFavorite.setColorFilter(Color.parseColor("#5A5858"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(DetailTourActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void showPickDateDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        timeStart = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvDate.setText("Ngày " + timeStart);

                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000 * 2);
        dpd.show();
    }

    private void handleDialog() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_smile);


            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);


            Button btnSure = (Button) dialog.findViewById(R.id.btn_sure);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

            // xu ly nhan nut sure
            /*btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = DataLocalManager.getUser();
                    Date date = new Date();
                    java.sql.Date currentDate = new java.sql.Date(date.getTime());
                    Booking booking = new Booking(user, tour, currentDate, "Chờ thanh toán");
                    ApiService.apiService.createBooking(booking).enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            if (response.isSuccessful()) {
                                ResponsePOST responsePOST = response.body();
                                if (responsePOST.getErrCode() == 0) {
                                    Toast.makeText(DetailTourActivity.this, "Đặt tour thành công! Hãy vào lịch sử để xem chi tiết", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            Toast.makeText(DetailTourActivity.this, "call API loi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
*/
            //---nut cancel---
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Xin đợi giây lát...!");
        progressDialog.show();
    }

    private void initView() {
        txtCategory = findViewById(R.id.txt_category_detail_tour);
        txtName = findViewById(R.id.txt_name_detalTour);
        txtPeriodTime = findViewById(R.id.tv_periodTime);
        txtDesc = findViewById(R.id.txt_desc_detail_tour);
        imgTour = findViewById(R.id.img_detail_tour);
        btnBooking = findViewById(R.id.btn_booking);
        iconBack = findViewById(R.id.icon_back);
        btnFavorite = findViewById(R.id.btn_favorite);
        tvPriceAdult = findViewById(R.id.tv_price_adult);
        tvPriceChildren = findViewById(R.id.tv_price_children);
        tvPlusAdult = findViewById(R.id.tv_plus);
        tvMinusAdult = findViewById(R.id.tv_minus);
        tvPlusChildren = findViewById(R.id.tv_plus_children);
        tvMinusChildren = findViewById(R.id.tv_minus_childen);
        tvTotalPrice = findViewById(R.id.tv_total_money);
        tvNumberTicketAdult = findViewById(R.id.tv_number_ticket_adult);
        tvNumberTicketChildren = findViewById(R.id.tv_number_ticket_children);
        grPickDate = findViewById(R.id.gr_pick_date);
        tvDate = findViewById(R.id.tv_date);
        rcvComment = findViewById(R.id.rcv_comment);
        tvComment = findViewById(R.id.tv_comment);
    }
}