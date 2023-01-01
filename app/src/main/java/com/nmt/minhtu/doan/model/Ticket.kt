package com.nmt.minhtu.doan.model

data class Ticket(
    var id: Int? = 0,
    var adultPrice: Int? = 0,
    var childrenPrice: Int? = 0,
    var tourId: Int? = 0
) {
    fun getTicketModel(): TicketModel {
        return TicketModel(
            id ?: -1,
            adultPrice ?: 0,
            childrenPrice ?: 0,
            tourId ?: -1
        )
    }
}

data class TicketModel(
    var id: Int = 0,
    var adultPrice: Int = 0,
    var childrenPrice: Int = 0,
    var tourId: Int = 0,
)