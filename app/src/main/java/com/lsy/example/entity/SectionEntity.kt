package com.lsy.example.entity

class SectionEntity {

    var sectionName: String = ""
    var sectionId: Int = 0

    constructor()

    constructor(sectionName: String, sectionId: Int) {
        this.sectionName = sectionName
        this.sectionId = sectionId
    }

}