package pitch

import io.github.serpro69.kfaker.Faker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

private val faker = Faker()

class PitchServiceTest : FunSpec({

    context("processData") {

        test("should track all open orders") {

            val records = List(faker.random.nextInt(10, 1000)) {
                faker.randomProvider.randomClassInstance<AddOrderRecord>()
            }

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe records.associateBy { it.orderId }
        }

        test("should execute a volume against an open order") {

            val volume = faker.random.nextInt()
            val symbol = faker.random.randomString()
            val orderId = faker.random.randomString()

            val records = listOf(
                AddOrderRecord(orderId, volume, symbol),
                ExecuteOrderRecord(orderId, volume)
            )

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe mapOf(
                symbol to volume
            )
            result.orders shouldBe mapOf(
                orderId to AddOrderRecord(orderId, volume, symbol)
            )
        }

        test("should not execute a volume if an open order is not found") {

            val volume = faker.random.nextInt()
            val orderId = faker.random.randomString()

            val records = listOf(
                ExecuteOrderRecord(orderId, volume)
            )

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe emptyMap()
        }

        test("should fully cancel an order") {

            val volume = faker.random.nextInt(1, 9999)
            val symbol = faker.random.randomString()
            val orderId = faker.random.randomString()

            val records = listOf(
                AddOrderRecord(orderId, volume, symbol),
                CancelOrderRecord(orderId, volume)
            )

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe emptyMap()
        }

        test("should partially cancel an order") {

            val volume = faker.random.nextInt(100, 9999)
            val symbol = faker.random.randomString()
            val orderId = faker.random.randomString()
            val cancelledVolume = faker.random.nextInt(1, 100)

            val records = listOf(
                AddOrderRecord(orderId, volume, symbol),
                CancelOrderRecord(orderId, cancelledVolume)
            )

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe mapOf(
                orderId to AddOrderRecord(orderId, volume - cancelledVolume, symbol)
            )
        }

        test("should execute all trade records") {

            val symbol = faker.random.randomString()

            val records = List(faker.random.nextInt(10, 100)) {
                TradeRecord(
                    faker.random.randomString(),
                    faker.random.nextInt(1, 1000),
                    symbol,
                )
            }

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe mapOf(
                symbol to records.sumOf { it.shares }
            )
            result.orders shouldBe emptyMap()

        }

        test("should ignore ignored records") {

            val records = List(faker.random.nextInt(10, 1000)) {
                faker.randomProvider.randomClassInstance<IgnoredRecord>()
            }

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe emptyMap()

        }

        test("should ignore unsupported records") {

            val records = List(faker.random.nextInt(10, 1000)) {
                faker.randomProvider.randomClassInstance<UnsupportedRecord>()
            }

            val sut = PitchService(records)
            val result = sut.processData()

            result.executedVolumes shouldBe emptyMap()
            result.orders shouldBe emptyMap()

        }
    }
})

data class UnsupportedRecord(val orderId: String, val shares: Int, val symbol: String)
