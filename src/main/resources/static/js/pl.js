document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".month-form")
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            const year = document.getElementById("year").value;
            let month = document.getElementById("month").value;
            if (month.length === 1) month = "0" + month;

            const yearMonth = `${year}-${month}`;
            window.location.href = `/pl?month=${yearMonth}`;
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-detail-body");
    const closeButton = modal.querySelector(".close-button");

    const subjectDetails = window.PL_ENTRIES_BY_SUBJECT || {}; // コントローラから渡すデータ
    console.log("subjectDEtail",subjectDetails )

    document.querySelectorAll(".detail-trigger").forEach(el => {
        el.addEventListener("click", () => {
            const subject = el.dataset.subject;
            const entries = subjectDetails[subject] || [];
            modalBody.innerHTML = entries.map(entry => `
        <tr>
          <td>${entry.date}</td>
          <td>${entry.type}</td>
          <td>${entry.amount.toLocaleString()}円</td>
          <td>${entry.summary}</td>
        </tr>
      `).join("");
            modal.classList.add("show");
        });
    });

    closeButton.addEventListener("click", () => modal.classList.remove("show"));
    modal.addEventListener("click", (e) => {
        if (e.target === modal) modal.classList.remove("show");
    });
});
