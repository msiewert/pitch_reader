package pitch

const val EXECUTE_ORDER_TYPE = 'E'

data class ExecuteOrderRecord(val orderId: String, val shares: Int)
