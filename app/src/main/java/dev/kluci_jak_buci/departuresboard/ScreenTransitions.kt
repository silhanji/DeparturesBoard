package dev.kluci_jak_buci.departuresboard

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

object ScreenTransitions {
    private const val DURATION = 400
    private val easing = FastOutSlowInEasing

    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(DURATION, easing = easing),
            initialOffset = { it / 10 }
        ) + fadeIn(animationSpec = tween(DURATION))
    }

    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(DURATION, easing = easing),
            targetOffset = { -it / 10 }
        ) + fadeOut(animationSpec = tween(DURATION))
    }

    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(DURATION, easing = easing),
            initialOffset = { -it / 10 }
        ) + fadeIn(animationSpec = tween(DURATION))
    }

    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(DURATION, easing = easing),
            targetOffset = { it / 10 }
        ) + fadeOut(animationSpec = tween(DURATION))
    }
}