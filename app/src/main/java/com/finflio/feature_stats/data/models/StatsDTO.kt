package com.finflio.feature_stats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsDTO(
    @SerialName("message")
    val message: String,
    @SerialName("stats")
    val stats: Stats,
    @SerialName("status")
    val status: Int
)