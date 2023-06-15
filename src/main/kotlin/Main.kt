import mu.KotlinLogging
import pitchdata.IgnoredRecord
import pitchdata.PitchParser
import pitchdata.PitchService

private val logger = KotlinLogging.logger { }

fun main() {
    try {
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

        logger.info { "${output.count()} total records loaded." }
        logger.info { "${ignoredRecords.count()} records will be ignored." }
        logger.info { "${recordsToProcess.count()} records will be processed." }

        val pitchService = PitchService(recordsToProcess)
        val executionResults = pitchService.processData()

        val topTenReport = executionResults.toList().sortedByDescending { (_, value) -> value }.take(10)
        logger.info { "Top 10 symbols by executed volume:" }
        topTenReport.forEach {
            logger.info { "${it.first} - ${it.second}" }
        }


    } catch (e: Exception) {
        logger.error(e) { "An unexpected error occurred while reading pitch data." }
    }
}
