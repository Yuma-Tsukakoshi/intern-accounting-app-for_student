document.addEventListener("DOMContentLoaded", () => {
    const maxLen = 7;  // 表示したい最大文字数
    document.querySelectorAll(".txt-limit").forEach(el => {
        const text = el.textContent.trim();
        if (text.length > maxLen) {
            el.textContent = text.slice(0, maxLen) + "…";
        }
    });
});