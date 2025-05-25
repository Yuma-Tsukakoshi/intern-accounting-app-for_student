import com.example.accounting.domain.account.AccountType
import java.time.LocalDate

data class PlDto(
    val profit: PlCategoryDto,
    val loss: PlCategoryDto,
    val benefit: Long
)

data class PlCategoryDto(
    val category: AccountType,
    val subjects: List<PlSubjectDto>,
    val totalAmount: Long
)

data class PlSubjectDto(
    val accountName: String,
    val totalAmount: Long,
    val entries: List<PlEntryDto>
)

data class PlEntryDto(
    val date: LocalDate,
    val debitCreditType: String,
    val amount: Long,
    val summary: String?
)