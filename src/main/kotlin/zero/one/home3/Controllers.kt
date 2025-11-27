package zero.one.home3

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
){

    @Operation(summary = "Create category")
    @PostMapping
    fun create(@RequestBody request: CategoryCreateRequest) = categoryService.create(request)
}

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    service: UserService
){


@Operation(summary = "Create user")
@PostMapping
fun create(@RequestBody userRequest: UserRequest) = userService.create(userRequest)

@Operation(summary = "Get one user by id")
@GetMapping("/{id}")
fun getOne(@PathVariable id: Long): UserResponse = userService.getOne(id)

@Operation(summary = "Delete user bu id")
@DeleteMapping("/{id}")
fun delete(@PathVariable id: Long) = userService.delete(id)

@Operation(summary = "Get all users")
@GetMapping
fun getAll(): List<UserResponse> = userService.getAll()

@Operation(summary = "Update user by id")
@PutMapping("/{id}")
fun update(@PathVariable id: Long, @RequestBody updateBody: UpdateUserRequest) =
userService.update(id, updateBody)
}

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
private val transactionService: TransactionService
){

@Operation(summary = "Transaction create")
@PostMapping
fun create(@RequestBody transactionRequest: TransactionRequest): Any = transactionService.create(transactionRequest)

@Operation(summary = "Get one transaction bu id")
@GetMapping("/{id}")
fun getOne(@PathVariable id: Long): TransactionResponse = transactionService.getOne(id)
}

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
){

@Operation(summary = "Create product")
@PostMapping
fun create(@RequestBody request: ProductCreateRequest) = productService.create(request)
}