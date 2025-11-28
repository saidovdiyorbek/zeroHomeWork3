package zero.one.home3


import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
    fun findAllNotDeleted(pageable: Pageable): Page<T>

}

class BaseRepositoryImpl<T : BaseEntity>(
    entiInformation: JpaEntityInformation<T, Long>, entityManager: EntityManager
): SimpleJpaRepository<T, Long>(entiInformation, entityManager), BaseRepository<T> {
    override fun findByIdAndDeletedFalse(id: Long): T? = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run{
        deleted = true
        save(this)
    }

    override fun trashList(ids: List<Long>): List<T?> {
        TODO("Not yet implemented")
    }

    override fun findAllNotDeleted(): List<T> {
        TODO("Not yet implemented")
    }

    override fun findAllNotDeleted(pageable: Pageable): Page<T> {
        TODO("Not yet implemented")
    }
}
@Repository
interface UserRepository : BaseRepository<User>{
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Transactional
    @Query("update User u set u.balance = u.balance - ?2" +
            " where u.balance >= ?2 and u.id = ?1")
    fun deductBalance(userId: Long?, amount: BigDecimal)

    @Query("select u from User u where u.id = ?1 and u.balance >= ?2")
    fun checkBalance(userId: Long?, amount: BigDecimal): User?
}
//User repo

//Transaction repo
@Repository
interface TransactionRepository : BaseRepository<Transaction>{
    fun findByUser(user: User): List<Transaction>
}

@Repository
interface ProductRepository : BaseRepository<Product>{

    @Modifying
    @Transactional
    @Query("update Product p set p.count = p.count - ?2 where p.id = ?1 and p.count >= ?2")
    fun deductCount(productId: Long?, count: Long)
}

@Repository
interface CategoryRepository : BaseRepository<Category>{
    fun findByName(name: String): Category?

    @Modifying
    @Transactional
    @Query("update Category c set c.order = c.order + 1  where c.order >= ?1")
    fun shiftOrderUp(order: Long)


}

@Repository
interface TransactionItemRepository : BaseRepository<TransactionItem>{

    @Query("""
        select t.id as id,
        t.createdDate as createdDate,
        t.lastModifiedDate as lastModifiedDate,
        t.createdBy as createdBy,
        t.lastModifiedBy as lastModifiedBy,
        t.deleted as deleted,
        t.product.id as productId,
        p.name as productName,
        t.count as count,
        t.totalAmount as totalAmount,
        t.transaction.id as transactionId
        from TransactionItem t
        join Product p on t.product.id = p.id
        join Transaction tt on t.transaction.id = tt.id
        where tt.user.id = ?1

""")
    fun getUserBoughtProducts(userId: Long?): List<TransactionFullInformationProjection>

}

@Repository
interface UserPaymentTransactionRepository : BaseRepository<UserPaymentTransaction>{

    @Query("""
        select ut
        from UserPaymentTransaction ut
        where ut.user.id = ?1
    """)
    fun getUserPaymentsByUserId(userId: Long?): List<UserPaymentTransaction?>?


    @Query("""
        select 
            u.id as id,
            u.createdDate as createdDate,
            u.lastModifiedDate as lastModifiedDate,
            u.createdBy as createdBy,
            u.lastModifiedBy as lastModifiedBy,
            u.deleted as deleted,
            us.username as username,
            u.user.id as userId,
            u.amount as amount
        from UserPaymentTransaction u
        join User us on u.user.id = u.id
    """)
    fun findByUser(user: User): List<UserPaymentTransactionFullInfoProjection>
}