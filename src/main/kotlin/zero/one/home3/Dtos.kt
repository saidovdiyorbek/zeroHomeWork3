package zero.one.home3

import java.math.BigDecimal

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class UserRequest(
    val fullname: String,
    val username: String,
)