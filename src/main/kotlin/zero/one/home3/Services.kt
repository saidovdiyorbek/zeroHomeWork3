package zero.one.home3

import org.springframework.stereotype.Service

//Category service
interface CategoryService{

}

@Service
class CategoryServiceImpl : CategoryService {}
//Category service

//User service
interface UserService{
    fun create(userRequest: UserRequest): Any
}
@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(userRequest: UserRequest): Any {
        if(userRepository.existsByUsername(userRequest.username)){
           throw UserAlreadyExistsException()
        }

        userRepository.save(User(fullname = userRequest.username,
            username = userRequest.username,
            balance = "0.0".toBigDecimal(),
            role = UserRole.USER))
        return "User created"
    }
}
//USer service