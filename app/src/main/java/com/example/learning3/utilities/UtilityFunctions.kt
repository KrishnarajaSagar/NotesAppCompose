package com.example.learning3.utilities

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.learning3.data.Note
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object UtilityFunctions {

    fun exportNoteDialogTitle(selectedNotes: List<Note>) =
        "Export ${if (selectedNotes.size == 1) "note" else "notes"}?"

    fun exportNoteDialogBody(selectedNotes: List<Note>) = "This will download a PDF of " +
            "${if (selectedNotes.size == 1) "your" else "each"} selected note. " +
            "It will be stored in the downloads folder."

    fun exportNoteDialogSuccessMsg(selectedNotes: List<Note>) =
        "${selectedNotes.size} ${if (selectedNotes.size == 1) "note" else "notes"} exported!"

    @RequiresApi(Build.VERSION_CODES.O)
    fun Long.formatDateAndTime(): String {
        val instant = Instant.ofEpochMilli(this)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.getDefault())
        return dateFormatter.format(localDateTime)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun LazyStaggeredGridState.calculateDelayAndEasing(
        index: Int,
        columnCount: Int
    ): Pair<Int, Easing> {
        val row = index / columnCount
        val column = index % columnCount
        val firstVisibleRow = remember { derivedStateOf { firstVisibleItemIndex } }
        val visibleRows = layoutInfo.visibleItemsInfo.count()
        val scrollingToBottom = firstVisibleRow.value < row
        val isFirstLoad = visibleRows == 0
        val rowDelay = 200 * when {
            isFirstLoad -> row
            scrollingToBottom -> 1
            else -> 1 // scrolling to top
        }
        val scrollDirectionMultiplier = if (scrollingToBottom || isFirstLoad) 1 else -1
        val columnDelay = column * 150 * scrollDirectionMultiplier
        val easing = if (scrollingToBottom || isFirstLoad)
            LinearOutSlowInEasing else FastOutSlowInEasing
        return rowDelay + columnDelay to easing
    }

    private enum class State { PLACING, PLACED }

    data class ScaleAndAlphaArgs(
        val fromScale: Float,
        val toScale: Float,
        val fromAlpha: Float,
        val toAlpha: Float
    )

    @Composable
    fun scaleAndAlpha(
        args: ScaleAndAlphaArgs,
        animation: FiniteAnimationSpec<Float>
    ): Pair<Float, Float> {
        val transitionState =
            remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
        val transition = updateTransition(transitionState, label = "transition")
        val alpha by transition.animateFloat(
            transitionSpec = { animation },
            label = "alpha"
        ) { state ->
            when (state) {
                State.PLACING -> args.fromAlpha
                State.PLACED -> args.toAlpha
            }
        }
        val scale by transition.animateFloat(
            transitionSpec = { animation },
            label = "scale"
        ) { state ->
            when (state) {
                State.PLACING -> args.fromScale
                State.PLACED -> args.toScale
            }
        }
        return alpha to scale
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun exportNoteToPDF(note: Note) {
        val document = PdfDocument(
            PdfWriter(
                File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ),
                    "${note.title}.pdf"
                )
            )
        )

        val text = Text("${note.title} - ${note.lastModified.formatDateAndTime()}").apply {
            setFontSize(20f)
            setBold()
        }

        val paragraph = Paragraph().apply {
            add(text)
            setTextAlignment(TextAlignment.CENTER)
            setMarginBottom(20f)
        }

        val doc = Document(document).apply {
            add(paragraph)
        }

        val msgText =
            Text(note.content).apply { setFontSize(12f) }

        val msgParagraph = Paragraph().apply {
            add(msgText)
            setMarginTop(10f)
            setMarginBottom(10f)
            setMarginLeft(20f)
            setMarginRight(20f)
        }

        doc.add(msgParagraph)
        doc.close()
    }
}