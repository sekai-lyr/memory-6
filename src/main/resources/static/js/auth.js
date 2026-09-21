document.querySelectorAll("form").forEach((form) => {
    form.addEventListener("submit", () => {
        const button = form.querySelector("button[type='submit']");
        if (button) {
            button.disabled = true;
            button.setAttribute("aria-busy", "true");
            const loadingLabel = button.dataset.loadingLabel || "正在进入...";
            const label = button.querySelector("[data-submit-label]");
            if (label) {
                label.textContent = loadingLabel;
            } else {
                button.textContent = loadingLabel;
            }
        }
    });
});

const passwordInput = document.querySelector("#password");
const passwordToggle = document.querySelector("#passwordToggle");

if (passwordInput && passwordToggle) {
    passwordToggle.addEventListener("click", () => {
        const isVisible = passwordInput.type === "text";
        passwordInput.type = isVisible ? "password" : "text";
        passwordToggle.textContent = isVisible ? "显示" : "隐藏";
        passwordToggle.setAttribute("aria-pressed", String(!isVisible));
    });
}
