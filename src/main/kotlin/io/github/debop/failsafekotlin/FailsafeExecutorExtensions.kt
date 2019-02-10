/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.debop.failsafekotlin

import net.jodah.failsafe.FailsafeExecutor
import net.jodah.failsafe.function.CheckedRunnable
import net.jodah.failsafe.function.CheckedSupplier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage


fun <R> FailsafeExecutor<R>.getChecked(supplier: () -> R): R =
    get(CheckedSupplier(supplier))

fun <R> FailsafeExecutor<R>.getAsync(supplier: () -> R): CompletableFuture<R> =
    getAsync(CheckedSupplier(supplier))

fun <R> FailsafeExecutor<R>.getStageAsync(supplier: () -> CompletionStage<R>): CompletableFuture<R> =
    getStageAsync(CheckedSupplier(supplier))

fun <R> FailsafeExecutor<R>.runChecked(runnable: () -> Unit) {
    run(CheckedRunnable(runnable))
}

fun <R> FailsafeExecutor<R>.runAsync(runnable: () -> Unit): CompletableFuture<Void> {
    return runAsync(CheckedRunnable(runnable))
}