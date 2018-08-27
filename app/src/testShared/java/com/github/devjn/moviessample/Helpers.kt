package com.github.devjn.moviessample

import kotlinx.coroutines.experimental.Unconfined
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Created by @author Jahongir on 26-Aug-18
 * devjn@jn-arts.com
 * Helpers
 */
class TestContextProvider : Provider.CoroutineContextProvider {
    override val Main: CoroutineContext = Unconfined
    override val IO: CoroutineContext = Unconfined
}