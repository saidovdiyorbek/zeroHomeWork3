package zero.one.home3

import getOrThrowNotFound
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

//Category service
interface CategoryService{
    fun create(request: CategoryCreateRequest)

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
    }
//Category service

//User service
interface UserService{
    fun create(userRequest: UserRequest)
    fun getOne(id: Long): UserResponse
    fun delete(id: Long)
    fun getAll(): List<UserResponse>
    fun update(id: Long, updateBody: UpdateUserRequest)
}
@Service
class UserServiceImpl(
    private val userRepository: UserRepository
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

}
//USer service

//Transaction Service
interface TransactionService{
    fun create(request: TransactionRequest)
    fun getOne(id: Long): TransactionResponse
}

@Service
class TransactionServiceImpl(
    private val userRepository: UserRepository,
    private val repository: TransactionRepository,
    private val productRepository: ProductRepository,
    private val transactionItemRepository: TransactionItemRepository,
    private val userPaymentRepository: UserPaymentTransactionRepository
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

    override fun getOne(id: Long): TransactionResponse {
        TODO("Not yet implemented")
    }
//Transaction Service
}

//Product service
interface ProductService{
    fun create(request: ProductCreateRequest): Any
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

}
//Product service