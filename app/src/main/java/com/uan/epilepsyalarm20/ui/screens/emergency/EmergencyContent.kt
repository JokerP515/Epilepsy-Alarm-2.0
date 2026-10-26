package com.uan.epilepsyalarm20.ui.screens.emergency

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanCard
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import com.uan.epilepsyalarm20.ui.buttons.FloatingBottomMenu
import com.uan.epilepsyalarm20.ui.cards.ReminderCard

@Composable
fun EmergencyContent(
    paddingValues: PaddingValues,
    viewModel: EmergencyViewModel,
    context: Context,
    user: UserEntity,
    firstEmergencyContact: EmergencyContactEntity?
){
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    val typography = tokens.typography
    
    val userInstructions = viewModel.getUserInstructions()
    val userMessage = viewModel.getUserMessage()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.md) ,
        ){

            ReminderCard(
                title = "Instrucciones De Atención"
            )

            Text(
                text = "1. Mantén la calma y quédate con la persona.",
                style = typography.component,
                color = colors.primary,
            )

            Text(
                text = "2. Asegura el entorno",
                style = typography.component,
                color = colors.primary,
            )

            Text(
                text = "3. Colócala de lado",
                style = typography.component,
                color = colors.primary,
            )

            Text(
                text = "4. No restrinjas ni pongas nada en su boca",
                style = typography.component,
                color = colors.primary,
            )

            Text(
                text = "5. Mide el tiempo",
                style = typography.component,
                color = colors.primary,
            )

            Text(
                text = "6. Tras la convulsión, tranquilízala y ayúdala a reorientarse.",
                style = typography.component,
                color = colors.primary,
            )

            userMessage?.let {
                UanCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.mensaje_personalizado),
                    body = it,
                )
            }

            userInstructions?.let {
                UanCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.instrucciones_personalizadas),
                    body = it,
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = colors.primary,
                thickness = 5.dp
            )

            Text(
                text = "Nombre: ${user.nombre}",
                style = typography.component,
                color = colors.primary
            )

            Text(
                text = "Documento: ${user.numeroDeDocumento}",
                style = typography.component,
                color = colors.primary
            )

            Text(
                text = "Tipo de Sangre: ${user.tipoDeSangre}",
                style = typography.component,
                color = colors.primary
            )

            firstEmergencyContact?.let {
                Text(
                    text = stringResource(R.string.contacto_de_emergencia),
                    style = typography.component,
                    color = colors.primary
                )
                Text(
                    text = "Nombre: ${it.name}",
                    style = typography.component,
                    color = colors.primary
                )
                Text(
                    text = "Teléfono: ${it.phoneNumber}",
                    style = typography.component,
                    color = colors.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        FloatingBottomMenu(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onPauseClick = {
                viewModel.pauseEmergency()
            },
            onStopClick = {
                viewModel.cancelEmergency()
                (context as? Activity)?.moveTaskToBack(true)
            }
        )
    }
}
