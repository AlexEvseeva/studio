package ua.rikutou.studio.ui.document

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.config.floatFieldMaxLength
import ua.rikutou.studio.ui.components.DatePickerModal
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.components.dateFormatter
import java.io.File
import java.util.Date

@Composable
fun DocumentCreateScreen(
    viewModel: DocumentCreateViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is DocumentCreate.Event.OnNavigate -> {
                    it.destination?.let {
                        navController.navigate(it)
                    } ?: run {
                        navController.popBackStack()
                    }
                }

                is DocumentCreate.Event.OnMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    DocumentCreateScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@SuppressLint("Recycle")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentCreateScreenContent(
    state: DocumentCreate.State,
    onAction: (DocumentCreate.Action) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pdfCreator = PdfCreator(context)

    val header = stringResource(R.string.documentHeader)
    val message = String.format(stringResource(R.string.documentMessage), state.studio?.name ?: "")
    val locationsTitle = stringResource(R.string.documentLocationsLabel)
    val transportTitle = stringResource(R.string.transportTitle)
    val equipmentTitle = stringResource(R.string.equipmentScreenTitle)
    val sum = stringResource(R.string.sumFooter)

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            it.data?.data?.let { uri ->
                context.contentResolver.openOutputStream(uri)?.let { stream ->
                    coroutineScope.launch {
                        pdfCreator.create(
                            outputStream = stream,
                            header = header,
                            message = message,
                            studio = state.studio,
                            locationsTitle = locationsTitle ,
                            locations = state.locations,
                            transportTitle = transportTitle,
                            transport = state.transport,
                            equipmentTitle = equipmentTitle,
                            equipment = state.equipment,
                            fromDate = state.fromDate ?: Date(),
                            toDays = state.toDays,
                            sumFooter = "$sum ${(state.equipmentSum + state.locationSum + state.transportSum) * state.toDays}"
                        )
                    }
                }
                onAction(DocumentCreate.Action.OnCreateDocument)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var showDatePickerDialog by remember { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if(state.locations.isNotEmpty()) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .alpha(0.8F)
                        ,
                        text = stringResource(R.string.documentLocationsLabel)
                    )
                }
                items(state.locations) { item ->
                    Item(
                        title = item.location.name,
                        comment = item.location.address,
                        onItemClick = {

                        },
                        onAddToCart = {
                            onAction(DocumentCreate.Action.OnRemoveLocationFromCart(locationId = item.location.locationId))
                        }
                    )
                }
            }

            if(state.transport.isNotEmpty()) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .alpha(0.8F)
                        ,
                        text = stringResource(R.string.transportTitle)
                    )
                }
                items(state.transport) { item ->
                    Item(
                        title = item.mark,
                        comment = item.technicalState,
                        onItemClick = {

                        },
                        onAddToCart = {
                            onAction(DocumentCreate.Action.OnRemoveTransportFromCart(transportId = item.transportId))
                        }
                    )

                }
            }

            if(state.equipment.isNotEmpty()) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .alpha(0.8F)
                        ,
                        text = stringResource(R.string.equipmentScreenTitle)
                    )
                }
                items(state.equipment) { item ->
                    Item(
                        title = item.name,
                        comment = item.comment,
                        onItemClick = {

                        },
                        onAddToCart = {
                            onAction(DocumentCreate.Action.OnRemoveEquipmentFromCart(equipmentId = item.equipmentId))
                        }
                    )

                }
            }


        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        showDatePickerDialog = true
                    }
                ,
                text = state.fromDate?.let { dateFormatter.format(it) } ?: "YYYY-MM-DD"
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 32.dp)
                ,
                value = state.toDays.toString(),
                onValueChange = {
                    it
                        .filter { it.toString().isNotEmpty() && it.toString().length < 5}
                        .runCatching { it.toInt() }.getOrNull()?.let { days ->
                            onAction(DocumentCreate.Action.OnSelectToDays(days = days))
                        }
                },
                label = {
                    Text(
                        text = "To days"
                    )
                }
            )
        }

        if(showDatePickerDialog) {
            DatePickerModal(
                onDateSelected = {
                    showDatePickerDialog = false
                    it?.let {
                        onAction(DocumentCreate.Action.OnSelectFromDate(time = it))
                    }
                },
                onDismiss = {
                    showDatePickerDialog = false
                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                state.fromDate?.let {
                    documentLauncher.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_TITLE, "Lease_agreement.pdf")
                        }
                    )
                } ?: run {
                    onAction(DocumentCreate.Action.OnMessage(message = "Please select start date"))
                }

            }
        ) {
            Text(
                text = stringResource(R.string.export)
            )
        }
    }


}
