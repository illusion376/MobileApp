package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class RegisterResponseDTO(
    val status : Int,
    val message : String,
    val token : String? = null
)