package pitch

const val ADD_ORDER_TYPE = 'A'
const val ADD_ORDER_SHARES_OFFSET = 23

data class AddOrderRecord(val orderId: String, val shares: Int, val symbol: String)
