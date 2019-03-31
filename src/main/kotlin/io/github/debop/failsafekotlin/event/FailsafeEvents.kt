package io.github.debop.failsafekotlin.event

import net.jodah.failsafe.event.ExecutionAttemptedEvent
import net.jodah.failsafe.event.ExecutionCompletedEvent

typealias ExecutionCompletedEventHandler<R> = (ExecutionCompletedEvent<R>) -> Unit
typealias ExecutionAttemptedEventHandler<R> = (ExecutionAttemptedEvent<R>) -> Unit