package pitchdata

const val CANCEL_ORDER_TYPE = 'X'

data class CancelOrderRecord(val orderId: String, val shares: Int)
