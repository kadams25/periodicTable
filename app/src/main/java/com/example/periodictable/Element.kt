package com.example.periodictable

import java.io.Serializable

class Element(
    var atomicNumber: String = "",
    var symbol: String = "",
    var name: String = "",
    var period: String = "",
    var group: String = "",
    var groupBlock: String = "",
    var block: String = "",
    var yearDiscovered: String = "",
    var standardState: String = "",
    var bondingType: String = "",
    var atomicMass: String = "",
    var electronicConfiguration: String = "",
    var atomicRadius: String = "",
    var meltingPoint: String = "",
    var boilingPoint: String = "",
    var density: String = "",
) :Serializable
