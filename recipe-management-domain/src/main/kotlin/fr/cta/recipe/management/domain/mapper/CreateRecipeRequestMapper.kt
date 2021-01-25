package fr.cta.recipe.management.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper

@Mapper
interface CreateRecipeRequestMapper{
    //    fun toRecipe(createRecipeRequest: CreateRecipeUseCase.CreateRecipeRequest): Recipe
    fun toCarRecipe(carDto: CarDto): Car
}

fun main() {

    val test:CreateRecipeRequestMapper = getMapper(CreateRecipeRequestMapper::class.java)
    val carDto = CarDto()
    carDto.seatCount = 0
    carDto.make = "dsvd"
    carDto.type = "dvsdvssvd"

    print(test.toCarRecipe(carDto))
}