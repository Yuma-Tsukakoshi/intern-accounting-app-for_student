document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".date-form");
    if (!form) return;

    form.addEventListener("submit", e => {
        e.preventDefault();

        const fromDate = document.getElementById("fromDate").value;
        const toDate = document.getElementById("toDate").value;

        window.location.href = `/journals?fromDate=${fromDate}&toDate=${toDate}`;
    });
});


document.addEventListener("DOMContentLoaded", () => {
    const maxLen = 7;  // 表示したい最大文字数

    document.querySelectorAll(".txt-limit").forEach(el => {
        const full = el.dataset.fulltext || "";
        const text = el.textContent.trim();

        // 省略表示
        if (full.length > maxLen) {
            el.textContent = full.slice(0, maxLen) + "…";
        }

        // tooltip 用イベント
        if (full.length > maxLen) {
            el.addEventListener("mouseenter", () => {
                const tip = document.createElement("div");
                tip.className = "tooltip";
                tip.textContent = full;
                document.body.appendChild(tip);
                const rect = el.getBoundingClientRect();
                tip.style.position = "absolute";
                tip.style.top  = `${rect.bottom + window.scrollY + 4}px`;
                tip.style.left = `${rect.left + window.scrollX}px`;
                el._tip = tip;
            });
            el.addEventListener("mouseleave", () => {
                if (el._tip) {
                    document.body.removeChild(el._tip);
                    el._tip = null;
                }
            });
        }
    });
});