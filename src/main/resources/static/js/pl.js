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
