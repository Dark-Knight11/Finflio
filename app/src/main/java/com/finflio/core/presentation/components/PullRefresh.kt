package com.finflio.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.finflio.ui.theme.PrimaryText

@Composable
fun PullRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)

    Box(modifier = Modifier.pullRefresh(pullRefreshState, enabled = enabled)) {
        content()
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .matchParentSize()
                .clipToBounds(),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedVisibility( // todo: fix this when cause of issue is known
                visible = ((pullRefreshState.progress != 0f) or refreshing),
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .height(160.dp)
                ) {
                    PullRefreshIndicator(
                        refreshing = refreshing,
                        state = pullRefreshState,
                        contentColor = PrimaryText
                    )
                }
            }
        }
    }
}