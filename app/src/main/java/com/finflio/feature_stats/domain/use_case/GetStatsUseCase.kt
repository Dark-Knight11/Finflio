package com.finflio.feature_stats.domain.use_case

import android.util.Log
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_stats.domain.mapper.toStatsData
import com.finflio.feature_stats.domain.model.StatsData
import com.finflio.feature_stats.domain.repository.StatsRepository
import com.finflio.feature_stats.presentation.util.StatsUiEvent
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    val eventFlow = MutableSharedFlow<StatsUiEvent>()
    suspend operator fun invoke(): Flow<StatsData?> {
        return channelFlow {
            repository.getStats().collectLatest { res ->
                when (res.status) {
                    Resource.Status.SUCCESS -> {
                        send(res.data?.stats?.toStatsData())
                        close()
                    }

                    Resource.Status.ERROR -> {
                        if (res.message == "Check your internet connection!") {
                            eventFlow.emit(StatsUiEvent.ShowSnackbar(res.message))
                        } else {
                            eventFlow.emit(StatsUiEvent.ShowSnackbar("Something went wrong"))
                        }
                        Log.i(this.toString(), res.message.toString())
                        close()
                    }

                    Resource.Status.LOADING -> {
                        println("loading...")
                    }
                }
            }
        }
    }
}