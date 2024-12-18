package ua.rikutou.studio.ui.document

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldLineLimits
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.ListItem
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.ui.components.dateFormatter
import java.io.File
import java.io.OutputStream
import java.util.Date

class PdfCreator(private val context: Context) {
    fun create(
        outputStream: OutputStream,
        header: String,
        message: String,
        locationsTitle: String,
        studio: StudioEntity? = null,
        locations: List<Location>? = null,
        transportTitle: String,
        transport: List<TransportEntity>? = null,
        equipmentTitle: String,
        equipment: List<EquipmentEntity>? = null,
        sumFooter: String,
        fromDate: Date,
        toDays: Int,
        created: String,
    ) {
        val headerFontSize = 20F
        val messageFontSize = 16F
        val listTitleFontSize = 12F
        val listCommentFontSize = 10F
        val listMargin = 8F

        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument, PageSize.A4)

        val page = pdfDocument.addNewPage()

        val headerParagraph = Paragraph(header)
            .setFontSize(headerFontSize)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(DeviceRgb.BLACK)

        val messageParagraph = Paragraph(message)
            .setFontSize(messageFontSize)
            .setTextAlignment(TextAlignment.JUSTIFIED)

        document.add(
            headerParagraph
        )
        document.add(LineSeparator(SolidLine(1F)))
        document.add(messageParagraph)

        if(locations?.isNotEmpty() == true) {
            document.add(
                Paragraph(locationsTitle)
                    .setBold()
            )
            locations.forEach { item ->
                document.add(
                    Paragraph(item.location.name)
                        .setFontSize(listTitleFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK)
                        .setMargins(listMargin, 0f, 0f, 0f)
                )
                document.add(
                    Paragraph(item.location.address)
                        .setFontSize(listCommentFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK, 50F)
                        .setMargins(0f, 0f, listMargin, 0f)
                )
            }
        }

        if(transport?.isNotEmpty() == true) {
            document.add(
                Paragraph(transportTitle)
                    .setBold()
            )
            transport.forEach { item ->
                document.add(
                    Paragraph(item.mark)
                        .setFontSize(listTitleFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK)
                        .setMargins(listMargin, 0f, 0f, 0f)
                )
                document.add(
                    Paragraph(item.technicalState)
                        .setFontSize(listCommentFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK, 50F)
                        .setMargins(0f, 0f, listMargin, 0f)
                )
            }
        }

        if(equipment?.isNotEmpty() == true) {
            document.add(
                Paragraph(equipmentTitle)
                    .setBold()
            )
            equipment.forEach { item ->
                document.add(
                    Paragraph(item.name)
                        .setFontSize(listTitleFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK)
                        .setMargins(listMargin, 0f, 0f, 0f)
                )
                document.add(
                    Paragraph(item.comment)
                        .setFontSize(listCommentFontSize)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontColor(DeviceRgb.BLACK, 50F)
                        .setMargins(0f, 0f, listMargin, 0f)
                )
            }
        }

        document.add(
            Paragraph("From ${dateFormatter.format(fromDate)} to $toDays days")
        )

        document.add(LineSeparator(SolidLine(1F)))

        document.add(
            Paragraph(sumFooter)
                .setBold()
        )

        document.add(
            Paragraph(created)
        )



        document.close()
    }
}