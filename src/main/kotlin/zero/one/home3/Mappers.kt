package zero.one.home3

import org.springframework.stereotype.Component

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