package com.example.shareit

class Upload {
    lateinit var name: String
    lateinit var imageUrl: String
    constructor() {
        // Vide
    }

    constructor(name: String, imageUrl: String) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        this.imageUrl = imageUrl
    }
}