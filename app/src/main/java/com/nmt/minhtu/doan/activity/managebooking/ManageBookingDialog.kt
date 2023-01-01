package com.nmt.minhtu.doan.activity.managebooking

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.nmt.minhtu.doan.ImgFromGrallery
import com.nmt.minhtu.doan.R
import com.nmt.minhtu.doan.api.ApiService
import com.nmt.minhtu.doan.api.BaseResponse
import com.nmt.minhtu.doan.api.BaseResponsePost
import com.nmt.minhtu.doan.model.Booking
import com.nmt.minhtu.doan.model.Comment
import com.nmt.minhtu.doan.model.Payment
import kotlinx.android.synthetic.main.dialog_manage_booking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ManageBookingDialog(private val booking: Booking): DialogFragment() {

    private var isShowPayment = false
    private var isShowComment = false
    private var isShowCancel = false
    private var imgPaymentBitmap: Bitmap? = null

    companion object {
        @JvmStatic
        fun newInstance(booking: Booking) = ManageBookingDialog(booking)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_manage_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        loadData()
    }

    private fun loadData() {
        getPaymentInfo()
        getCommentInfo()
    }

    private fun getCommentInfo() {
        ApiService.apiService.getCommentByBookingId(booking.id).enqueue(object : Callback<BaseResponsePost<Comment>>{
            override fun onResponse(
                call: Call<BaseResponsePost<Comment>>,
                response: Response<BaseResponsePost<Comment>>
            ) {
                response.body()?.let {
                    if(it.isSuccess) {
                        it.data?.let { comment ->
                            tv_rating.rating = comment.rating
                            tv_content.setText(comment.content)
                            tv_rating.isEnabled = false
                            tv_content.isEnabled = false
                            btn_comment.apply {
                                isClickable = false
                                setBackgroundResource(R.drawable.bg_button_gray)
                            }
                        }

                    } else {
                        gr_comment.isClickable = true
                        btn_comment.apply {
                            isClickable = true
                            setBackgroundResource(R.drawable.custom_button)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponsePost<Comment>>, t: Throwable) {
                Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getPaymentInfo() {
        ApiService.apiService.getPaymentByBookingId(booking.id).enqueue(object : Callback<BaseResponsePost<Payment>> {
            override fun onResponse(
                call: Call<BaseResponsePost<Payment>>,
                response: Response<BaseResponsePost<Payment>>
            ) {
                response.body()?.let {
                    if(it.isSuccess) {
                        it.data?.let { payment ->
                            img_payment.isClickable = false
                            img_payment.setImageBitmap(ImgFromGrallery.deCodeToBase64(payment.imgPayment))
                        }

                    } else {
                        img_payment.isClickable = true
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponsePost<Payment>>, t: Throwable) {
                Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initListener() {
        line_payment.setOnClickListener {
            isShowPayment = !isShowPayment
            gr_payment.visibility = if(isShowPayment) {
                ic_next_payment.rotation = 90f
                View.VISIBLE
            } else {
                ic_next_payment.rotation = 0f
                View.GONE
            }
        }

        tv_content.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                btn_comment.isClickable = if(tv_content.text.isNotEmpty()) {
                    btn_comment.setBackgroundResource(R.drawable.custom_button)
                    true
                } else {
                    btn_comment.setBackgroundResource(R.drawable.bg_button_gray)
                    false
                }
            }

        })

        line_comment.setOnClickListener {
            isShowComment = !isShowComment
            gr_comment.visibility = if(isShowComment) {
                ic_next_comment.rotation = 90f
                View.VISIBLE
            } else {
                ic_next_comment.rotation = 0f
                View.GONE
            }
        }

        line_cancel.setOnClickListener {
            isShowCancel = !isShowCancel
            gr_cancel.visibility = if(isShowCancel) {
                ic_next_cancel.rotation = 90f
                View.VISIBLE
            } else {
                ic_next_cancel.rotation = 0f
                View.GONE
            }
        }

        img_payment.setOnClickListener { takePhotoPayment() }

        btn_payment.setOnClickListener { createPayment() }

        btn_comment.setOnClickListener {
            if(booking.statusId == 3) {
                createComment()
            } else {
                Toast.makeText(requireContext(), "Để thực hiện đánh giá, bạn phải hoàn thành chuyến đi", Toast.LENGTH_LONG).show()
            }
        }

        btn_cancel.setOnClickListener { cancelBooking() }
    }

    private fun cancelBooking() {
        when(booking.statusId) {
            1 -> {
                // huy
                updateStatusBooking(5, "Hủy chuyến đi thành công")
            }
            2 -> {
                // cho xacs nhan
                updateStatusBooking(4, "Đã gửi yêu cầu hủy chuyến, chờ xác nhận")
            }
            3 -> {
                Toast.makeText(requireContext(), "Bạn không thể hủy chuyến đi khi đã hoàn thành!", Toast.LENGTH_LONG).show()
            }
            4 -> {
                Toast.makeText(requireContext(), "Bạn đã gửi yêu cầu hủy chuyến đi này!", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Bạn đã hủy chuyến đi này!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateStatusBooking(statusId: Int, mess: String) {
        booking.statusId = statusId
        ApiService.apiService.updateStatusBooking(booking).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                response.body()?.let {
                    if(it.isSuccess) {
                        Toast.makeText(requireContext(), mess, Toast.LENGTH_LONG).show()
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

    private fun createComment() {
        val comment = Comment(
            userId = booking.user.id,
            bookingId = booking.id,
            rating = tv_rating.rating,
            content = tv_content.text.toString(),
            tourId  = booking.tour.id
        )
        ApiService.apiService.createComment(comment).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                response.body()?.let {
                    if(it.isSuccess) {
                        Toast.makeText(requireContext(), "Lưu thông tin đánh giá thành công!", Toast.LENGTH_LONG).show()
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

    private fun createPayment() {
        imgPaymentBitmap?.let {
            val payment = Payment(
                imgPayment = ImgFromGrallery.endCodeImg(imgPaymentBitmap),
                bookingId = booking.id
            )
            ApiService.apiService.createPayment(payment).enqueue(object : Callback<BaseResponse>{
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    response.body()?.let {
                        if(it.isSuccess) {
                            Toast.makeText(requireContext(), "Gửi thông tin thanh toán thành công. Chờ xác nhận!", Toast.LENGTH_LONG).show()
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

    }

    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data ?: return@ActivityResultCallback
                val uri = data.data
                try {
                    imgPaymentBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    img_payment.setImageBitmap(imgPaymentBitmap)
                    btn_payment.isClickable = if(imgPaymentBitmap != null) {
                        btn_payment.setBackgroundResource(R.drawable.custom_button)
                        true
                    } else {
                        btn_payment.setBackgroundResource(R.drawable.bg_button_gray)
                        false
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    )
    private fun takePhotoPayment() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}