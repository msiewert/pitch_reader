package pitch

const val CANCEL_ORDER_TYPE = 'X'
const val CANCEL_ORDER_SHARES_OFFSET = 22

data class CancelOrderRecord(val orderId: String, val shares: Int)
