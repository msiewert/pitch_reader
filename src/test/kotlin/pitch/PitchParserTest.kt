package pitch

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf


class PitchParserTest : FunSpec({
    context("parse") {

        test("should ignore Trade Break and long messages") {

            val records = listOf(
                "S28921671B6K27GA0000C0B000500IWM   0000736800Y",
                "S28921671r6K27GA0000C0B000500IWM   0000736800Y",
                "S28921671d6K27GA0000C0B000500IWM   0000736800Y",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result.forEach {
                it.shouldBeInstanceOf<IgnoredRecord>()
            }
            result shouldContain IgnoredRecord("B")
            result shouldContain IgnoredRecord("r")
            result shouldContain IgnoredRecord("d")
        }

        test("should load AddOrderRecords") {

            val records = listOf(
                "S28800011AAK27GA0000DTS000100SH    0000619200Y",
                "S28800012ABK27GA00000KB001000SSO   0000763800Y",
                "S28800012ABK27GA00000LB001000SSO   0000763600Y",
                "S28800012ABK27GA00000MS001000SSO   0000764800Y",
                "S28800012AAK27GA0000DUS001000SDS   0000549300Y",
                "S28800012AAK27GA0000DVB001000SDS   0000548000Y",
                "S28800012AAK27GA0000DWB000100SPY   0001424100Y",
                "S28800012AAK27GA0000DXS000100SPY   0001426300Y",
                "S28800012AAK27GA0000DYB000100SH    0000618500Y",
                "S28800015AAK27GA0000DZB001000SDS   0000547800Y",
                "S28800075ABK27GA00000NS000100UWM   0000542900Y",
                "S28800075ABK27GA00000OB000100TWM   0000701400Y",
                "S28800075ABK27GA00000PB000100TWM   0000701600Y",
                "S28800075ABK27GA00000QB000100UWM   0000542000Y",
                "S28800075ABK27GA00000RS000100TWM   0000702600Y",
                "S28800075ABK27GA00000SB000100TWM   0000701500Y",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result.forEach {
                it.shouldBeInstanceOf<AddOrderRecord>()
            }
        }

        test("should load CancelOrderRecords") {

            val records = listOf(
                "S28800174X5K27GA00000K000100",
                "S28800179X5K27GA00000J000100",
                "S28800180X1K27GA00000V000100",
                "S28800180X1K27GA00000Y000100",
                "S28800180X1K27GA00000X000100",
                "S28800180XAK27GA0000DW000100",
                "S28800181X4K27GA00002X000100",
                "S28800179X6K27GA00001I000100",
                "S28800181X1K27GA00000Y000100",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result.forEach {
                it.shouldBeInstanceOf<CancelOrderRecord>()
            }
        }

        test("should load ExecuteOrderRecords") {

            val records = listOf(
                "S28807216E5K27GA00000S00010000005AQ00002",
                "S28807239E5K27GA00000X00010000005AQ00003",
                "S28800318E1K27GA00000X00010000001AQ00001",
                "S28803224E4K27GA00003G00007600004AQ00002",
                "S28803224E4K27GA00003G00007700004AQ00001",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result.forEach {
                it.shouldBeInstanceOf<ExecuteOrderRecord>()
            }
        }

        test("should load TradeRecords") {

            val records = listOf(
                "S28807528PCK27GA000016B000177ZVZZT 0020000000000I000HV1PJ",
                "S28807529PCK27GA000016B000100ZVZZT 0020000000000I000HV1PK",
                "S28807529PCK27GA000016B000100ZVZZT 0020000000000I000HV1PL",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result.forEach {
                it.shouldBeInstanceOf<TradeRecord>()
            }
        }

        test("should load multiple types of records") {

            val records = listOf(
                "S28921671B6K27GA0000C0B000500IWM   0000736800Y",
                "S28800011AAK27GA0000DTS000100SH    0000619200Y",
                "S28800174X5K27GA00000K000100",
                "S28803224E4K27GA00003G00007700004AQ00001",
                "S28807529PCK27GA000016B000100ZVZZT 0020000000000I000HV1PL",
            ).asSequence()

            val sut = PitchParser(records)

            val result = sut.parse()

            result.count() shouldBe records.count()
            result[0].shouldBeInstanceOf<IgnoredRecord>()
            result[1].shouldBeInstanceOf<AddOrderRecord>()
            result[2].shouldBeInstanceOf<CancelOrderRecord>()
            result[3].shouldBeInstanceOf<ExecuteOrderRecord>()
            result[4].shouldBeInstanceOf<TradeRecord>()
        }
    }
})
