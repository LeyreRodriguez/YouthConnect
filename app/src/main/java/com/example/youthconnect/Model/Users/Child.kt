package com.example.youthconnect.Model.Users

import com.example.youthconnect.Model.Course

data class Child(
    val FullName: String,
    val ID: String,
    val Course: String,
    val Password: String,
    val BelongsToSchool: Boolean,
    val FaithGroups: Boolean,
    val GoOutAlone: Boolean,
    val Observations: String? = null,
    val ParentID: List<String>,
    val InstructorID: String
)

