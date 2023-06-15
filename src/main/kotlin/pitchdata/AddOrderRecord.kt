package pitchdata

const val ADD_ORDER_TYPE = 'A'

data class AddOrderRecord(val orderId: String, val shares: Int, val symbol: String)
