package com.example.tb.ui.screens.blacklist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
    val iconResId: Int,
    var isSelected: Boolean,
    val isCustom: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (List<AddableCategory>) -> Unit = {}
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showAddCustomDialog by remember { mutableStateOf(false) }
    var customCategoryName by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    // Инициализируем список категорий
    val initialCategories = remember {
        listOf(
            AddableCategory(1, "Одежда", R.drawable.vector1, true),
            AddableCategory(2, "Техника", R.drawable.vector3, true),
            AddableCategory(3, "Игры", R.drawable.ic_videogame_asset_48px, false),
            AddableCategory(4, "Косметика", R.drawable.brush_fill, false),
            AddableCategory(5, "Ювелирные изделия", R.drawable.vector2, false),
            AddableCategory(6, "Электроника", R.drawable.vector3, false),
            AddableCategory(7, "Книги", R.drawable.vector3, false),
            AddableCategory(8, "Спорт", R.drawable.vector3, false),
            AddableCategory(9, "Мебель", R.drawable.vector3, false),
            AddableCategory(10, "Путешествия", R.drawable.vector3, false)
        )
    }

    var categories by remember { mutableStateOf(initialCategories) }
    var customCategories by remember { mutableStateOf<List<AddableCategory>>(emptyList()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp) // Добавляем отступ сверху чтобы не залазило под status bar
        ) {
            // Шапка экрана
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                // Стрелка назад
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.path1),
                        contentDescription = "Стрелка назад",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(12.dp, 24.dp)
                    )
                }

                // Заголовок
                Text(
                    text = "Добавить категорию",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Поле поиска
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF333333))
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp, // Увеличил размер текста
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 40.dp),
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
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Иконка микрофона
                Image(
                    painter = painterResource(id = R.drawable.temp_cold_line), // Замени на свою иконку микрофона
                    contentDescription = "Микрофон",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                )
            }

            // Кнопка добавления своей категории
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp, vertical = 16.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFF2A64D9))
                    .clickable {
                        showAddCustomDialog = true
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Добавить свою категорию",
                        color = Color.White,
                        fontSize = 14.sp, // Увеличил размер текста
                        fontWeight = FontWeight.Medium
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color(0xFF2A64D9),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Список категорий
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 27.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val allCategories = categories + customCategories
                val filteredCategories = if (searchText.text.isEmpty()) {
                    allCategories
                } else {
                    allCategories.filter {
                        it.name.contains(searchText.text, ignoreCase = true)
                    }
                }

                items(filteredCategories) { category ->
                    AddableCategoryItem(
                        category = category,
                        onToggle = {
                            if (category.isCustom) {
                                customCategories = customCategories.map { cat ->
                                    if (cat.id == category.id) cat.copy(isSelected = !cat.isSelected)
                                    else cat
                                }
                            } else {
                                categories = categories.map { cat ->
                                    if (cat.id == category.id) cat.copy(isSelected = !cat.isSelected)
                                    else cat
                                }
                            }
                        }
                    )
                }
            }
        }

        // Кнопка "Сохранить" - фиксированная внизу экрана
        Button(
            onClick = {
                val selectedCategories = (categories + customCategories).filter { it.isSelected }
                onSaveClick(selectedCategories)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Фиксированная высота
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDD2D),
                contentColor = Color(0xFF141414)
            )
        ) {
            Text(
                text = "Сохранить",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Bottom Sheet для добавления своей категории
    if (showAddCustomDialog) {
        ModalBottomSheet(
            onDismissRequest = { showAddCustomDialog = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1A1A1A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .padding(bottom = 24.dp), // Отступ для безопасной зоны
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Добавить свою категорию",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Поле для ввода названия категории
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Увеличил высоту
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF333333))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = customCategoryName,
                        onValueChange = { customCategoryName = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (customCategoryName.isEmpty()) {
                                    Text(
                                        text = "Введите название категории",
                                        color = Color(0xFFAAAAAA),
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка добавить
                Button(
                    onClick = {
                        if (customCategoryName.isNotBlank()) {
                            val newCustomCategory = AddableCategory(
                                id = (customCategories.maxOfOrNull { it.id } ?: 100) + 1,
                                name = customCategoryName,
                                iconResId = R.drawable.vector3, // Иконка по умолчанию
                                isSelected = true,
                                isCustom = true
                            )
                            customCategories = customCategories + newCustomCategory
                            customCategoryName = ""
                            showAddCustomDialog = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Увеличил высоту
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A64D9),
                        contentColor = Color.White
                    ),
                    enabled = customCategoryName.isNotBlank()
                ) {
                    Text(
                        text = "Добавить",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Кнопка отмены
                Button(
                    onClick = {
                        showAddCustomDialog = false
                        customCategoryName = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Увеличил высоту
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Отмена",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
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
            .height(56.dp) // Увеличил высоту элемента
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp), // Более скругленные углы
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Иконка категории
                Image(
                    painter = painterResource(id = category.iconResId),
                    contentDescription = category.name,
                    modifier = Modifier.size(24.dp), // Увеличил иконку
                    contentScale = ContentScale.Fit
                )

                // Название категории
                Text(
                    text = category.name,
                    color = Color.White,
                    fontSize = 14.sp, // Увеличил размер текста
                    fontWeight = FontWeight.Medium
                )

                // Маркер для пользовательской категории
                if (category.isCustom) {
                    Text(
                        text = "моя",
                        color = Color(0xFF2A64D9),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF2A64D9).copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Чекбокс
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (category.isSelected) Color(0xFF2A64D9)
                            else Color(0xFF494949)
                        ),
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
}