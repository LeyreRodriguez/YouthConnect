package com.example.youthconnect.Model.Object

data class Child(
    val fullName: String,
    val id: String,
    val course: String,
    val password: String,
    val belongsToSchool: Boolean,
    val faithGroups: Boolean,
    val goOutAlone: Boolean,
    val observations: String? = null,
    val parentId: List<String>,
    var instructorId: String,
    val state: Boolean,
    val score: Int? =  0,
    val rollCall: List<String>? = emptyList()
)

