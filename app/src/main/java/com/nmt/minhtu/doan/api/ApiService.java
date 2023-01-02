package com.nmt.minhtu.doan.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nmt.minhtu.doan.model.Booking;
import com.nmt.minhtu.doan.model.Category;
import com.nmt.minhtu.doan.model.Comment;
import com.nmt.minhtu.doan.model.Favorite;
import com.nmt.minhtu.doan.model.Payment;
import com.nmt.minhtu.doan.model.Ticket;
import com.nmt.minhtu.doan.model.TicketBooking;
import com.nmt.minhtu.doan.model.Tour;
import com.nmt.minhtu.doan.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.10:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);


    //-------------------user----------------
    @POST("/api/user/create-new-user")
    Call<ResponsePOST> postUser(@Body User user);

    @POST("/api/user/login")
    Call<ResponseLoginUser> postLoginUser(@Body User user);

    @GET("/api/user/get-all-user")
    Call<List<User>> getListUser();

    @GET("/api/user/get-user-by-id/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("/api/user/update-user")
    Call<ResponsePOST> updateUser(@Body User user);

    @DELETE("/api/user/delete-user/{id}")
    Call<ResponsePOST> deleteUserById(@Path("id") int id);


    //--------------------category-----------------
    @POST("/api/category/create-new-category")
    Call<ResponsePOST> postCategory(@Body Category category);

    @POST("/api/category/update-category")
    Call<ResponsePOST> updateCategory(@Body Category category);

    @GET("/api/category/get-all-category")
    Call<List<Category>> getListCategory();

    @GET("/api/category/get-category/{id}")
    Call<Category> getCategoryById(@Path("id") int id);

    @DELETE("/api/category/delete-category/{id}")
    Call<ResponsePOST> deleteCategoryById(@Path("id") int id);


    //-----------------------tour---------------
    @POST("/api/tour/create-new-tour")
    Call<ResponsePOST> createNewTour(@Body Tour tour);

    @POST("/api/tour/update-tour")
    Call<ResponsePOST> updateTour(@Body Tour tour);

    @GET("/api/tour/get-all-tour")
    Call<List<Tour>> getAllTour();

    @GET("/api/tour/get-tour-by-id/{id}")
    Call<Tour> getTourById(@Path("id") int id);

    @DELETE("/api/tour/delete-tour/{id}")
    Call<ResponsePOST> deleteTourById(@Path("id") int id);

    @GET("/api/tour/get-tour-by-categoryId/{categoryId}")
    Call<List<Tour>> getTourByCategoryId(@Path("categoryId") int categoryId);

    //---------------------booking------------
    @POST("/api/booking/create-new-booking")
    Call<BaseResponsePost<Booking>> createBooking(@Body Booking booking);

    @GET("/api/booking/get-all-booking")
    Call<List<Booking>> getAllBooking();

    @GET("/api/booking/get-booking-by-id/{id}")
    Call<Booking> getBookingById(@Path("id") int id);

    @POST("/api/booking/update-status-booking")
    Call<BaseResponse> updateStatusBooking(@Body Booking booking);

    @GET("/api/booking/get-booking-by-userid/{userId}")
    Call<List<Booking>> getBookingByUserId(@Path("userId") int userId);


    //-----------------favorite--------------------
    @GET("/api/favorite/check-is-favorite/{userId}/{tourId}")
    Call<BaseResponse> checkIsFavorite(@Path("userId") int userId, @Path("tourId") int tourId);

    @POST("/api/favorite/create-new-favorite")
    Call<BaseResponse> saveFavoriteTour(@Body Favorite favorite);

    @DELETE("/api/favorite/delete-favorite/{userId}/{tourId}")
    Call<BaseResponse> cancelFavorite(@Path("userId") int userId, @Path("tourId") int tourId);

    @GET("/api/favorite/get-favorite-by-userid/{userId}")
    Call<List<Favorite>> getListFavorite(@Path("userId") int userId);

    //-------------ticket---------------
    @GET("/api/ticket/get-ticket-by-tourId/{tourId}")
    Call<Ticket> getTicketByTourId(@Path("tourId") int tourId);

    //-------------ticketBooking---------------
    @POST("/api/ticketBooking/create-ticketBooking")
    Call<BaseResponse> createTicketBooking(@Body TicketBooking ticketBooking);

    @GET("/api/ticketBooking/get-ticketBooking-by-bookingId/{bookingId}")
    Call<TicketBooking> getTicketBookingByBookingId(@Path("bookingId") int bookingId);

    //------------------payment-------------
    @POST("/api/payment/create-payment")
    Call<BaseResponse> createPayment(@Body Payment payment);

    @GET("/api/payment/get-payment-by-bookingId/{bookingId}")
    Call<BaseResponsePost<Payment>> getPaymentByBookingId(@Path("bookingId") int bookingId);

    //------------------comment-------------
    @POST("/api/comment/create-comment")
    Call<BaseResponse> createComment(@Body Comment comment);

    @GET("/api/comment/get-comment-by-bookingId/{bookingId}")
    Call<BaseResponsePost<Comment>> getCommentByBookingId(@Path("bookingId") int bookingId);

    @GET("/api/comment/get-comment-by-tourId/{tourId}")
    Call<BaseResponsePost<List<Comment>>> getCommentByBookingTourId(@Path("tourId") int tourId);
}
