package zero.one.home3

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.Date

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class UserRequest(
    val fullname: String,
    val username: String,
)

data class UserResponse(
    @Schema(description = "Full name of the user", example = "John Smith")
    val fullname: String,
    val username: String,
    val balance: BigDecimal,
    val createdDate: Date?,
)

data class UpdateUserRequest(
    val fullname: String?= null,
    val username: String? = null,
    val balance: BigDecimal? = null,
)