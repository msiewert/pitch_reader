package pitch

import br.com.guiabolso.fixedlengthfilehandler.multiFixedLengthFileParser
import java.util.stream.Collectors
import kotlin.streams.asStream

const val MESSAGE_TYPE_OFFSET = 9
const val ORDER_ID_OFFSET = 10
const val ORDER_ID_LENGTH = 12
const val SYMBOL_OFFSET = 29
const val SYMBOL_LENGTH = 6
const val SHARES_LENGTH = 6

class PitchParser(private val data: Sequence<String>) {
    fun parse(): List<Any> {
        val inputStream = data.asStream().collect(Collectors.joining("\n")).byteInputStream()
        val records = multiFixedLengthFileParser(
            inputStream
        ) {
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == CANCEL_ORDER_TYPE }) {
                CancelOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(CANCEL_ORDER_SHARES_OFFSET, CANCEL_ORDER_SHARES_OFFSET + SHARES_LENGTH),
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == ADD_ORDER_TYPE }) {
                AddOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(ADD_ORDER_SHARES_OFFSET, ADD_ORDER_SHARES_OFFSET + SHARES_LENGTH),
                    symbol = field<String>(SYMBOL_OFFSET, SYMBOL_OFFSET + SYMBOL_LENGTH)
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == EXECUTE_ORDER_TYPE }) {
                ExecuteOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(EXECUTE_ORDER_SHARES_OFFSET, EXECUTE_ORDER_SHARES_OFFSET + SHARES_LENGTH),
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == TRADE_RECORD_TYPE }) {
                TradeRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    symbol = field<String>(SYMBOL_OFFSET, SYMBOL_OFFSET + SYMBOL_LENGTH),
                    shares = field(TRADE_RECORD_SHARES_OFFSET, TRADE_RECORD_SHARES_OFFSET + SHARES_LENGTH),
                )
            }
            withRecord({ _ -> true }) {
                IgnoredRecord(
                    type = field(MESSAGE_TYPE_OFFSET, MESSAGE_TYPE_OFFSET + 1)
                )
            }
        }.toList()
        inputStream.close()
        return records
    }


}
