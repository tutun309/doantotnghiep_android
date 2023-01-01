package com.nmt.minhtu.doan.activity.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.nmt.minhtu.doan.ImgFromGrallery
import com.nmt.minhtu.doan.R
import com.nmt.minhtu.doan.api.ApiService
import com.nmt.minhtu.doan.api.BaseResponse
import com.nmt.minhtu.doan.api.BaseResponsePost
import com.nmt.minhtu.doan.model.Booking
import com.nmt.minhtu.doan.model.TicketBooking
import com.nmt.minhtu.doan.model.Tour
import com.nmt.minhtu.doan.model.User
import com.nmt.minhtu.doan.utils.Utils
import kotlinx.android.synthetic.main.dialog_confirm_info_booking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class ConfirmInfoBookingDialog(private val booking: Booking) : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(booking: Booking) = ConfirmInfoBookingDialog(booking)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_confirm_info_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initListener()
    }

    private fun initListener() {
        cb_condition.setOnClickListener {
            if(cb_condition.isChecked) {
                btn_booking.isEnabled = true
                btn_booking.setBackgroundResource(R.drawable.custom_button)
            } else {
                btn_booking.isEnabled = false
                btn_booking.setBackgroundResource(R.drawable.bg_button_gray)
            }
        }

        btn_booking.setOnClickListener {
            saveBooking()
           // saveTicketBooking()
        }
    }

    private fun saveTicketBooking(bookingId: Int) {
        booking.ticketBooking.bookingId = bookingId
        ApiService.apiService.createTicketBooking(booking.ticketBooking).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                response.body()?.let {
                    if(it.isSuccess) {
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), it.mess, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun saveBooking() {
        ApiService.apiService.createBooking(booking).enqueue(object : Callback<BaseResponsePost<Booking>>{
            override fun onResponse(call: Call<BaseResponsePost<Booking>>, response: Response<BaseResponsePost<Booking>>) {
                response.body()?.let {
                    if(it.isSuccess) {
                        Toast.makeText(requireContext(), "Đặt chuyến thành công! Vào lịch sử chuyến đi để xem chi tiết", Toast.LENGTH_LONG).show()
                        it.data.id?.let { id ->
                            saveTicketBooking(id)
                        }
                    } else {
                        Toast.makeText(requireContext(), it.mess, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponsePost<Booking>>, t: Throwable) {
                Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setupView() {
        val tour = booking.tour ?: Tour()
        img_booked_tour.setImageBitmap(ImgFromGrallery.deCodeToBase64(tour.img))
        txt_nameBookedTour.text = tour.name
        txt_periodTime.text = "Lịch trình: ${tour.periodTime}"

        val ticketBooking = booking.ticketBooking ?: TicketBooking()
        val tvAdult =
            if (ticketBooking.adultAmount != 0) "${ticketBooking.adultAmount} x vé tiêu chuẩn " else ""
        val tvChildren =
            if (ticketBooking.childrenAmount != 0) "\n${ticketBooking.childrenAmount} x vé trẻ em " else ""
        tv_ticket_amount.text = tvAdult + tvChildren

        val user = booking.user ?: User()
        tv_name.text = user.name
        tv_email.text = user.email
        tv_phone.text = user.phoneNumber
        tv_time_booking.text = booking.timeBooking
        tv_time_start.text = booking.timeStart
        tv_total_price.text = "${Utils.formatMoney(booking.priceTotal)} VND"

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}