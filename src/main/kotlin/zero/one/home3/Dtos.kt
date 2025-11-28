package zero.one.home3

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.Date
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

data class UserBoughtProductsResponse(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val productId: Long,
    val productName: String?,
    val count: Long,
    val totalAmount: BigDecimal,
    val transactionId: Long
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

data class ProductUpdateRequest(
    val name: String?,
    val count: Long?,
    val categoryId: Long?,
    val prince: BigDecimal?,
)

data class CategoryCreateRequest(
    val name: String,
    val order: Long,
    val description: String,
)

data class CategoryFullInfo(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val name: String?,
    val order: Long?,
    val description: String?
)

data class CategoryUpdateRequest(
    val name: String?,
    val order: Long?,
    val description: String?
)

data class TransactionFullInformation(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val userId: Long?,
    val totalAmount: BigDecimal,

    )
data class UserPaymentTransactionResponse(
    val id: Long?,
    val userId: Long,
    val createdDate: Date?,
    val amount: BigDecimal?
)

data class UserPaymentTransactionFullInfo(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val username: String?,
    val userId: Long?,
    val amount: BigDecimal,
)

data class ProductFullInfo(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val name: String?,
    val count: Long?,
    val categoryId: Long?,
    var price: BigDecimal?,
)


