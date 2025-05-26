document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".month-form");
    if (!form) return;

    form.addEventListener("submit", e => {
        e.preventDefault();

        const ly = document.getElementById("lastYear").value;
        let lm = document.getElementById("lastMonth").value;
        const cy = document.getElementById("currentYear").value;
        let cm = document.getElementById("currentMonth").value;

        // 1桁月を2桁に
        if (lm && lm.length === 1) lm = "0" + lm;
        if (cm.length === 1) cm = "0" + cm;

        const params = [];
        if (ly && lm) {
            params.push(`lastMonth=${ly}-${lm}`);
        }
        // 当期は必須
        params.push(`currentMonth=${cy}-${cm}`);

        window.location.href = `/pl?${params.join("&")}`;
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const modal       = document.getElementById("modal");
    const modalTitle  = document.getElementById("modal-title");
    const modalBody   = document.getElementById("modal-body");
    const closeBtn    = modal.querySelector(".close-button");
    const dataMap     = window.PL_ENTRIES_BY_SUBJECT || {};
    console.log("dataMap",dataMap)

    document.querySelectorAll(".detail-trigger").forEach(el => {
        el.addEventListener("click", () => {
            const subj   = el.dataset.subject;
            const period = el.dataset.period;
            console.log("subj,period", subj,period)// "last" or "current"
            const list   = (dataMap[subj] || {})[period] || [];

            modalTitle.textContent = period === "last"
                ? "前期明細"
                : "当期明細";

            modalBody.innerHTML = list.map(e => `
        <tr>
          <td>${e.date}</td>
          <td>${e.type}</td>
          <td>${Number(e.amount).toLocaleString()}円</td>
          <td>${e.summary}</td>
        </tr>
      `).join("");

            modal.classList.add("show");
        });
    });

    closeBtn.addEventListener("click", () => modal.classList.remove("show"));
    modal.addEventListener("click", e => {
        if (e.target === modal) modal.classList.remove("show");
    });
});
