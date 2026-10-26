package com.uan.epilepsyalarm20.ui.screens.menu

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.domain.models.BloodType
import com.uan.epilepsyalarm20.domain.models.BloodType.Companion.toBloodType
import com.uan.epilepsyalarm20.domain.models.ContactsViewModel
import com.uan.epilepsyalarm20.domain.models.DocumentType
import com.uan.epilepsyalarm20.domain.models.DocumentType.Companion.toDocumentType
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import com.uan.epilepsyalarm20.domain.models.RegisterViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.ContactCard
import com.uan.epilepsyalarm20.ui.cards.ErrorDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.cards.ReminderCard
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.fields.ClickableUanTextField
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.utils.getContactFromUri
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    contactsViewModel: ContactsViewModel,
    emergencyViewModel: EmergencyViewModel,
    go: (Any) -> Unit = {},
    navController: NavHostController? = null,
    boolean: Boolean = false
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val typography = tokens.typography
    val spacing = tokens.spacing

    BackHandler {
        if(boolean && navController != null){
            navController.navigate(Routes.Informacion.id)
        } else {
            val activity = context as? Activity
            activity?.moveTaskToBack(true)
        }
    }

    var user by remember { mutableStateOf<UserEntity?>(null) }

    var documentInput by rememberSaveable { mutableStateOf("") }
    val documentTypes = DocumentType.entries
    var selectedDocumentType by rememberSaveable { mutableStateOf(documentTypes.first()) }
    val bloodTypes = BloodType.entries
    var selectedBloodType by rememberSaveable { mutableStateOf(bloodTypes.first()) }

    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var contact by remember { mutableStateOf<EmergencyContactEntity?>(null) }

    var pendingSmsPhone by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var errorMessages by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val pickContactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        val selectedContact = getContactFromUri(context, uri)

        if (selectedContact != null) {
            name = selectedContact.first
            phone = selectedContact.second
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickContactLauncher.launch()
        } else {
            Toast.makeText(
                context,
                "Se requiere permiso de contactos para seleccionar uno",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val requestSmsAndPhonePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val smsGranted = permissions[Manifest.permission.SEND_SMS] ?: false
        val phoneStateGranted = permissions[Manifest.permission.READ_PHONE_STATE] ?: false

        if (smsGranted && phoneStateGranted) {
            pendingSmsPhone?.let { phoneNum ->
                emergencyViewModel.sendPreparedMessage(
                    phoneNumber = phoneNum,
                    emergencyMessage = "Mensaje de confirmación envío de alerta del aplicativo Epilepsy Alarm",
                    location = ""
                )
                pendingSmsPhone = null
            }
        } else {
            Toast.makeText(
                context,
                "Se requieren permisos de SMS y Teléfono para verificar la SIM Card y enviar la alerta",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        user = registerViewModel.getUser()
        user?.let {
            registerViewModel.name = it.nombre
            registerViewModel.lastName = it.apellido
            registerViewModel.bloodType = it.tipoDeSangre
            registerViewModel.documentType = it.tipoDeDocumento
            registerViewModel.document = it.numeroDeDocumento
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
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if (!boolean) {
                    Modifier
                        .padding(
                            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.systemBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                } else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(spacing.xs)
    ) {

        HeadlineCard(
            title = stringResource(R.string.registro_de_usuario),
            description = stringResource(R.string.explicacion_registro_usuario)
        )

        if(!boolean) {
            ReminderCard(
                title = stringResource(R.string.recordatorio_sms)
            )
        }

        ClickableUanTextField(
            value = registerViewModel.name,
            onValueChange = { registerViewModel.name = it },
            label = stringResource(R.string.nombre),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Next) })
        )

        ClickableUanTextField(
            value = registerViewModel.lastName,
            onValueChange = { registerViewModel.lastName = it },
            label = stringResource(R.string.apellido),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Next) })
        )

        // Tipo de sangre
        EnumDropdown(
            selectedOption = selectedBloodType,
            options = bloodTypes,
            label = stringResource(R.string.tipo_de_sangre),
            placeholder = stringResource(R.string.seleccionar_tipo_de_sangre),
            onOptionSelected = { selectedBloodType = it }
        )

        // Tipo de documento
        EnumDropdown(
            selectedOption = selectedDocumentType,
            options = documentTypes,
            label = stringResource(R.string.tipo_de_documento),
            placeholder = stringResource(R.string.seleccionar_tipo_documento),
            onOptionSelected = { selectedDocumentType = it }
        )

        ClickableUanTextField(
            value = documentInput,
            onValueChange = { input ->
                if (input.all { char -> char.isDigit() }) {
                    documentInput = input
                }
            },
            label = stringResource(R.string.documento),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
        )

        if(!boolean) {
            BasicText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colors.onSurface)) {
                        append("Puedes agregar más ")
                    }
                    withStyle(style = SpanStyle(color = colors.primary)) {
                        append("contactos de emergencia")
                    }
                    withStyle(style = SpanStyle(color = colors.onSurface)) {
                        append(" desde la pestaña de ")
                    }
                    withStyle(style = SpanStyle(color = colors.primary)) {
                        append("configuración.")
                    }
                },
                style = typography.component.copy(
                    textAlign = TextAlign.Center
                )
            )
            if (name.isBlank()) {
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Nuevo Contacto",
                ) {
                    val permissionStatus = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_CONTACTS
                    )

                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        pickContactLauncher.launch()
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                }
            } else {
                LaunchedEffect(name) {
                    contact = emergencyViewModel.getFirstEmergencyContact()
                        ?: EmergencyContactEntity(userId = 0, phoneNumber = phone, name = name)
                }

                contact?.let { safeContact ->
                    ContactCard(
                        contact = safeContact,
                        onDelete = {
                            contactsViewModel.deleteEmergencyContact(safeContact)
                            name = ""
                            phone = ""
                            contact = null
                        },
                        onUpdate = { updatedContact ->
                            contactsViewModel.updateEmergencyContact(updatedContact)
                            contact = updatedContact
                        },
                        onSendSms = { phoneNum ->
                            val hasSmsPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.SEND_SMS
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasSmsPermission) {
                                emergencyViewModel.sendPreparedMessage(
                                    phoneNumber = phoneNum,
                                    emergencyMessage = "Mensaje de confirmación envío de alerta del aplicativo Epilepsy Alarm",
                                    location = ""
                                )
                            } else {
                                pendingSmsPhone = phoneNum
                                requestSmsAndPhonePermissionsLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.SEND_SMS,
                                        Manifest.permission.READ_PHONE_STATE
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = if(!boolean) stringResource(R.string.continuar) else stringResource(R.string.guardar),
            enabled = contact?.isConfirmed ?: boolean
        ) {
            val missingFields = checkMissingFields(
                registerViewModel.name,
                registerViewModel.lastName,
                selectedDocumentType,
                documentInput,
                selectedBloodType
            )

            var missingFieldsContact = checkMissingFieldsContact(name, phone)
            if(boolean) missingFieldsContact = emptyList()

            if (missingFields.isEmpty() && missingFieldsContact.isEmpty()) {
                registerViewModel.document = documentInput
                registerViewModel.documentType = selectedDocumentType.toString()
                registerViewModel.bloodType = selectedBloodType.toString()

                if(boolean) {
                    registerViewModel.updateUser()
                } else {
                    registerViewModel.saveUser()
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        val isConfirmed = contact?.isConfirmed ?: true
                        contactsViewModel.insertEmergencyContact(name, phone, isConfirmed)
                    }
                }

                contact = null
                name = ""
                phone = ""

                errorMessages = emptyList()
                showDialog = false

                if(!boolean){
                    go(Routes.ConfigAlarma)
                } else {
                    showSuccessMessage = true
                }
            } else {
                errorMessages = missingFields + missingFieldsContact
                showDialog = true
            }
        }
    }

    if (showSuccessMessage) {
        LaunchedEffect(true) {
            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            showSuccessMessage = false
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

fun checkMissingFieldsContact(name: String, phoneNumber: String): List<String> {
    val missing = mutableListOf<String>()
    if(name.isBlank() || phoneNumber.isBlank())
        missing.add("Contacto de emergencia")
    return missing
}