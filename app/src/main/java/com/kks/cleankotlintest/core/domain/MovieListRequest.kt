package com.kks.cleankotlintest.core.domain

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/

data class MovieListRequest(
    val page: Int?,
    val results: List<MovieRequest>?,
    val total_pages: Int?,
    val total_results: Int?,
    var status_code: Int? = null,
    var status_message: String? = null,
    var success: Boolean? = null,
    var errors: Array<String>? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieListRequest

        if (page != other.page) return false
        if (results != other.results) return false
        if (total_pages != other.total_pages) return false
        if (total_results != other.total_results) return false
        if (status_code != other.status_code) return false
        if (status_message != other.status_message) return false
        if (success != other.success) return false
        if (errors != null) {
            if (other.errors == null) return false
            if (!errors.contentEquals(other.errors)) return false
        } else if (other.errors != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = (page ?: 0)
        result = 31 * result + results.hashCode()
        result = 31 * result + (total_pages ?: 0)
        result = 31 * result + (total_results ?: 0)
        result = 31 * result + (status_code ?: 0)
        result = 31 * result + (status_message?.hashCode() ?: 0)
        result = 31 * result + (success?.hashCode() ?: 0)
        result = 31 * result + (errors?.contentHashCode() ?: 0)
        return result
    }
}