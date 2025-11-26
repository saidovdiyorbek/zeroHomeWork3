package zero.one.home3

import getOrThrowNotFound
import org.springframework.stereotype.Service
import java.util.Date

//Category service
interface CategoryService{

}

@Service
class CategoryServiceImpl : CategoryService {}
//Category service

//User service
interface UserService{
    fun create(userRequest: UserRequest): Any
    fun getOne(id: Long): UserResponse
    fun delete(id: Long): BaseMessage
    fun getAll(): List<UserResponse>
    fun update(id: Long, updateBody: UpdateUserRequest): BaseMessage
}
@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(userRequest: UserRequest): Any {
        if(userRepository.existsByUsername(userRequest.username)){
           throw UserAlreadyExistsException()
        }

        userRepository.save(User(fullname = userRequest.fullname,
            username = userRequest.username,
            balance = "0.0".toBigDecimal(),
            role = UserRole.USER))
        return "User created"
    }

    override fun getOne(id: Long): UserResponse {
        val findById = userRepository.findById(id)
        if (findById.isPresent){
            val user = findById.get()
            return UserResponse(user.fullname, user.username, user.balance, user.createdDate)
        }
        throw UserNotFoundException()
    }

    override fun delete(id: Long): BaseMessage {
        val findById = userRepository.findById(id)
        if (findById.isPresent){
            userRepository.deleteById(id)
            return BaseMessage(200, "User deleted")
        }
        throw UserNotFoundException()
    }

    override fun getAll(): List<UserResponse> {
        val responseUsers: MutableList<UserResponse> = mutableListOf()

        userRepository.findAll().forEach {
            responseUsers.add(UserResponse(it.fullname, it.username, it.balance, it.createdDate))
        }
        return responseUsers
    }

    override fun update(id: Long, updateBody: UpdateUserRequest): BaseMessage {
        val findById = userRepository.findById(id)
        if (findById.isPresent){
            val getUser = findById.get()
            getUser.fullname = updateBody.fullname.toString()
            getUser.username = updateBody.username.toString()
            userRepository.save(getUser)
            return BaseMessage(200, "User updated")
        }
        throw UserNotFoundException()
    }

}
//USer service

//Transaction Service
interface TransactionService{
    fun create(transactionRequest: TransactionRequest): Any
    fun getOne(id: Long): TransactionResponse
}

@Service
class TransactionServiceImpl(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : TransactionService {
    override fun create(transactionRequest: TransactionRequest): BaseMessage {
        val findById = userRepository.findById(transactionRequest.userId)
        if (findById.isPresent){
            val user = findById.get()
            transactionRepository.save(Transaction(transactionRequest.userId, user, transactionRequest.totalAmount,))
            return BaseMessage(200, "Transaction saved")
        }
        throw UserNotFoundException()
    }

    override fun getOne(id: Long): TransactionResponse {
        val findById = transactionRepository.findById(id).getOrThrowNotFound("Transaction", id)

    }
}
//Transaction Service
