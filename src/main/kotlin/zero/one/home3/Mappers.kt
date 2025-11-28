package zero.one.home3

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.Date
import kotlin.Long

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

    fun toProductFullInfo(body: Product): ProductFullInfo {
        body.run {
            return ProductFullInfo(
                id,
                createdDate,
                lastModifiedDate,
                createdBy,
                lastModifiedBy,
                deleted,
                name,
                count,
                category?.id,
                price
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

    fun toCategoryFullInfo(body: Category): CategoryFullInfo {
        body.run {
            return CategoryFullInfo(
                 id,
             createdDate,
             lastModifiedDate,
             createdBy,
             lastModifiedBy,
             deleted,
             name,
             order,
             description
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

interface UserPaymentTransactionFullInfoProjection{
    fun getId(): Long?
    fun getCreatedDate(): Date?
    fun getLastModifiedDate(): Date?
    fun getCreatedBy(): String?
    fun getLastModifiedBy(): String?
    fun getDeleted(): Boolean?
    fun getUsername(): String?
    fun getUserId(): Long?
    fun getAmount(): BigDecimal
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

@Component
class UserPaymentTransactionMapper(){
    fun toUserPaymentTransactionFullInfo(body: UserPaymentTransactionFullInfoProjection): UserPaymentTransactionFullInfo {
        body.run {
            return UserPaymentTransactionFullInfo(
                getId(),
                getCreatedDate(),
                getLastModifiedDate(),
                getCreatedBy(),
                getLastModifiedBy(),
                getDeleted(),
                getUsername(),
                getUserId(),
                getAmount(),
            )
        }
    }
}