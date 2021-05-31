package com.example.shareit

class Upload {
    lateinit var name: String
    private lateinit var imageUrl: String
    private lateinit var description: String
    constructor() {
        // Vide
    }

    constructor(name: String, imageUrl: String, description: String) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        var description = description
        if (description.trim { it <= ' ' } == "") {
            description = "No description"
        }
        this.name = name
        this.imageUrl = imageUrl
        this.description = description
    }
}