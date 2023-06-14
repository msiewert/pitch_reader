package pitch

const val TRADE_RECORD_TYPE = 'P'

data class TradeRecord(val orderId: String, val shares: Int, val symbol: String)
