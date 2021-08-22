import kotlinx.coroutines.runBlocking
import net.shilu.neis.NeisClient
import net.shilu.neis.entities.SchoolMeal
import java.time.OffsetDateTime

fun main() {
    runBlocking {
        val client = NeisClient()
        val searchResult = client.search("서울예술고등학교")

        val school = searchResult.first()
        val officeCode = school.officeCode
        val schoolCode = school.code

        val mealData = client.retrieveMeals(
            officeCode, schoolCode, SchoolMeal.LUNCH,
            OffsetDateTime.now().plusDays(1)
        ).first()
        println(school)
        println(school.location)
        println(school.name)
        println(mealData)
        println(mealData.content)
    }
}
