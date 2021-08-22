package net.shilu.neis.entities

import net.shilu.neis.utilities.JsonMapper

data class SchoolInfo(
    val officeCode: String,
    val officeName: String,
    val code: String,
    val name: String,
    val kind: String,
    val location: String,
    val raw: JsonMapper
) {
    override fun toString(): String {
        return "SchoolInfo(N:${this.name} C:${this.code})"
    }
}