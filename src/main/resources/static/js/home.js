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
        container.innerHTML = `<p class="text-center text-muted">Você ainda não tem personagens. Crie o primeiro!</p>`;
        return;
    }

    chars.forEach(c => {
        // Se tiver foto usa ela, senão usa um placeholder
        const imageHtml = c.photoUrl 
            ? `<img src="${c.photoUrl}" class="char-img w-100" alt="Foto">`
            : `<div class="placeholder-img w-100">👾</div>`;

        const card = `
            <div class="col-md-4 col-sm-6">
                <div class="card char-card shadow-sm h-100" onclick="entrarComo(${c.id})">
                    ${imageHtml}
                    <div class="card-body text-center bg-white">
                        <h4 class="card-title fw-bold text-dark">${c.name}</h4>
                        <p class="card-text text-primary fw-semibold mb-1">Lvl ${c.level} • ${c.characterClass}</p>
                        <span class="badge bg-secondary">${c.game.name}</span>
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
    const gameName = document.getElementById("charGameName").value; // Agora é nome, não ID
    const fileInput = document.getElementById("charPhotoFile");

    // Imagem PNG padrão caso ele não faça upload de nada
    const defaultImage = "https://api.dicebear.com/7.x/bottts/svg?seed=" + name; 
    let photoUrl = defaultImage;

    // Se o usuário selecionou um arquivo do computador
    if (fileInput.files.length > 0) {
        const file = fileInput.files[0];
        
        // Converte a imagem para Base64 (texto)
        photoUrl = await new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = error => reject(error);
        });
    }

    const response = await fetch(`${API_URL}/characters`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        // Manda os dados, incluindo a imagem convertida
        body: JSON.stringify({ name, level, characterClass, gameName, photoUrl })
    });

    if (response.ok) {
        bootstrap.Modal.getInstance(document.getElementById('newCharModal')).hide();
        carregarPersonagens();
    } else {
        alert("Erro ao criar personagem. Preencha todos os campos.");
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