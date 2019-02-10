/*
 * Copyright (c) 2017-2020 by Coupang. Some rights reserved.
 * See the project homepage at: http://gitlab.coupang.net
 * Licensed under the Coupang License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://coupang.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.debop.failsafekotlin

import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.RetryPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * FailsafeExecutorExtensionsTest
 * @author debop
 * @since 2019-02-11
 */
class FailsafeExecutorExtensionsTest {

    @Test
    fun `get with retry`() {
        val retry = RetryPolicy<String>()

        val result = Failsafe.with(retry).getChecked { "failsafe" }

        assertEquals("failsafe", result)
    }

    @Test
    fun `get exception with retry`() {
        val retry = RetryPolicy<String>()
            .withMaxRetries(3)
            .onFailedAttempt { evt ->
                println("failure message=${evt.lastFailure?.message}")
            }

        val result = Failsafe.with(retry).get { context ->
            if (context.attemptCount < 2) {
                throw RuntimeException("Boom!")
            } else {
                "Success"
            }
        }

        assertEquals("Success", result)
    }
}