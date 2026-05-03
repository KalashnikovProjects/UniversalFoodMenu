package com.kalashnikovprojects.ufmserver.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
data class Category(
    val id: Int,
    val name: Int,
) : Designable

fun Category.toNoIdCategory() = NoIdCategory(
    name = name,
)