
const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");
const myCharId = localStorage.getItem("selectedCharId");

// Proteção de Rota
if (!token || !myCharId) {
    window.location.href = "dashboard.html";
}

document.addEventListener("DOMContentLoaded", () => {
    carregarMeuPerfil();
    carregarFeed(); // Agora carrega só quem eu sigo
});

// 1. Carrega os dados do meu personagem
async function carregarMeuPerfil() {
    const response = await fetch(`${API_URL}/characters/my`, {
        headers: { "Authorization": `Bearer ${token}` }
    });
    
    if(response.ok) {
        const chars = await response.json();
        const me = chars.find(c => c.id == myCharId);
        
        if(me) {
            const avatar = me.photoUrl || `https://api.dicebear.com/7.x/bottts/svg?seed=${me.name}`;
            document.getElementById("myProfileCard").innerHTML = `
                <img src="${avatar}" class="profile-img mx-auto mb-2" alt="Avatar" style="width: 80px; height: 80px; border-radius: 50%;">
                <h5 class="fw-bold mb-0">${me.name}</h5>
                <p class="text-muted small mb-1">Lvl ${me.level} • ${me.characterClass}</p>
                <span class="badge bg-secondary mb-2">${me.game.name}</span>
            `;
        }
    }
}

// 2. Carrega o FEED PERSONALIZADO (Só de quem eu sigo)
async function carregarFeed() {
    // Nova Rota: Busca apenas posts do feed deste personagem
    const response = await fetch(`${API_URL}/posts/feed/${myCharId}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const feedList = document.getElementById("feedList");
    feedList.innerHTML = "";

    if (response.ok) {
        const posts = await response.json();
        
        if (posts.length === 0) {
            feedList.innerHTML = `<p class="text-center text-muted mt-5">Seu feed está vazio. Pesquise e siga outros personagens para ver as postagens deles aqui!</p>`;
            return;
        }

        posts.forEach(post => {
            if(!post.author) return; 
            
            const avatar = post.author.photoUrl || `https://api.dicebear.com/7.x/bottts/svg?seed=${post.author.name}`;
            
            feedList.innerHTML += `
                <div class="card post-card p-3 mb-3 shadow-sm border-0">
                    <div class="d-flex align-items-center mb-2">
                        <img src="${avatar}" class="post-avatar me-2" alt="Avatar" style="width: 40px; height: 40px; border-radius: 50%;">
                        <div>
                            <h6 class="mb-0 fw-bold text-primary" style="cursor:pointer;" onclick="verPerfil(${post.author.id})">${post.author.name}</h6>
                            <small class="text-muted">${new Date(post.createdAt).toLocaleDateString('pt-BR')} às ${new Date(post.createdAt).toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'})}</small>
                        </div>
                    </div>
                    <p class="mb-2">${post.content}</p>
                    
                    <div class="text-muted small mt-2 pt-2 border-top">
                        <span style="cursor:pointer; color: #198754;" class="me-3 fw-bold" onclick="gostarPost(${post.id})">👍 Curtir / Descurtir</span> 
                        <span style="cursor:pointer; color: #0dcaf0;" class="fw-bold" onclick="toggleComentarios(${post.id})">💬 Comentários</span>
                    </div>

                    <div id="caixa-comentarios-${post.id}" class="mt-3 p-2 bg-light rounded" style="display: none;">
                        <div id="lista-comentarios-${post.id}" class="mb-2 small"></div>
                        <div class="d-flex">
                            <input type="text" id="input-comentario-${post.id}" class="form-control form-control-sm me-2" placeholder="Escreva um comentário...">
                            <button class="btn btn-primary btn-sm" onclick="enviarComentario(${post.id})">Enviar</button>
                        </div>
                    </div>
                </div>
            `;
        });
    }
}

// 3. Criar uma nova postagem
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
        document.getElementById("postContent").value = ""; 
        alert("Publicado com sucesso! (Lembre-se: Você só verá seus próprios posts se entrar no seu próprio perfil, o feed é para ver os outros).");
    } else {
        alert("Erro ao publicar.");
    }
}

// 4. Lógica de Curtir (Toggle Like)
async function gostarPost(postId) {
    const response = await fetch(`${API_URL}/posts/${postId}/like`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ characterId: myCharId })
    });

    if (response.ok) {
        const msg = await response.text();
        alert(msg); // Exibe "Like adicionado" ou "Like removido"
    }
}

// 5. Exibir/Ocultar caixa de comentários e carregar os comentários do banco
async function toggleComentarios(postId) {
    const caixa = document.getElementById(`caixa-comentarios-${postId}`);
    if (caixa.style.display === "none") {
        caixa.style.display = "block";
        carregarComentarios(postId);
    } else {
        caixa.style.display = "none";
    }
}

// 6. Buscar comentários daquele post no Backend
async function carregarComentarios(postId) {
    const response = await fetch(`${API_URL}/posts/${postId}/comments`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const lista = document.getElementById(`lista-comentarios-${postId}`);
    lista.innerHTML = "";

    if (response.ok) {
        const comentarios = await response.json();
        if (comentarios.length === 0) {
            lista.innerHTML = `<span class="text-muted">Nenhum comentário ainda.</span>`;
            return;
        }

        comentarios.forEach(c => {
            lista.innerHTML += `
                <div class="mb-1 border-bottom pb-1">
                    <strong class="text-dark">${c.author.name}:</strong> 
                    <span class="text-secondary">${c.content}</span>
                </div>
            `;
        });
    }
}

// 7. Enviar um comentário novo
async function enviarComentario(postId) {
    const input = document.getElementById(`input-comentario-${postId}`);
    const content = input.value.trim();
    if(!content) return;

    const response = await fetch(`${API_URL}/posts/${postId}/comments`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ characterId: myCharId, content: content })
    });

    if (response.ok) {
        input.value = "";
        carregarComentarios(postId); // Atualiza a listinha na hora
    }
}

function verPerfil(targetCharacterId) {
    // Salva o ID do alvo e vai para a página de perfil dele (Próximo passo!)
    localStorage.setItem("targetProfileId", targetCharacterId);
    window.location.href = "profile.html";
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}