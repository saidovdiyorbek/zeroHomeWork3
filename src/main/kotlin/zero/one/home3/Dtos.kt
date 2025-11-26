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

data class TransactionRequest(
    @Schema(description = "User id of the transaction", example = "1")
    val userId: Long,
    @Schema(description = "Total amount", example = "0.000")
    val totalAmount: BigDecimal,
)

data class TransactionResponse(
    val id: Long,
    val userId: Long,
    val totalAmount: BigDecimal,
    val createdDate: Date,
    val createdBy: String,
)