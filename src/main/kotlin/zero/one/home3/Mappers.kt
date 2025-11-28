package zero.one.home3

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.Date

@Component
class ProductMapper(

){
    fun toEntity(body: ProductCreateRequest, category: Category): Product {
        body.run {
            return Product(
                name = name,
                count = count,
                category = category,
                prince
            )
        }
    }
}

data class TransactionItemFullInfo(
    val id: Long?,
    val createdDate: Date?,
    val lastModifiedDate: Date?,
    val createdBy: String?,
    val lastModifiedBy: String?,
    val deleted: Boolean?,
    val productId: Long?,
    val productName: String?,
    val count: Long?,
    val totalAmount: BigDecimal?,
    val transactionId: Long?
)

@Component
class CategoryMapper(){
    fun toEntity(body: CategoryCreateRequest): Category {
        body.run {
            return Category(
                name = name,
                order = order,
                description = description,
            )
        }
    }
}

@Component
class TransactionMapper(){
    fun toDto(body: Transaction): TransactionFullInformation {
        body.run {
            return TransactionFullInformation(
                id,
                createdDate,
                lastModifiedDate,
                createdBy,
                lastModifiedBy,
                deleted,
                user.id,
                totalAmount
            )
        }
    }
}

interface TransactionFullInformationProjection{
    fun getId(): Long?
    fun getCreatedDate(): Date?
    fun getLastModifiedDate(): Date?
    fun getCreatedBy(): String?
    fun getLastModifiedBy(): String?
    fun getDeleted(): Boolean?
    fun getProductId(): Long
    fun getProductName(): String?
    fun getCount(): Long
    fun getTotalAmount(): BigDecimal
    fun getTransactionId(): Long?
}

@Component
class TransactionItemMapper(){

    fun toTransactionItemFullInfo(body: TransactionFullInformationProjection): TransactionItemFullInfo {
        body.run {
            return TransactionItemFullInfo(
                 getId(),
                 getCreatedDate(),
                 getLastModifiedDate(),
                 getCreatedBy(),
                 getLastModifiedBy(),
                 getDeleted(),
                 getProductId(),
                 getProductName(),
                 getCount(),
                 getTotalAmount(),
                 getTransactionId()
            )
        }
    }
}