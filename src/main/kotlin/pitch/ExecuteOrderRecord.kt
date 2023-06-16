package pitch

const val EXECUTE_ORDER_TYPE = 'E'
const val EXECUTE_ORDER_SHARES_OFFSET = 22

data class ExecuteOrderRecord(val orderId: String, val shares: Int)
