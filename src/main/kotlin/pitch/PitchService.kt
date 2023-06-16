package pitch

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class PitchService(private val data: List<Any>) {

    private val orderMap = mutableMapOf<String, AddOrderRecord>()
    private val executedVolumes = mutableMapOf<String, Int>()

    fun processData(): Results {
        data.forEach { record ->
            when (record) {
                is AddOrderRecord -> {
                    if (orderMap.containsKey(record.orderId)) {
                        logger.info { "Duplicate order id ${record.orderId}" }
                    } else {
                        orderMap[record.orderId] = record
                    }
                }

                is ExecuteOrderRecord -> {
                    val order = orderMap[record.orderId]
                    if (order != null) {
                        logger.info { "Execute - Order id ${record.orderId} for ${record.shares} shares of ${order.symbol}" }
                        if (record.shares > order.shares) {
                            logger.info { "Execute - Order id ${record.orderId} for ${record.shares} shares of ${order.symbol} is greater than the order shares ${order.shares}." }
                        }
                        executedVolumes[order.symbol] = executedVolumes.getOrDefault(order.symbol, 0) + record.shares
                    } else {
                        logger.info { "Cannot Execute - Order id ${record.orderId} not found." }
                    }
                }

                is CancelOrderRecord -> {
                    val order = orderMap[record.orderId]
                    if (order != null) {
                        val orderUpdate = order.copy(shares = order.shares - record.shares)
                        if (orderUpdate.shares == 0) {
                            orderMap.remove(record.orderId)
                        } else {

                            orderMap[record.orderId] = orderUpdate
                        }
                    } else {
                        logger.info { "Cannot Cancel - Order id ${record.orderId} not found." }
                    }
                }

                is TradeRecord -> {
                    logger.info { "Trade - Order id ${record.orderId} for ${record.shares} shares of ${record.symbol}" }
                    executedVolumes[record.symbol] = executedVolumes.getOrDefault(record.symbol, 0) + record.shares
                }

                is IgnoredRecord -> {
                    logger.info { "Ignored record of type ${record.type}" }
                }

                else -> {
                    logger.info { "Unknown record type ${record::class.simpleName}" }
                }
            }
        }

        logger.info { "${orderMap.size} unfulfilled orders remain." }

        return Results(executedVolumes, orderMap)
    }
}

data class Results(val executedVolumes: Map<String, Int>, val orders: Map<String, AddOrderRecord>)
