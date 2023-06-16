package pitch

const val TRADE_RECORD_TYPE = 'P'
const val TRADE_RECORD_SHARES_OFFSET = 23

data class TradeRecord(val orderId: String, val shares: Int, val symbol: String)
