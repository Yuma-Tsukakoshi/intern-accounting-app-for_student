document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".month-form");
    if (!form) return;

    form.addEventListener("submit", e => {
        e.preventDefault();

        const cfY = document.getElementById("currentMonthFromYear").value;
        let cfM   = document.getElementById("currentMonthFromMonth").value;
        const ctY = document.getElementById("currentMonthToYear").value;
        let ctM   = document.getElementById("currentMonthToMonth").value;
        const lfY = document.getElementById("lastMonthFromYear").value;
        let lfM   = document.getElementById("lastMonthFromMonth").value;
        const ltY = document.getElementById("lastMonthToYear").value;
        let ltM   = document.getElementById("lastMonthToMonth").value;

        // 一桁月を必ず2桁にパディング
        if (cfM.length === 1) cfM = "0" + cfM;
        if (ctM.length === 1) ctM = "0" + ctM;
        if (lfM.length === 1) lfM = "0" + lfM;
        if (ltM.length === 1) ltM = "0" + ltM;

        const params = [
            `currentMonthFrom=${cfY}-${cfM}`,
            `currentMonthTo=${ctY}-${ctM}`
        ];
        if (lfY && lfM && ltY && ltM) {
            params.push(`lastMonthFrom=${lfY}-${lfM}`);
            params.push(`lastMonthTo=${ltY}-${ltM}`);
        }

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
