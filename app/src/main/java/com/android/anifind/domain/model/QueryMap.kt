package com.android.anifind.domain.model

class QueryMap(builder: Builder) {
    var limit: String = "20"
    var order: String? = null
    var kind: String? = null
    var type: String? = null
    var status: String? = null
    var season: String? = null
    var score: String? = null
    var duration: String? = null
    var rating: String? = null
    var genre: String? = null
    var studio: String? = null

    fun getHashMap(): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()
        hashMap["limit"] = limit
        order?.let { hashMap["order"] = it }
        kind?.let { hashMap["kind"] = it }
        type?.let { hashMap["kind"] = it }
        status?.let { hashMap["status"] = it }
        season?.let { hashMap["season"] = it }
        score?.let { hashMap["score"] = it }
        duration?.let { hashMap["duration"] = it }
        rating?.let { hashMap["rating"] = it }
        genre?.let { hashMap["genre"] = it }
        studio?.let { hashMap["studio"] = it }
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
        private var type: String? = null
        private var status: String? = null
        private var season: String? = null
        private var score: String? = null
        private var duration: String? = null
        private var rating: String? = null
        private var genre: String? = null
        private var studio: String? = null

        fun order(order: String?) = apply { this.order = order }
        fun kind(kind: String?) = apply { this.kind = kind }
        fun type(type: String?) = apply { this.type = type }
        fun status(status: String?) = apply { this.status = status }
        fun season(season: String?) = apply { this.season = season }
        fun score(score: String?) = apply { this.score = score }
        fun duration(duration: String?) = apply { this.duration = duration }
        fun rating(rating: String?) = apply { this.rating = rating }
        fun genre(genre: String?) = apply { this.genre = genre }
        fun studio(studio: String?) = apply { this.studio = studio }

        fun build() = QueryMap(this)

        fun getOrder() = order
        fun getKind() = kind
        fun getType() = type
        fun getStatus() = status
        fun getSeason() = season
        fun getScore() = score
        fun getDuration() = duration
        fun getRating() = rating
        fun getGenre() = genre
        fun getStudio() = studio
    }

    init {
        order = builder.getOrder()
        kind = builder.getKind()
        type = builder.getType()
        status = builder.getStatus()
        season = builder.getSeason()
        score = builder.getScore()
        duration = builder.getDuration()
        rating = builder.getRating()
        genre = builder.getGenre()
        studio = builder.getStudio()
    }
}