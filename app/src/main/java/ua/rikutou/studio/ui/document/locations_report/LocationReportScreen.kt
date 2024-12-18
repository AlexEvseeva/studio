package ua.rikutou.studio.ui.document.locations_report

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ua.rikutou.studio.R
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.components.dateFormatter
import ua.rikutou.studio.ui.document.PdfCreator
import ua.rikutou.studio.ui.document.lease.DocumentCreate
import java.util.Date

@Composable
fun LocationReportScreen(
    viewModel: LocationsReportViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is LocationsReport.Event.OnMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                is LocationsReport.Event.OnNavigate -> {
                    navController.navigateUp()
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LocationReportScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun LocationReportScreenContent(
    state: LocationsReport.State,
    onAction: (LocationsReport.Action) -> Unit
) {
    val context = LocalContext.current
    val pdfCreator by remember { mutableStateOf(PdfCreator()) }

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            it.data?.data?.let { uri ->
                context.contentResolver.openOutputStream(uri)?.let { stream ->
                    stream.use {
                        pdfCreator.createLocationsReport(
                            outputStream = it,
                            title = context.getString(R.string.vacantLocationsReport),
                            locations = state.report?.locations ?: emptyList(),
                            locationsStatistics = String
                                .format(
                                    context.getString(R.string.locationStatistics),
                                    state.report?.locationsCount ?: 0,
                                    state.report?.locationsCountTotal ?: 0),
                            created = String.format(context.getString(R.string.documentCreationDate), dateFormatter.format(Date()))
                        )
                    }
                }
                onAction(LocationsReport.Action.OnCreateDocument)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(state.report?.locations?.isNotEmpty() == true) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = String
                    .format(
                        context.getString(R.string.locationStatistics),
                        state.report.locationsCount,
                        state.report.locationsCountTotal),
                fontWeight = FontWeight(700),
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1F)
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = state.report.locations, key = {it.locationId}) { item ->
                    Item(
                        title = item.name,
                        comment = item.address,
                        onItemClick = {},
                        onAddToCart = {}
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    documentLauncher.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_TITLE, "Locations report.pdf")
                        }
                    )
            }) {
                Text(
                    text = stringResource(R.string.createDocument)
                )
            }
        } else {
            Text(
                text = stringResource(R.string.locationsReportIsEmpty)
            )
        }
    }
}