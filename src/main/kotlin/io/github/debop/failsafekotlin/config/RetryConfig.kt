/*
 * Copyright (c) 2019. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package io.github.debop.failsafekotlin.config

import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * RetryConfig
 * @author debop
 * @since 2019-02-17
 */
class RetryProperties : Serializable {

    var maxRetries: Int = 3
    var maxAttempts: Int = 4

    var delay: Long? = null
    var maxDelay: Long? = null
    var delayUnit: TimeUnit = TimeUnit.MILLISECONDS

    var backoffDelay: Long? = 1000
    var backoffMaxDelay: Long? = 10_000
    var backoffTimeUnit: TimeUnit = TimeUnit.MILLISECONDS
    var backoffFactor: Double? = 2.0

    var abortOnClass: String? = null
}