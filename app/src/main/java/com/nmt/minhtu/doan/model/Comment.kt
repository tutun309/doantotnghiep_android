package com.nmt.minhtu.doan.model

data class Comment(
   var id: Int = 0,
   var userId: Int = 0,
   var bookingId: Int = 0,
   var rating: Float = 0f,
   var content: String = "",
   var tourId: Int = 0,
   var imgUser: String = "",
   var userName: String = ""
)