package zero.one.home3

import getOrThrowNotFound
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.plus

//Category service
interface CategoryService{
    fun create(request: CategoryCreateRequest)
    fun getAll(): List<CategoryFullInfo>
    fun getOne(id: Long): CategoryFullInfo
    fun update(id: Long, updateBody: CategoryUpdateRequest)
    fun delete(id: Long)

}

@Service
class CategoryServiceImpl(
    private val repository: CategoryRepository,
    private val mapper: CategoryMapper
) : CategoryService {
    override fun create(request: CategoryCreateRequest) {
        request.run {
            repository.findByName(name)?.let {
                throw CategoryNotFoundException()
            } ?: run {
                   repository.shiftOrderUp(this.order)
                   repository.save(mapper.toEntity(this))
                }
            }
        }

    override fun getAll(): List<CategoryFullInfo> {
        val responses: MutableList<CategoryFullInfo> = mutableListOf()
        repository.findAll().forEach { category ->
            responses.add(mapper.toCategoryFullInfo(category))
        }
        return responses
    }

    override fun getOne(id: Long): CategoryFullInfo {
        val category = repository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()

        return category.run {
            mapper.toCategoryFullInfo(this)
        }
    }

    override fun update(id: Long, updateBody: CategoryUpdateRequest) {
        val category = repository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()

        repository.save(updateBody.run {
            this.name?.let { category.name = name }
            this.order?.let { category.order = order }
            this.description?.let { category.description = description }
            category
        })
    }

    override fun delete(id: Long) {
        repository.trash(id) ?: throw CategoryNotFoundException()
    }
}
//Category service

//User service
interface UserService{
    fun create(userRequest: UserRequest)
    fun getOne(id: Long): UserResponse
    fun delete(id: Long)
    fun getAll(): List<UserResponse>
    fun update(id: Long, updateBody: UpdateUserRequest)
    fun getAllPayments(id: Long): List<UserPaymentTransactionResponse>
}
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val transactionItemRepository: TransactionItemRepository,
    private val userPaymentRepository: UserPaymentTransactionRepository
) : UserService {
    override fun create(userRequest: UserRequest) {
        if(userRepository.existsByUsername(userRequest.username)){
           throw UserAlreadyExistsException()
        }

        userRepository.save(User(fullname = userRequest.fullname,
            username = userRequest.username,
            balance = "0.0".toBigDecimal(),
            role = UserRole.USER))
    }

    override fun getOne(id: Long): UserResponse {
        val findById = userRepository.findById(id)
        if (findById.isPresent){
            val user = findById.get()
            return UserResponse(user.fullname, user.username, user.balance, user.createdDate)
        }
        throw UserNotFoundException()
    }

    override fun delete(id: Long) {
        userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        userRepository.trash(id)
    }

    override fun getAll(): List<UserResponse> {
        val responseUsers: MutableList<UserResponse> = mutableListOf()

        userRepository.findAll().forEach {
            responseUsers.add(UserResponse(it.fullname, it.username, it.balance, it.createdDate))
        }
        return responseUsers
    }

    override fun update(id: Long, updateBody: UpdateUserRequest) {
        updateBody.run {
            val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
            fullname?.let { user.fullname = it }
            balance?.let { user.balance = it }
            username?.let { newUsername ->
                userRepository.existsByUsername(newUsername)
            }

        }
    }

    override fun getAllPayments(id: Long): List<UserPaymentTransactionResponse> {
        userRepository.findById(id).getOrThrowNotFound(UserNotFoundException())
        val userPaymentsByUserId = userPaymentRepository.getUserPaymentsByUserId(id)
        val response: MutableList<UserPaymentTransactionResponse> = mutableListOf()

        userPaymentsByUserId?.run {
            for (payments in this) {
                response.add(UserPaymentTransactionResponse(
                    payments?.id,
                    id,
                    payments?.createdDate,
                    payments?.amount
                ))
            }
        }
        return response;
    }
}
//USer service

//Transaction Service
interface TransactionService{
    fun create(request: TransactionRequest)
    fun getOne(id: Long): TransactionFullInformation
    fun getUserAllTransaction(userId: Long): List<TransactionFullInformation>
    fun getUserBoughtProducts(userId: Long): MutableList<TransactionItemFullInfo>
}

