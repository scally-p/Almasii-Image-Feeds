package io.capsella.almasii.imagefeeds.model

class Image(id: Int, url: String, downloadToken: String, name: String, createdAtMillis: Int) {

    var id: Int = id

    var url: String = url

    var downloadToken: String = downloadToken

    var name: String = name

    var createdAtMillis: Int = createdAtMillis
}