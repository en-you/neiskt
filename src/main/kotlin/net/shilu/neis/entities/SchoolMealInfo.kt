package net.shilu.neis.entities

import net.shilu.neis.NeisClient
import net.shilu.neis.SchoolMeal
import net.shilu.neis.utilities.JsonMapper
import java.time.temporal.TemporalAccessor

data class SchoolMealInfo(
    val schoolCode: String,
    val schoolName: String,
    val kind: SchoolMeal,
    val date: TemporalAccessor,
    val content: String,
    val originInfo: String,
    val nutritionInfo: String,
    val calorie: String,
    val raw: JsonMapper
) {
    override fun toString(): String {
        return "SchoolMealInfo(C:${this.schoolCode} D:${NeisClient.DATE_FORMATTER.format(this.date)})"
    }
}