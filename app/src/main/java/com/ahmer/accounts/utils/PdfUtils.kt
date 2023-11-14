package com.ahmer.accounts.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.io.OutputStream

object PdfUtils {
    fun exportToPdf(context: Context, transList: List<TransEntity>?): Intent? {
        val mIntent: Intent?
        if (!transList.isNullOrEmpty()) {
            val mFileName: String = HelperUtils.getDateTime(
                time = System.currentTimeMillis(),
                pattern = Constants.DATE_TIME_FILE_NAME_PATTERN
            ) + ".pdf"
            mIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                val mMimeType = "application/pdf"
                addCategory(Intent.CATEGORY_OPENABLE)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mMimeType))
                putExtra(Intent.EXTRA_TITLE, mFileName)
                type = mMimeType
            }
        } else {
            HelperUtils.showToast(
                context = context, msg = context.getString(R.string.toast_pdf_not_generated)
            )
            mIntent = null
        }

        return mIntent
    }

    fun generatePdf(
        context: Context,
        uri: Uri,
        transEntity: List<TransEntity>,
        transSumModel: TransSumModel,
        personName: String
    ): Boolean {
        //One inch size equal to 70F; margin using 0.75% of a inch
        val mDocument = Document(PageSize.A4, 52.5F, 52.5F, 52.5F, 52.5F)
        try {
            val mOutPutStream: OutputStream = context.contentResolver.openOutputStream(uri)
                ?: throw NullPointerException("OutputStream for given input Uri is null")

            PdfWriter.getInstance(mDocument, mOutPutStream).apply {
                pageEvent = HeaderFooterPageEvent(context = context)
            }

            val mAppName = context.getString(R.string.app_name)
            val mKeywords = "$personName, Statement, Balance, Sheet"
            val mTitle = "$personName Account Statement"

            val mFont: Font = Font(Font.FontFamily.HELVETICA).apply {
                color = BaseColor.BLACK
                size = 12F
                style = Font.NORMAL
            }

            val mParagraph: Paragraph = Paragraph(mTitle, mFont).apply {
                spacingAfter = 20F
                alignment = Element.ALIGN_CENTER
            }

            mDocument.apply {
                open()
                addAuthor(mAppName)
                addCreationDate()
                addCreator(mAppName)
                addKeywords(mKeywords)
                addSubject(mAppName)
                addTitle(mTitle)
                add(mParagraph)
            }

            val mTableMain: PdfPTable = PdfPTable(5).apply {
                widthPercentage = 100F
                setTotalWidth(floatArrayOf(72F, 148F, 90F, 90F, 90F))
                isLockedWidth = true
                addCell(
                    cellFormat(
                        text = "Date".uppercase(),
                        isHeading = true,
                        alignment = AlignmentCell.EMPTY
                    )
                )
                addCell(
                    cellFormat(
                        text = "Description".uppercase(),
                        isHeading = true,
                        alignment = AlignmentCell.EMPTY
                    )
                )
                addCell(
                    cellFormat(
                        text = "Debit".uppercase(),
                        isHeading = true,
                        alignment = AlignmentCell.EMPTY
                    )
                )
                addCell(
                    cellFormat(
                        text = "Credit".uppercase(),
                        isHeading = true,
                        alignment = AlignmentCell.EMPTY
                    )
                )
                addCell(
                    cellFormat(
                        text = "Balance".uppercase(),
                        isHeading = true,
                        alignment = AlignmentCell.EMPTY
                    )
                )
            }

            val mSortedList: List<TransEntity> = transEntity.sortedWith { o1, o2 -> o1.id - o2.id }
            var mBalanceEntity = 0.0
            var mCreditTransactions = 0
            var mDebitTransactions = 0
            mSortedList.forEach { entity ->
                mTableMain.addCell(
                    cellFormat(text = entity.shortDate, alignment = AlignmentCell.CENTER)
                )
                mTableMain.addCell(
                    cellFormat(text = entity.description, alignment = AlignmentCell.EMPTY)
                )
                var mCreditEntity = 0.0
                var mDebitEntity = 0.0
                if (entity.type == "Credit") {
                    mCreditTransactions += 1
                    mCreditEntity = entity.amount.toDouble()
                } else {
                    mDebitTransactions += 1
                    mDebitEntity = entity.amount.toDouble()
                }
                mBalanceEntity += mCreditEntity - mDebitEntity
                val mDebit: String = HelperUtils.getRoundedValue(value = mDebitEntity)
                val mCredit: String = HelperUtils.getRoundedValue(value = mCreditEntity)
                val mBalance: String = HelperUtils.getRoundedValue(value = mBalanceEntity)
                if (mDebit == "0") {
                    mTableMain.addCell("")
                } else {
                    mTableMain.addCell(cellFormat(text = mDebit, alignment = AlignmentCell.RIGHT))
                }
                if (mCredit == "0") {
                    mTableMain.addCell("")
                } else {
                    mTableMain.addCell(cellFormat(text = mCredit, alignment = AlignmentCell.RIGHT))
                }
                mTableMain.addCell(cellFormat(text = mBalance, alignment = AlignmentCell.RIGHT))
            }
            val mTableTotal: PdfPTable = PdfPTable(4).apply {
                widthPercentage = 100F
                setTotalWidth(floatArrayOf(220F, 90F, 90F, 90F))
                isLockedWidth = true
                addCell(
                    cellFormat(
                        text = "Total".uppercase(),
                        alignment = AlignmentCell.CENTER,
                        isTotal = true
                    )
                )
                addCell(
                    cellFormat(
                        text = HelperUtils.getRoundedValue(value = transSumModel.debitSum),
                        alignment = AlignmentCell.RIGHT,
                        isTotal = true
                    )
                )
                addCell(
                    cellFormat(
                        text = HelperUtils.getRoundedValue(value = transSumModel.creditSum),
                        alignment = AlignmentCell.RIGHT,
                        isTotal = true
                    )
                )
                addCell(
                    cellFormat(
                        text = HelperUtils.getRoundedValue(value = transSumModel.balance),
                        alignment = AlignmentCell.RIGHT,
                        isTotal = true
                    )
                )
            }

            val mSummaryCredit = "Total Credit Transactions: $mCreditTransactions"
            val mSummaryDebit = "Total Debit Transactions: $mDebitTransactions"
            val mFontSummary: Font = Font(Font.FontFamily.HELVETICA).apply {
                color = BaseColor.BLACK
                size = 8F
                style = Font.NORMAL
            }

            val mParagraphCredit: Paragraph = Paragraph(mSummaryCredit, mFontSummary).apply {
                spacingBefore = 10F
                alignment = Element.ALIGN_LEFT
            }

            val mParagraphDebit: Paragraph = Paragraph(mSummaryDebit, mFontSummary).apply {
                alignment = Element.ALIGN_LEFT
            }

            mDocument.add(mTableMain)
            mDocument.add(mTableTotal)
            mDocument.add(mParagraphCredit)
            mDocument.add(mParagraphDebit)
            return true
        } catch (de: DocumentException) {
            Log.e(Constants.LOG_TAG, de.localizedMessage, de)
            FirebaseCrashlytics.getInstance().recordException(de)
            return false
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.localizedMessage, e)
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        } finally {
            mDocument.close()
        }
    }

    private fun cellFormat(
        text: String,
        isHeading: Boolean = false,
        alignment: AlignmentCell,
        isTotal: Boolean = false
    ): PdfPCell {
        val mFont: Font = Font(Font.FontFamily.HELVETICA).apply {
            color = BaseColor.BLACK
        }
        if (isHeading) {
            mFont.apply {
                size = 10F
                style = Font.BOLD
            }
        } else {
            if (isTotal) {
                mFont.apply {
                    size = 10F
                    style = Font.BOLD
                }
            } else {
                mFont.apply {
                    size = 8F
                    style = Font.NORMAL
                }
            }
        }
        val mPdfPCell = PdfPCell(Phrase(text, mFont))
        if (isHeading) {
            mPdfPCell.apply {
                verticalAlignment = Element.ALIGN_MIDDLE
                horizontalAlignment = Element.ALIGN_CENTER
                paddingTop = 5F
                paddingBottom = 8F
            }
        } else {
            when (alignment) {
                AlignmentCell.CENTER -> {
                    mPdfPCell.apply {
                        verticalAlignment = Element.ALIGN_MIDDLE
                        horizontalAlignment = Element.ALIGN_CENTER
                    }
                }

                AlignmentCell.EMPTY -> {
                    mPdfPCell.apply {
                        verticalAlignment = Element.ALIGN_MIDDLE
                    }
                }

                AlignmentCell.RIGHT -> {
                    mPdfPCell.apply {
                        verticalAlignment = Element.ALIGN_MIDDLE
                        horizontalAlignment = Element.ALIGN_RIGHT
                    }
                }
            }
            if (isTotal) {
                mPdfPCell.apply {
                    paddingTop = 5F
                    paddingBottom = 7F
                }
            } else {
                mPdfPCell.apply {
                    paddingTop = 3F
                    paddingBottom = 5F
                }
            }
        }
        return mPdfPCell
    }

    class HeaderFooterPageEvent(private val context: Context) : PdfPageEventHelper() {
        override fun onStartPage(writer: PdfWriter, document: Document?) {
            val mFont: Font = Font(Font.FontFamily.HELVETICA).apply {
                color = BaseColor.BLACK
                size = 8F
                style = Font.NORMAL
            }
            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                Phrase(context.getString(R.string.app_name), mFont),
                80F,
                800F,
                0F
            )
            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                Phrase(
                    HelperUtils.getDateTime(
                        time = System.currentTimeMillis(), pattern = Constants.DATE_TIME_PDF_PATTERN
                    ), mFont
                ),
                477F,
                800F,
                0F
            )
        }

        override fun onEndPage(writer: PdfWriter, document: Document) {
            val mFont: Font = Font(Font.FontFamily.HELVETICA).apply {
                color = BaseColor.BLACK
                size = 8F
                style = Font.NORMAL
            }
            val mPlayStoreLink: String = HelperUtils.getPlayStoreLink(context = context)
            val mChunk = Chunk(mPlayStoreLink).apply {
                setAnchor(mPlayStoreLink)
            }
            val mPhrase: Phrase = Phrase("", mFont).apply {
                add(mChunk)
            }
            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                Phrase(mPhrase),
                170F,
                35F,
                0F
            )
            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                Phrase("Page " + document.pageNumber, mFont),
                530F,
                35F,
                0F
            )
        }
    }

    sealed class AlignmentCell {
        data object CENTER : AlignmentCell()
        data object EMPTY : AlignmentCell()
        data object RIGHT : AlignmentCell()
    }
}