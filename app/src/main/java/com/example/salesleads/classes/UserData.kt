package com.example.salesleads.classes

data class UserData(val userId: String ?=null,
                    var imageURL: String? = null,
                    var compName: String? = null,
                    var firstname: String ?= null,
                    var lastname: String ?= null,
                    var email: String ?= null,
                    var rating: Float ?= null,
                    var description: String ?= null) {
}
