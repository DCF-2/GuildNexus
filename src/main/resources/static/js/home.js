const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// Verifica se está logado
if (!token) {
    window.location.href = "index.html";
}

document.getElementById("userDisplay").innerText = localStorage.getItem("userEmail");

// Ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
    carregarPersonagens();
});

async function carregarPersonagens() {
    const response = await fetch(`${API_URL}/characters/my`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const chars = await response.json();
    const container = document.getElementById("charList");
    container.innerHTML = "";

    if (chars.length === 0) {
        container.innerHTML = `<p class="text-center text-muted">Você ainda não tem personagens.</p>`;
        return;
    }

    chars.forEach(c => {
        const card = `
            <div class="col-md-4 col-sm-6">
                <div class="card char-card text-white p-3 h-100" onclick="entrarComo(${c.id})">
                    <div class="card-body text-center">
                        <div class="display-4 mb-2">👾</div>
                        <h4 class="card-title">${c.name}</h4>
                        <p class="card-text text-success mb-1">Lvl ${c.level} • ${c.characterClass}</p>
                        <small class="text-muted">${c.game.name}</small>
                    </div>
                </div>
            </div>
        `;
        container.innerHTML += card;
    });
}

async function criarPersonagem() {
    const name = document.getElementById("charName").value;
    const level = document.getElementById("charLevel").value;
    const characterClass = document.getElementById("charClass").value;
    const gameId = document.getElementById("charGame").value;

    const response = await fetch(`${API_URL}/characters`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name, level, characterClass, gameId })
    });

    if (response.ok) {
        // Fecha o modal e recarrega
        bootstrap.Modal.getInstance(document.getElementById('newCharModal')).hide();
        carregarPersonagens();
    } else {
        alert("Erro ao criar personagem");
    }
}

function entrarComo(charId) {
    // Salva qual personagem foi escolhido e vai pra tela de Feed
    localStorage.setItem("selectedCharId", charId);
    window.location.href = "feed.html"; // Próxima tela que faremos
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}