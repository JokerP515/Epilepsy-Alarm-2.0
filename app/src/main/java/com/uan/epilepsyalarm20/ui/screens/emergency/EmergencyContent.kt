package com.uan.epilepsyalarm20.ui.screens.emergency

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    val userInstructions = viewModel.getUserInstructions()
    val userMessage = viewModel.getUserMessage()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)

                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp) ,
        ){

            ReminderCard(
                title = "Instrucciones De Atención",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "1. Mantén la calma y quédate con la persona.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            Text(
                text = "2. Asegura el entorno",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            Text(
                text = "3. Colócala de lado",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            Text(
                text = "4. No restrinjas ni pongas nada en su boca",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            Text(
                text = "5. Mide el tiempo",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            Text(
                text = "6. Tras la convulsión, tranquilízala y ayúdala a reorientarse.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            )

            userMessage?.let {
                OutlinedCard (
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column (
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.mensaje_personalizado),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }

            userInstructions?.let {
                OutlinedCard (
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column (
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.instrucciones_personalizadas),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                thickness = 5.dp
            )

            Text(
                text = "Nombre: ${user.nombre}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            Text(
                text = "Documento: ${user.numeroDeDocumento}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            Text(
                text = "Tipo de Sangre: ${user.tipoDeSangre}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            firstEmergencyContact?.let {
                Text(
                    text = stringResource(R.string.contacto_de_emergencia),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = "Nombre: ${it.name}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = "Teléfono: ${it.phoneNumber}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
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
