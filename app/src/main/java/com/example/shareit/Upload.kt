package com.example.shareit

class Upload {
    private var name: String? = null
    private var imageUrl: String? = null

    constructor() {
        // Vide
    }

    constructor(name: String, imageUrl: String?) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        this.imageUrl = imageUrl
    }
}