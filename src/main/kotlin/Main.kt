import mu.KotlinLogging
import pitchdata.IgnoredRecord
import pitchdata.PitchParser
import pitchdata.PitchService

private val logger = KotlinLogging.logger { }

fun main() {
    try {
        println("Pitch reader is executing.")
        logger.info { "Pitch reader is executing." }

        val pitchParser = PitchParser(generateSequence(::readLine))
        val output = pitchParser.parse()

//        output.forEach {
//            println(it)
//        }

        val ignoredRecords = output.mapNotNull {
            if (it is IgnoredRecord) {
                it
            } else null
        }

        val recordsToProcess = output.mapNotNull {
            if (it !is IgnoredRecord) {
                it
            } else null
        }

        println("${output.count()} total records loaded.")
        println("${ignoredRecords.count()} records will be ignored.")
        println("${recordsToProcess.count()} records will be processed.")

        val pitchService = PitchService(recordsToProcess)
        val executionResults = pitchService.processData()

        val topTenReport = executionResults.toList().sortedByDescending { (_, value) -> value }.take(10)
        println("Top 10 symbols by executed volume:")
        topTenReport.forEach {
            println("${it.first} - ${it.second}")
        }


    } catch (e: Exception) {
        logger.error(e) { "An unexpected error occurred while reading pitch data." }
    }
}
