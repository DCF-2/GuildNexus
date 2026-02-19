const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");
const myCharId = localStorage.getItem("selectedCharId");

// Proteção de Rota
if (!token || !myCharId) {
    window.location.href = "dashboard.html";
}

document.addEventListener("DOMContentLoaded", () => {
    carregarMeuPerfil();
    carregarFeed();
    carregarDescobrir();
});

// 1. Carrega os dados do meu personagem para a coluna da esquerda
async function carregarMeuPerfil() {
    // Como a nossa API ainda não tem um endpoint para um único personagem (GET /characters/{id}),
    // vamos buscar todos os meus e filtrar pelo ID selecionado.
    const response = await fetch(`${API_URL}/characters/my`, {
        headers: { "Authorization": `Bearer ${token}` }
    });
    
    if(response.ok) {
        const chars = await response.json();
        const me = chars.find(c => c.id == myCharId);
        
        if(me) {
            const avatar = me.photoUrl || `https://api.dicebear.com/7.x/bottts/svg?seed=${me.name}`;
            document.getElementById("myProfileCard").innerHTML = `
                <img src="${avatar}" class="profile-img mx-auto mb-2" alt="Avatar">
                <h5 class="fw-bold mb-0">${me.name}</h5>
                <p class="text-muted small mb-1">Lvl ${me.level} • ${me.characterClass}</p>
                <span class="badge bg-secondary mb-2">${me.game.name}</span>
            `;
        }
    }
}

// 2. Carrega as publicações globais
async function carregarFeed() {
    const response = await fetch(`${API_URL}/posts`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const feedList = document.getElementById("feedList");
    feedList.innerHTML = "";

    if (response.ok) {
        const posts = await response.json();
        
        if (posts.length === 0) {
            feedList.innerHTML = `<p class="text-center text-muted">Ainda não há publicações. Sê o primeiro!</p>`;
            return;
        }

        posts.forEach(post => {
            // Previne erro se o autor for apagado
            if(!post.author) return; 
            
            const avatar = post.author.photoUrl || `https://api.dicebear.com/7.x/bottts/svg?seed=${post.author.name}`;
            
            feedList.innerHTML += `
                <div class="card post-card p-3">
                    <div class="d-flex align-items-center mb-2">
                        <img src="${avatar}" class="post-avatar me-2" alt="Avatar">
                        <div>
                            <h6 class="mb-0 fw-bold">${post.author.name}</h6>
                            <small class="text-muted">${new Date(post.createdAt).toLocaleString('pt-PT')}</small>
                        </div>
                    </div>
                    <p class="mb-2">${post.content}</p>
                    <div class="text-muted small">
                        <span style="cursor:pointer;" onclick="gostarPost(${post.id})">👍 Gostar</span> • 
                        <span style="cursor:pointer;" onclick="comentarPost(${post.id})">💬 Comentar</span>
                    </div>
                </div>
            `;
        });
    }
}

// 3. Criar uma nova publicação
async function publicarPost() {
    const content = document.getElementById("postContent").value;
    
    if(!content.trim()) return;

    const response = await fetch(`${API_URL}/posts`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ content: content, characterId: myCharId })
    });

    if (response.ok) {
        document.getElementById("postContent").value = ""; // Limpa a caixa
        carregarFeed(); // Atualiza a lista
    } else {
        alert("Erro ao publicar.");
    }
}

// (Opcional - Simples listagem de users para a lateral)
async function carregarDescobrir() {
    // Para simplificar, poderíamos buscar utilizadores aqui, mas por agora 
    // podes testar publicando com 2 personagens diferentes para os ver no feed global.
    document.getElementById("discoverList").innerHTML = `<small class="text-muted">A procurar jogadores no servidor...</small>`;
}

function abrirLive() {
    alert("Funcionalidade de Live em construção! (Próxima etapa)");
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}