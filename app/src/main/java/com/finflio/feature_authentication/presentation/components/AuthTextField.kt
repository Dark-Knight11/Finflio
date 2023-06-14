package com.finflio.feature_authentication.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.Poppins

@Composable
fun AuthTextField(
    text: String,
    placeholder: String,
    leadingIcon: Int,
    trailingIcon: (() -> Int)? = null,
    onTextChange: (String) -> Unit,
    transformation: VisualTransformation? = null,
    toggleVisibility: (() -> Unit)? = null
) {
    if (transformation != null && toggleVisibility != null) {
        TextField(
            value = text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = "name",
                    tint = Color.White
                )
            },
            trailingIcon = {
                val icon = trailingIcon?.invoke()
                if (icon != null) {
                    IconButton(
                        onClick = toggleVisibility
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "name",
                            tint = Color.White
                        )
                    }
                }
            },
            onValueChange = onTextChange,
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Poppins,
                    color = Color.White.copy(0.8f)
                )
            },
            visualTransformation = transformation,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = GoldIcon,
                cursorColor = GoldIcon,
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                unfocusedIndicatorColor = GoldIcon.copy(0.42f)
            )
        )
    } else {
        TextField(
            value = text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = "name",
                    tint = Color.White
                )
            },
            onValueChange = onTextChange,
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Poppins,
                    color = Color.White.copy(0.8f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = GoldIcon,
                cursorColor = GoldIcon,
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                unfocusedIndicatorColor = GoldIcon.copy(0.42f)
            )
        )
    }
}