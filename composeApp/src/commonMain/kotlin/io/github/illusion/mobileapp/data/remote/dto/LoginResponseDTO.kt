package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO(
    val status : Int,
    val userId : Int?,
    val message : String,
    val token : String? = null
)