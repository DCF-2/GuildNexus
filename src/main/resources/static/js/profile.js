
const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");
const myCharId = localStorage.getItem("selectedCharId");
const targetProfileId = localStorage.getItem("targetProfileId");

if (!token || !myCharId || !targetProfileId) {
    window.location.href = "feed.html";
}

document.addEventListener("DOMContentLoaded", () => {
    carregarPerfilAlvo();
    carregarPostsDoPerfil();
});

async function carregarPerfilAlvo() {
    const response = await fetch(`${API_URL}/characters/${targetProfileId}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const header = document.getElementById("profileHeader");

    if (response.ok) {
        const char = await response.json();
        const avatar = char.photoUrl || `https://api.dicebear.com/7.x/bottts/svg?seed=${char.name}`;
        
        header.innerHTML = `
            <img src="${avatar}" class="profile-img-large mb-3" alt="Avatar">
            <h2 class="fw-bold mb-1">${char.name}</h2>
            <p class="mb-2 fs-5 text-light">Lvl ${char.level} • ${char.characterClass}</p>
            <span class="badge bg-light text-dark mb-3 fs-6 px-3 py-2">${char.game.name}</span>
            <br>
            <button class="btn btn-success fw-bold px-4 rounded-pill shadow" onclick="seguirToggle()">
                <span id="followBtnText">Seguir / Deixar de Seguir</span>
            </button>
        `;
    } else {
        header.innerHTML = `<h3 class="text-danger">Personagem não encontrado.</h3>`;
    }
}

async function carregarPostsDoPerfil() {
    const response = await fetch(`${API_URL}/posts/character/${targetProfileId}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const postsList = document.getElementById("profilePostsList");
    postsList.innerHTML = "";

    if (response.ok) {
        const posts = await response.json();
        
        if (posts.length === 0) {
            postsList.innerHTML = `<div class="card p-4 text-center border-0 shadow-sm rounded-3"><p class="text-muted mb-0">Este personagem ainda não partilhou nenhuma aventura.</p></div>`;
            return;
        }

        posts.forEach(post => {
            postsList.innerHTML += `
                <div class="card post-card p-4 bg-white mb-3">
                    <small class="text-muted mb-2 d-block border-bottom pb-2">Publicado em: ${new Date(post.createdAt).toLocaleDateString('pt-PT')} às ${new Date(post.createdAt).toLocaleTimeString('pt-PT', {hour: '2-digit', minute:'2-digit'})}</small>
                    <p class="mb-0 fs-5 text-dark">${post.content}</p>
                </div>
            `;
        });
    }
}

async function seguirToggle() {
    // Chama o endpoint de seguir que criámos na Fase 5
    const response = await fetch(`${API_URL}/characters/follow`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ 
            myCharacterId: myCharId, 
            targetCharacterId: targetProfileId 
        })
    });

    if (response.ok) {
        const msg = await response.text();
        alert(msg); // Exibe se "Agora seguindo" ou "Deixou de seguir"
    } else {
        alert("Erro ao tentar seguir o personagem.");
    }
}