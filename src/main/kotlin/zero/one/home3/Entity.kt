package zero.one.home3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.util.Date

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var lastModifiedDate: Date? = null,
    @CreatedBy var createdBy: String? = null,
    @LastModifiedBy var lastModifiedBy: String? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity
class Category(
    var name: String,
    @Column(name = "orders") var order: Long,
    var description: String? = null,
) : BaseEntity()

@Entity
class Product(
    var name: String,
    var count: Long,
    var categoryId: Long,
) : BaseEntity()

@Entity
@Table(name = "users")
class User(
    var fullname: String,
    @Column(unique = true)var username: String,
    var balance: BigDecimal,
    var role: UserRole,
) : BaseEntity()

@Entity
class Transaction(
    @Column(name = "user_id", insertable = false, updatable = false)
    var userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    var totalAmount: BigDecimal,
) : BaseEntity()

@Entity
class TransactionItem(
    var productId: Long,
    var count: Long,
    var amount: BigDecimal,
    var totalAmount: BigDecimal,
    var transactionId: Long,
) : BaseEntity()

class UserPaymentTransaction(
    var userId: Long,
    var amount: BigDecimal,
    var date: Date,
)