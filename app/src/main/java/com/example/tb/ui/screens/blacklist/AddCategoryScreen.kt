package com.example.tb.ui.screens.blacklist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R

data class AddableCategory(
    val id: Int,
    val name: String,
    val iconResId: Int, // Изменено: теперь храним ID ресурса
    var isSelected: Boolean
)

@Composable
fun AddCategoryScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (List<AddableCategory>) -> Unit = {}
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    // Инициализируем список категорий с ID ресурсов для иконок
    val initialCategories = remember {
        listOf(
            AddableCategory(1, "Одежда", R.drawable.vector1, true),
            AddableCategory(2, "Техника", R.drawable.vector3, true),
            AddableCategory(3, "Игры", R.drawable.ic_videogame_asset_48px, false),
            AddableCategory(4, "Косметика", R.drawable.brush_fill, false),
            AddableCategory(5, "Ювелирные изделия", R.drawable.vector2, false),
            AddableCategory(6, "Электроника", R.drawable.vector3, false), // можно использовать ту же иконку
            AddableCategory(7, "Книги", R.drawable.vector3, false),
            AddableCategory(8, "Спорт", R.drawable.vector3, false),
            AddableCategory(9, "Мебель", R.drawable.vector3, false),
            AddableCategory(10, "Путешествия", R.drawable.vector3, false)
        )
    }

    var categories by remember { mutableStateOf(initialCategories) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Стрелка назад
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 43.dp)
                .clickable { onBackClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.path1),
                contentDescription = "Стрелка назад",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(12.dp, 24.dp)
                    .align(Alignment.Center)
            )
        }

        // Заголовок
        Text(
            text = "Добавить категорию",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 41.dp)
        )

        // Поле поиска
        Box(
            modifier = Modifier
                .width(296.dp)
                .height(40.dp)
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF333333))
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 40.dp, top = 10.dp, bottom = 10.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (searchText.text.isEmpty()) {
                            Text(
                                text = "Поиск",
                                color = Color(0xFFAAAAAA),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // Иконка микрофона
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .background(Color.White)
            )
        }

        // Список категорий
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 149.dp, bottom = 120.dp),
            contentPadding = PaddingValues(horizontal = 27.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filteredCategories = if (searchText.text.isEmpty()) {
                categories
            } else {
                categories.filter {
                    it.name.contains(searchText.text, ignoreCase = true)
                }
            }

            items(filteredCategories) { category ->
                AddableCategoryItem(
                    category = category,
                    onToggle = {
                        categories = categories.map { cat ->
                            if (cat.id == category.id) cat.copy(isSelected = !cat.isSelected)
                            else cat
                        }
                    }
                )
            }
        }

        // Кнопка "Сохранить"
        Button(
            onClick = {
                val selectedCategories = categories.filter { it.isSelected }
                onSaveClick(selectedCategories)
            },
            modifier = Modifier
                .width(296.dp)
                .height(106.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDD2D),
                contentColor = Color(0xFF141414)
            )
        ) {
            Text(
                text = "Сохранить",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AddableCategoryItem(
    category: AddableCategory,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Иконка категории (теперь картинка)
                Image(
                    painter = painterResource(id = category.iconResId),
                    contentDescription = category.name,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )

                // Название категории
                Text(
                    text = category.name,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Чекбокс
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        if (category.isSelected) Color(0xFF2A64D9)
                        else Color(0xFF494949)
                    )
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (category.isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}