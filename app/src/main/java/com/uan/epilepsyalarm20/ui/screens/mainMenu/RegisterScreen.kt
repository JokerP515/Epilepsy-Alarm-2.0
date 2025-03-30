package com.uan.epilepsyalarm20.ui.screens.mainMenu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.domain.models.BloodType
import com.uan.epilepsyalarm20.domain.models.BloodType.Companion.toBloodType
import com.uan.epilepsyalarm20.domain.models.DocumentType
import com.uan.epilepsyalarm20.domain.models.DocumentType.Companion.toDocumentType
import com.uan.epilepsyalarm20.domain.models.RegisterViewModel
import com.uan.epilepsyalarm20.ui.cards.ErrorDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.textFieldColors
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    go: (Any) -> Unit = {},
    navController: NavHostController? = null,
    boolean: Boolean = false
) {

    val context = LocalContext.current

    BackHandler {
        if(boolean && navController != null){
            navController.navigate(Routes.Informacion.id)
        }else {
            val activity = context as? Activity
            activity?.moveTaskToBack(true) // Mueve la app al fondo sin cerrarla
        }
    }

    var user by remember { mutableStateOf<UserEntity?>(null) }

    var documentInput by rememberSaveable { mutableStateOf("") }
    val documentTypes = DocumentType.entries
    var selectedDocumentType by rememberSaveable { mutableStateOf(documentTypes.first()) }
    val bloodTypes = BloodType.entries
    var selectedBloodType by rememberSaveable { mutableStateOf(bloodTypes.first()) }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var errorMessages by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        user = viewModel.getUser()
        user?.let {
            viewModel.name = it.nombre
            viewModel.lastName = it.apellido
            viewModel.bloodType = it.tipoDeSangre
            viewModel.documentType = it.tipoDeDocumento
            viewModel.document = it.numeroDeDocumento
            documentInput = it.numeroDeDocumento

            selectedDocumentType = toDocumentType(it.tipoDeDocumento)
            selectedBloodType = toBloodType(it.tipoDeSangre)
        }
    }

    if (showDialog) {
        ErrorDialog(errorMessages = errorMessages, onDismiss = { showDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if(!boolean) Modifier.padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        HeadlineCard(title = stringResource(R.string.registro_de_usuario))

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text(stringResource(R.string.nombre)) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )
        OutlinedTextField(
            value = viewModel.lastName ,
            onValueChange = { viewModel.lastName = it },
            label = { Text(stringResource(R.string.apellido)) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        // Tipo de documento
        EnumDropdown(
            selectedOption = selectedDocumentType,
            options = documentTypes,
            label = stringResource(R.string.tipo_de_documento),
            placeholder = stringResource(R.string.seleccionar_tipo_documento),
            onOptionSelected = { selectedDocumentType = it }
        )

        OutlinedTextField(
            value = documentInput, //viewModel.document
            onValueChange = { input ->
                if (input.all { char -> char.isDigit() }) {
                    documentInput = input
                }
            },
            label = { Text(stringResource(R.string.documento)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors()
        )

        // Tipo de sangre
        EnumDropdown(
            selectedOption = selectedBloodType,
            options = bloodTypes,
            label = stringResource(R.string.tipo_de_sangre),
            placeholder = stringResource(R.string.seleccionar_tipo_de_sangre),
            onOptionSelected = { selectedBloodType = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val missingFields = checkMissingFields(
                    viewModel.name,
                    viewModel.lastName,
                    selectedDocumentType,
                    documentInput,
                    selectedBloodType
                )
                if (missingFields.isEmpty()) {
                    viewModel.document = documentInput
                    viewModel.documentType = selectedDocumentType.toString()
                    viewModel.bloodType = selectedBloodType.toString()
                    viewModel.saveUser()
                    errorMessages = emptyList()
                    showDialog = false
                    if(!boolean){
                        go(Routes.ConfigAlarma)
                    } else showSuccessMessage = true
                } else {
                    errorMessages = missingFields
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.guardar),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    if (showSuccessMessage) {
        LaunchedEffect(true) {
            // Mostrar Snackbar utilizando SnackbarHostState
            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            showSuccessMessage = false // Ocultar el mensaje después de mostrarlo
        }
    }

    SnackbarHost(hostState = snackbarHostState)

}

fun checkMissingFields(
    name: String,
    lastName: String,
    documentType: DocumentType,
    document: String,
    bloodType: BloodType
): List<String> {
    val missing = mutableListOf<String>()
    if (name.isBlank()) missing.add("Nombre")
    if (lastName.isBlank()) missing.add("Apellido")
    if (documentType == DocumentType.SELECTOPTION) missing.add("Tipo de Documento")
    if (document.isBlank()) missing.add("Número de documento")
    if (bloodType == BloodType.SELECTOPTION) missing.add("Tipo de Sangre")
    return missing
}
