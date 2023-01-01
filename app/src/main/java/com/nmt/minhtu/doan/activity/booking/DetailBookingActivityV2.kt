package com.nmt.minhtu.doan.activity.booking

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.nmt.minhtu.doan.ImgFromGrallery
import com.nmt.minhtu.doan.R
import com.nmt.minhtu.doan.activity.managebooking.ManageBookingDialog.Companion.newInstance
import com.nmt.minhtu.doan.api.ApiService
import com.nmt.minhtu.doan.model.Booking
import com.nmt.minhtu.doan.model.TicketBooking
import com.nmt.minhtu.doan.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_booking_v2.*
import kotlinx.android.synthetic.main.activity_detail_booking_v2.icon_back
import kotlinx.android.synthetic.main.activity_detail_booking_v2.img_booked_tour
import kotlinx.android.synthetic.main.activity_detail_booking_v2.tv_email
import kotlinx.android.synthetic.main.activity_detail_booking_v2.tv_name
import kotlinx.android.synthetic.main.activity_detail_booking_v2.tv_phone
import kotlinx.android.synthetic.main.activity_detail_booking_v2.tv_time_start
import kotlinx.android.synthetic.main.activity_detail_booking_v2.tv_total_price
import kotlinx.android.synthetic.main.activity_detail_booking_v2.txt_nameBookedTour
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookingActivityV2 : AppCompatActivity() {
    private var booking: Booking? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_booking_v2)

        initListener()
        loadData()
    }

    private fun loadData() {
        getBookingInfo()
        getTicketBooking()
    }

    private fun getBookingInfo() {
        val bookingId = intent.getIntExtra("currentBookingId", 1)
        ApiService.apiService.getBookingById(bookingId).enqueue(object : Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                booking = response.body()
                booking?.let {
                    tv_name.text = it.user.name
                    tv_email.text = it.user.email
                    tv_phone.text = it.user.phoneNumber
                    tv_time_start.text = it.timeBooking
                    tv_status.text = it.bookingStatus
                    tv_total_price.text = "${Utils.formatMoney(it.priceTotal)} VND"

                    img_booked_tour.setImageBitmap(ImgFromGrallery.deCodeToBase64(it.tour.img))
                    txt_nameBookedTour.text = it.tour.name
                    tv_periodTime.text = "Lịch trình: ${it.tour.periodTime}"
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {

            }

        })
    }

    private fun getTicketBooking() {
        val bookingId = intent.getIntExtra("currentBookingId", 1)
        ApiService.apiService.getTicketBookingByBookingId(bookingId).enqueue(object : Callback<TicketBooking>{
            override fun onResponse(call: Call<TicketBooking>, response: Response<TicketBooking>) {
                response.body()?.let{
                    val ticketBooking = it
                    val tvAdult =
                        if (ticketBooking.adultAmount != 0) "${ticketBooking.adultAmount} x vé tiêu chuẩn " else ""
                    val tvChildren =
                        if (ticketBooking.childrenAmount != 0) "\n${ticketBooking.childrenAmount} x vé trẻ em " else ""
                    tv_ticket_amount.text = tvAdult + tvChildren
                }
            }

            override fun onFailure(call: Call<TicketBooking>, t: Throwable) {
                Toast.makeText(this@DetailBookingActivityV2, "Call API loi", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initListener() {
        btn_manage_booking.setOnClickListener(View.OnClickListener { v: View? ->
            booking?.let {
                val dialogFragment: DialogFragment =
                    newInstance(it)
                dialogFragment.show(supportFragmentManager, "")
            }
        })

        icon_back.setOnClickListener { finish() }
    }


}