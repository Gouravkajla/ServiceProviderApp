package com.gaurav.serviceproviderapp

data class ServiceProviderModel (
    var name: String ?= null,
    var image: String ?= null,
    var key: String ?= null,
    var address: String ?= null,
    var category: String ?= null,
    var email: String ?= null,
    var gender: Int ?= null,
    var phone: String ?= null,
    var currentStatus: Int?= 0 // 0 - new apply,, 1 - accepted, 2 - rejected
)