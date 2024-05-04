package com.example.youthconnect.View.Authentication


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier


@Composable
fun Policy(onConfirm: () -> Unit) {
    // Estado para controlar la visibilidad del botón de confirmación
    var isButtonEnabled by remember { mutableStateOf(false) }

    // Observador del scroll para determinar si se ha llegado al final del contenido
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = {},
        icon = { Icons.Outlined.Policy },
        title = { Text(text = "Términos y condiciones de uso") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Text("El presente Política de Privacidad establece los términos en que YouthConnect usa y protege la información que es proporcionada por sus usuarios al momento de utilizar su sitio web. Esta compañía está comprometida con la seguridad de los datos de sus usuarios. Cuando le pedimos llenar los campos de información personal con la cual usted pueda ser identificado, lo hacemos asegurando que sólo se empleará de acuerdo con los términos de este documento. Sin embargo esta Política de Privacidad puede cambiar con el tiempo o ser actualizada por lo que le recomendamos y enfatizamos revisar continuamente esta página para asegurarse que está de acuerdo con dichos cambios. \n" +
                        "Nuestra aplicación podrá recoger información personal por ejemplo: Nombre,  información de contacto como  su dirección de correo electrónica e información demográfica. Así mismo cuando sea necesario podrá ser requerida información específica para procesar algún pedido o realizar una entrega o facturación.\n" +
                        "Nuestra aplicación emplea la información con el fin de proporcionar el mejor servicio posible, particularmente para mantener un registro de usuarios, de pedidos en caso que aplique, y mejorar nuestros productos y servicios.  Es posible que sean enviados correos electrónicos periódicamente a través de nuestro sitio con ofertas especiales, nuevos productos y otra información publicitaria que consideremos relevante para usted o que pueda brindarle algún beneficio, estos correos electrónicos serán enviados a la dirección que usted proporcione y podrán ser cancelados en cualquier momento.\n" +
                        "YouthConnect está altamente comprometido para cumplir con el compromiso de mantener su información segura. Usamos los sistemas más avanzados y los actualizamos constantemente para asegurarnos que no exista ningún acceso no autorizado.\n" +
                        "YouthConnect Se reserva el derecho de cambiar los términos de la presente Política de Privacidad en cualquier momento.")
            }
        },
        confirmButton = {
            // Botón de confirmación con estado activado/desactivado según la posición del scroll
            TextButton(
                onClick = {
                    onConfirm()
                },
                enabled = isButtonEnabled // Habilitar el botón según el estado
            ) {
                Text("Aceptar y confirmar")
            }
        }
    )

    // Observador de cambios en la posición del scroll
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                // Comprobar si el scroll ha llegado al final del contenido
                val maxScroll = scrollState.maxValue
                val currentScroll = scrollPosition
                val shouldEnableButton = currentScroll >= maxScroll
                isButtonEnabled = shouldEnableButton
            }
    }
}

