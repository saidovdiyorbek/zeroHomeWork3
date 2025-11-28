package zero.one.home3

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
){

    @Operation(summary = "Create category")
    @PostMapping
    fun create(@RequestBody request: CategoryCreateRequest) = categoryService.create(request)

    @Operation(summary = "Get all categories")
    @GetMapping
    fun getAll(): List<CategoryFullInfo> = categoryService.getAll()

    @Operation(summary = "Get one by id")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): CategoryFullInfo = categoryService.getOne(id)

    @Operation(summary = "Update by id and body")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updateBody: CategoryUpdateRequest) = categoryService.update(id, updateBody)

    @Operation(summary = "Delete by id")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = categoryService.delete(id)
}

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    service: UserService
) {


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

    @Operation(summary = "Get user all payments")
    @PostMapping("/{id}")
    fun getUserAllPayments(@PathVariable id: Long): List<UserPaymentTransactionResponse> =
        userService.getAllPayments(id)
}

    @RestController
    @RequestMapping("/api/transactions")
    class TransactionController(
        private val transactionService: TransactionService
    ) {

        @Operation(summary = "Transaction create")
        @PostMapping
        fun create(@RequestBody transactionRequest: TransactionRequest): Any =
            transactionService.create(transactionRequest)

        @Operation(summary = "Get one transaction bu id")
        @GetMapping("/{id}")
        fun getOne(@PathVariable id: Long): TransactionFullInformation = transactionService.getOne(id)

        @Operation(summary = "Get user all transactions")
        @GetMapping("get-users-all-transactions/{userId}")
        fun getUserAllTransactions(@PathVariable userId: Long): List<TransactionFullInformation> =
            transactionService.getUserAllTransaction(userId)

        @Operation(summary = "Get user bought products by user id")
        @GetMapping("get-user-bought-products/{userId}")
        fun getUserBoughtProducts(@PathVariable userId: Long): MutableList<TransactionItemFullInfo> =
            transactionService.getUserBoughtProducts(userId)
    }



@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
){

    @Operation(summary = "Create product")
    @PostMapping
    fun create(@RequestBody request: ProductCreateRequest) = productService.create(request)

    @Operation(summary = "Get all products")
    @GetMapping
    fun getAll(): List<ProductFullInfo> = productService.getAll()

    @Operation(summary = "Get one by id")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ProductFullInfo = productService.getOne(id)

    @Operation(summary = "Update by id and body")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updateBody: ProductUpdateRequest) = productService.update(id, updateBody)

    @Operation(summary = "Delete by id")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = productService.delete(id)
}

@RestController
@RequestMapping("/api/user-payments-transaction")
class UserPaymentTransactionController(
    private val service: UserPaymentTransactionService
){

    @Operation(summary = "Deposit transaction by id")
    @PostMapping("/deposit/{userId}")
    fun deposit(@PathVariable userId: Long, @RequestParam amount: BigDecimal) =
        service.deposit(userId, amount)

    @Operation(summary = "Get user deposit history by user id")
    @GetMapping("/deposit-history/{userId}")
    fun depositHistory(@PathVariable userId: Long): MutableList<UserPaymentTransactionFullInfo> =
        service.depositHistory(userId)
}