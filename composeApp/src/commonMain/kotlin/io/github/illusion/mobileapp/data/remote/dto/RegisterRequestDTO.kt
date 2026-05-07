package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDTO(val email : String,
                              val login : String,
                              val password : String)