package pitchdata

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class PitchService(private val data: List<Any>) {

    private val orderMap = mutableMapOf<String, AddOrderRecord>()
    private val executedVolumes = mutableMapOf<String, Int>()

    fun processData(): Map<String, Int> {
        data.forEach { record ->
            when (record) {
                is AddOrderRecord -> {
                    if (orderMap.containsKey(record.orderId)) {
                        logger.info { "Duplicate order id ${record.orderId}" }
                    } else {
                        //println("Adding order ${record.orderId} for ${record.shares} shares of ${record.symbol}")
                        orderMap[record.orderId] = record
                    }

                }

                is ExecuteOrderRecord -> {
                    val order = orderMap[record.orderId]
                    if (order != null) {

                        if (record.shares > order.shares) {
                            logger.info { "Execute - Order id ${record.orderId} for ${record.shares} shares of ${order.symbol} is greater than the order shares ${order.shares}." }
                        }

                        executedVolumes[order.symbol] = executedVolumes.getOrDefault(order.symbol, 0) + record.shares
                        //println("Execute - Order id ${record.orderId} for ${record.shares} shares of ${order.symbol}.")
                    } else {
                        logger.info { "Execute - Order id ${record.orderId} not found." }
                    }
                }

                is CancelOrderRecord -> {
                    val order = orderMap[record.orderId]
                    if (order != null) {
                        //println("Cancel - Order id ${record.orderId} found. ${order.shares} ${record.shares} ")
                        if (order.shares == record.shares) {
                            //println("Removing order ${record.orderId}")
                            orderMap.remove(record.orderId)
                        } else {
                            logger.info { "Updating order ${record.orderId}" }
                            orderMap[record.orderId] = order.copy(shares = order.shares - record.shares)
                        }
                        //println("Cancel - Order id ${record.orderId} found. ${order.shares} ${record.shares} ")


                    } else {
                        //println("Cancel - Order id ${record.orderId} not found.")
                    }
                }

                is TradeRecord -> {
                    executedVolumes[record.symbol] = executedVolumes.getOrDefault(record.symbol, 0) + record.shares
                }

                else -> {
                    logger.info { "Unknown record type ${record::class.simpleName}" }
                }
            }
        }
        logger.info { executedVolumes }
        return executedVolumes
    }
}
