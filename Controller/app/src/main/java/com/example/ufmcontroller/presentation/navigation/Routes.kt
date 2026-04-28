package com.example.ufmcontroller.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

//@Serializable
//data class ItemRoute(val id: Int)

// TODO: следующие экраны:
//  0. LoginScreen - вход в аккаунт: Название заведения, пароль (регистрация). Всегда показывается при первом входе.
//  1. HomeScreen - тут из весёлого поиск, переключение плиток и списка.
//  2. MenuEditScreen - редактирование меню, добавление и изменение позиций
//  3. VisualConfigurationScreen - отображение на TV,
//      отсюда могут быть кнопки на ещё несколько экранов, например:
//      3.1 TVConfigurationScreen - редактор расположения телеков (какой слева, какой справа)
//      3.2 EditComponentVisual -
//  4. Настройки самого приложения (не знаю зачем, может тёмную тему туда)
//  5. О приложении - ссылка на мой гитхаб, имя разработчика

// Сделать один общий компонент - меню слева выезжающее, где переключение между экранами.
