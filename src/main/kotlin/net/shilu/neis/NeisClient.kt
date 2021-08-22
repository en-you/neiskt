package net.shilu.neis

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import net.shilu.neis.entities.SchoolInfo
import net.shilu.neis.entities.SchoolMeal
import net.shilu.neis.entities.SchoolMealInfo
import net.shilu.neis.utilities.JsonMapper
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class NeisClient {
    suspend fun search(schoolName: String): List<SchoolInfo> {
        val response = request("/schoolInfo") {
            parameter("SCHUL_NM", schoolName)
            parameter("Type", "json")
        }
        val json = JsonMapper.parse(response)
        return json["schoolInfo"].values().last()["row"].values()
            .map { schoolInfo ->
                SchoolInfo(
                    schoolInfo["ATPT_OFCDC_SC_CODE"].text()!!,
                    schoolInfo["ATPT_OFCDC_SC_NM"].text()!!,
                    schoolInfo["SD_SCHUL_CODE"].text()!!,
                    schoolInfo["SCHUL_NM"].text()!!,
                    schoolInfo["SCHUL_KND_SC_NM"].text()!!,
                    schoolInfo["ORG_RDNMA"].text()!! + schoolInfo["ORG_RDNDA"].text()!!,
                    schoolInfo
                )
            }
    }

    suspend fun retrieveMeals(
        officeCode: String,
        schoolCode: String,
        mealType: SchoolMeal? = null,
        date: TemporalAccessor = Instant.now()
    ): List<SchoolMealInfo> {
        val response = request("/mealServiceDietInfo") {
            this.parameter("ATPT_OFCDC_SC_CODE", officeCode)
            this.parameter("SD_SCHUL_CODE", schoolCode)
            if (mealType != null)
                this.parameter("MMEAL_SC_CODE", mealType.mealCode)
            this.parameter("MLSV_YMD", DATE_FORMATTER.format(date))
            parameter("Type", "json")
        }
        val json = JsonMapper.parse(response)
        return json["mealServiceDietInfo"].values().last()["row"].values()
            .map { mealInfo ->
                SchoolMealInfo(
                    mealInfo["SD_SCHUL_CODE"].text()!!,
                    mealInfo["SCHUL_NM"].text()!!,
                    SchoolMeal.values()
                        .first { "${it.mealCode}" == mealInfo["MMEAL_SC_CODE"].text()!! },
                    DATE_FORMATTER.parse(mealInfo["MLSV_YMD"].text()!!),
                    mealInfo["DDISH_NM"].text()!!.replace("<br/>", "\n"),
                    mealInfo["ORPLC_INFO"].text()!!,
                    mealInfo["NTR_INFO"].text()!!,
                    mealInfo["CAL_INFO"].text()!!,
                    mealInfo
                )
            }
    }

    private suspend fun request(endpoint: String, builder: HttpRequestBuilder.() -> Unit): String {
        val httpClient = HttpClient(CIO)
        httpClient.use { client ->
            val response = client.request<HttpResponse>(BASE_URL + endpoint) {
                this.apply(builder)
            }
            return response.readText(StandardCharsets.UTF_8)
        }
    }

    companion object {
        private const val BASE_URL = "https://open.neis.go.kr/hub"

        internal val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withZone(ZoneId.of("Asia/Seoul"))
    }
}