@Service
class TransactionServiceImpl(
    private val userRepository: UserRepository,
    private val repository: TransactionRepository,
    private val productRepository: ProductRepository,
    private val transactionItemRepository: TransactionItemRepository,
    private val userPaymentRepository: UserPaymentTransactionRepository,
    private val mapper: TransactionMapper,
    private val transactionItemMapper: TransactionItemMapper,
) : TransactionService {

    @Transactional
    override fun create(request: TransactionRequest) {
        val user = userRepository.findById(request.userId).getOrThrowNotFound(UserNotFoundException())

        var calculatedTotalAmount = BigDecimal.ZERO
        val transactionItems = mutableListOf<TransactionItem>()
        val fakeTransaction = Transaction(user, calculatedTotalAmount)

        for (itemReq in request.items){
            val product = productRepository.findById(itemReq.productId).getOrThrowNotFound(ProductNotFoundException())

            val itemAmount= product.price
            val itemAmountTotal =  itemAmount.multiply(BigDecimal(itemReq.count))
            calculatedTotalAmount = calculatedTotalAmount.add(itemAmountTotal)
            productRepository.deductCount(product.id, itemReq.count)


            userRepository.checkBalance(user.id, itemAmountTotal) ?: throw InsufficientFundsException()
            val transactionItem = TransactionItem(
                product = product,
                count = itemReq.count.toLong(),
                amount = itemAmount,
                totalAmount = itemAmountTotal,
                transaction = fakeTransaction
            )
            transactionItems.add(transactionItem)
        }
        val transaction = Transaction(
            user = user,
            totalAmount = calculatedTotalAmount,

        )

        val savedTransaction = repository.save(transaction)

        for (item in transactionItems) {
            item.transaction = savedTransaction
        }
        userRepository.deductBalance(user.id, calculatedTotalAmount)
        transactionItemRepository.saveAll(transactionItems)
        userPaymentRepository.save(UserPaymentTransaction(
            user = user,
            amount = calculatedTotalAmount,
        ))
    }

    override fun getOne(id: Long): TransactionFullInformation {
        val findNotDeleted = repository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        return mapper.toDto(findNotDeleted)
    }

    override fun getUserAllTransaction(userId: Long): List<TransactionFullInformation> {
        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()
        val responseTransactions = mutableListOf<TransactionFullInformation>()
        for (transaction in repository.findByUser(user)) {
            responseTransactions.add(mapper.toDto(transaction))
        }
        return responseTransactions
    }

    override fun getUserBoughtProducts(userId: Long): MutableList<TransactionItemFullInfo> {
        val user = userRepository.findById(userId).getOrThrowNotFound(UserNotFoundException())
        val responseTransactionItems: MutableList<TransactionItemFullInfo> = mutableListOf()
        val userBoughtProducts = transactionItemRepository.getUserBoughtProducts(user.id)

        userBoughtProducts.run {
            for (projection in userBoughtProducts) {
                transactionItemMapper.toTransactionItemFullInfo(projection).run {
                    responseTransactionItems.add(this)
                }
            }
        }
        return responseTransactionItems

    }
}
//Transaction Service
//Product service
interface ProductService{
    fun create(request: ProductCreateRequest): Any
    fun getAll(): List<ProductFullInfo>
    fun getOne(id: Long): ProductFullInfo
    fun update(id: Long, updateBody: ProductUpdateRequest)
    fun delete(id: Long)
}
@Service
class ProductServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val repository: ProductRepository,
    private val mapper: ProductMapper,
) : ProductService {

    override fun create(request: ProductCreateRequest){
        request.run {
            val category = categoryRepository.findByIdAndDeletedFalse(request.categoryId) ?:
                throw CategoryNotFoundException()
            repository.save(mapper.toEntity(request, category))
        }
    }

    override fun getAll(): List<ProductFullInfo> {
        val response: MutableList<ProductFullInfo> = mutableListOf()
        repository.findAll().forEach {product ->
            mapper.toProductFullInfo(product).run {
                response.add(this)
            }
        }
        return response
    }

    override fun getOne(id: Long): ProductFullInfo {
        val product = repository.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException()

        return product.run {
            mapper.toProductFullInfo(product)
        }

    }

    override fun update(id: Long, updateBody: ProductUpdateRequest) {
        val product = repository.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException()
        updateBody.run {
            this.categoryId?.let {
               val category =  categoryRepository.findByIdAndDeletedFalse(this.categoryId) ?: throw CategoryNotFoundException()
                product.category = category
            }
            this.name?.let { product.name = name}
            this.count?.let { product.count = count}
            this.prince?.let { product.price = prince}
        }
        repository.save(product)
    }

    override fun delete(id: Long) {
        repository.trash(id) ?: throw ProductNotFoundException()
    }

}
//Product service

//UserPaymentTransaction
interface UserPaymentTransactionService{
    fun deposit(userId: Long, amount: BigDecimal)
    fun depositHistory(userId: Long): MutableList<UserPaymentTransactionFullInfo>
}

@Service
class UserPaymentTransactionServiceImpl(
    private val userRepository: UserRepository,
    private val repository: UserPaymentTransactionRepository,
    private val mapper: UserPaymentTransactionMapper,
): UserPaymentTransactionService{

    override fun deposit(userId: Long, amount: BigDecimal) {
        val user = userRepository.findByIdAndDeletedFalse(userId)
            ?: throw UserNotFoundException()
        user.run {
            this.balance += amount
            repository.save(UserPaymentTransaction(
                user = user,
                amount = amount,
            ))
        }
    }

    override fun depositHistory(userId: Long): MutableList<UserPaymentTransactionFullInfo> {
        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()
        val responsePayments = mutableListOf<UserPaymentTransactionFullInfo>()
        for (payment in repository.findByUser(user)) {
            responsePayments.add(mapper.toUserPaymentTransactionFullInfo(payment))
        }
        return responsePayments
    }

}
//UserPaymentTransaction