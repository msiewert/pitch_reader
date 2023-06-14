import mu.KotlinLogging
import pitch.IgnoredRecord
import pitch.PitchParser

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

        println("${ignoredRecords.count()} records ignored.")

        println("${output.count()} records parsed.")


    } catch (e: Exception) {
        logger.error(e) { "An unexpected error occurred while reading pitch data." }
    }
}
