package com.android.anifind.domain

class QueryMap(builder: Builder) {
    var limit: String = "20"
    var order: String? = null
    var kind: String? = null
    var status: String? = null
    var season: String? = null
    var year: String? = null
    var genre: String? = null

    fun getHashMap(): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()
        hashMap["limit"] = limit
        order?.let { hashMap["order"] = it }
        kind?.let { hashMap["kind"] = it }
        status?.let { hashMap["status"] = it }
        year?.let { hashMap["season"] = it }
        season?.let { hashMap["season"] = String.format("%s_%s", it, year) }
        genre?.let { hashMap["genre"] = it }
        return hashMap
    }

    fun getHashMapWithRandom(): HashMap<String, String> {
        val hashMap = getHashMap()
        hashMap["order"] = "random"
        return hashMap
    }

    class Builder {
        private var order: String? = null
        private var kind: String? = null
        private var status: String? = null
        private var season: String? = null
        private var year: String? = null
        private var genre: String? = null

        fun order(order: String?) = apply { this.order = order }
        fun kind(kind: String?) = apply { this.kind = kind }
        fun status(status: String?) = apply { this.status = status }
        fun season(season: String?) = apply { this.season = season }
        fun year(year: String?) = apply { this.year = year }
        fun genre(genre: String?) = apply { this.genre = genre }

        fun build() = QueryMap(this)

        fun getOrder() = order
        fun getKind() = kind
        fun getStatus() = status
        fun getSeason() = season
        fun getYear() = year
        fun getGenre() = genre
    }

    init {
        order = builder.getOrder()
        kind = builder.getKind()
        status = builder.getStatus()
        season = builder.getSeason()
        year = builder.getYear()
        genre = builder.getGenre()
    }
}