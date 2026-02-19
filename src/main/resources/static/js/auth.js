const API_URL = "http://localhost:8080";

// --- LOGIN ---
async function fazerLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Limpa token antigo
    localStorage.clear();

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("token", data.token);
            localStorage.setItem("userEmail", email);
            
            // Redireciona para o dashboard
            window.location.href = "dashboard.html"; 
        } else {
            alert("Erro no login! Verifique suas credenciais.");
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao conectar com o servidor.");
    }
}

// --- CADASTRO ---
async function fazerCadastro() {
    const name = document.getElementById("regName").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;

    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        if (response.ok) {
            alert("Conta criada com sucesso! Faça login.");
            window.location.href = "index.html";
        } else {
            // Tenta ler o JSON de erro do nosso GlobalExceptionHandler
            try {
                const erro = await response.json();
                // Formata o erro bonito (ex: {"email": "invalido"} -> "email: invalido")
                let msg = "Erro no cadastro:\n";
                for (const [key, value] of Object.entries(erro)) {
                    msg += `- ${value}\n`;
                }
                alert(msg);
            } catch (e) {
                alert("Erro desconhecido no cadastro.");
            }
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao conectar com o servidor.");
    }
}