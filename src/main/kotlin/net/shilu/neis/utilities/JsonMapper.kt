package net.shilu.neis.utilities

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class JsonMapper(private val node: JsonNode?) {
    operator fun get(key: String): JsonMapper {
        return if (node?.isObject == true) JsonMapper(node.get(key))
        else JsonMapper(null)
    }

    fun text(): String? {
        return node?.asText()
    }

    fun values() = mutableListOf<JsonMapper>().apply {
        node?.elements()?.forEach { add(JsonMapper(it)) }
    }.toList()

    @Suppress("unused")
    fun format(): String {
        return node?.toPrettyString() ?: ""
    }

    companion object {
        private val mapper = newMapper()

        fun parse(text: String): JsonMapper {
            return JsonMapper(mapper.readTree(text))
        }

        private fun newMapper(): ObjectMapper {
            val jsonFactory = JsonFactory()
            jsonFactory.enable(JsonParser.Feature.ALLOW_COMMENTS)
            jsonFactory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            return ObjectMapper(jsonFactory)
        }
    }
}
