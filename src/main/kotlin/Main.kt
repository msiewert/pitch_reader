import mu.KotlinLogging
import pitch.PitchParser
import pitch.PitchService
import pitch.UnsupportedRecord

private val logger = KotlinLogging.logger { }

fun main() {
    try {
        logger.info { "Pitch reader is executing." }

        val pitchParser = PitchParser(generateSequence(::readLine))
        val output = pitchParser.parse()

        val ignoredRecords = output.mapNotNull {
            if (it is UnsupportedRecord) {
                it
            } else null
        }

        val recordsToProcess = output.mapNotNull {
            if (it !is UnsupportedRecord) {
                it
            } else null
        }

        logger.info { "${output.count()} total records loaded." }
        logger.info { "${ignoredRecords.count()} records will be ignored." }
        logger.info { "${recordsToProcess.count()} records will be processed." }

        val executionResults = PitchService().processData(recordsToProcess)

        val topTenReport = executionResults.executedVolumes.toList().sortedByDescending { (_, value) -> value }.take(10)
        logger.info { "Top 10 symbols by executed volume:" }
        topTenReport.forEach {
            logger.info { "${it.first} ${it.second}" }
        }


    } catch (e: Exception) {
        logger.error(e) { "An unexpected error occurred while reading pitch data." }
    }
}
