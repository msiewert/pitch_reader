package pitchdata

import br.com.guiabolso.fixedlengthfilehandler.multiFixedLengthFileParser
import java.util.stream.Collectors

const val MESSAGE_TYPE_OFFSET = 9
const val ORDER_ID_OFFSET = 10
const val ORDER_ID_LENGTH = 12

const val SYMBOL_OFFSET = 29
const val SYMBOL_LENGTH = 6

class PitchParser(private val data: Sequence<String>) {
    fun parse(): List<Any> {
        //TODO: convert sequence to input stream without needing to join as one string
        val inputStream = data.toList().stream().collect(Collectors.joining("\n")).byteInputStream()
        val records = multiFixedLengthFileParser(
            inputStream
        ) {
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == CANCEL_ORDER_TYPE }) {
                CancelOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(22, 28),
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == ADD_ORDER_TYPE }) {
                AddOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(23, 29),
                    symbol = field<String>(SYMBOL_OFFSET, SYMBOL_OFFSET + SYMBOL_LENGTH)
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == EXECUTE_ORDER_TYPE }) {
                ExecuteOrderRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    shares = field(22, 28),
                )
            }
            withRecord({ line -> line[MESSAGE_TYPE_OFFSET] == TRADE_RECORD_TYPE }) {
                TradeRecord(
                    orderId = field(ORDER_ID_OFFSET, ORDER_ID_OFFSET + ORDER_ID_LENGTH),
                    symbol = field<String>(SYMBOL_OFFSET, SYMBOL_OFFSET + SYMBOL_LENGTH),
                    shares = field(23, 29),
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
