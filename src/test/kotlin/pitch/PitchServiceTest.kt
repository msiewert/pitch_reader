package pitch

import io.github.serpro69.kfaker.Faker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

private val faker = Faker()

class PitchServiceTest : FunSpec({

    context("processData") {

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
        }
    }
})
