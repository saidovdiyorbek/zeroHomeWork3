package zero.one.home3

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.Date
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

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
    @field:Size(min = 4, max = 30) val fullname: String?= null,
    @field:Size(min = 8, max = 52, ) val username: String? = null,
    val balance: BigDecimal? = null,
)

data class TransactionRequest(
    @Schema(description = "User id of the transaction", example = "1")
    val userId: Long,
    @Schema(description = "Total items")
    val items: List<TransactionItemRequest>,
)

data class TransactionItemRequest(
    @Schema(description = "Product id", example = "1")
    val productId: Long,
    @Schema(description = "Item count", example = "5")
    val count: Long,
)

data class TransactionResponse(
    val id: Long,
    val userId: Long,
    val totalAmount: BigDecimal,
    val createdDate: Date,
    val createdBy: String,
)

data class ProductCreateRequest(
    val name: String,
    val count: Long,
    val categoryId: Long,
    val prince: BigDecimal
)

data class CategoryCreateRequest(
    val name: String,
    val order: Long,
    val description: String,
)

