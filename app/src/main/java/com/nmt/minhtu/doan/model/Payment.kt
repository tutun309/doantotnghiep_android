package com.nmt.minhtu.doan.model

data class Payment(
    var id: Int? = 0,
    var imgPayment: String? = "",
    var isConfirm: Boolean? = false,
    var bookingId: Int? = 0,
)