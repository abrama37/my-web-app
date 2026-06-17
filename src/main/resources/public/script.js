const API = '/api/meals';

async function loadMeals() {
    const res  = await fetch(API);
    const meals = await res.json();
    const list  = document.getElementById('meals-list');

    if (meals.length === 0) {
        list.innerHTML = '<p style="color:#aaa">No meals yet. Add some!</p>';
        return;
    }

    list.innerHTML = meals.map(m => `
        <div class="meal-item" id="meal-${m.id}">
            <div class="meal-info">
                <strong>${m.name}</strong>
                <span>${m.category || ''}</span>
                <span>⭐ ${m.rating}</span>
                <br><small>${m.description || ''}</small>
            </div>
            <div class="meal-actions">
                <input type="number" min="0" max="5" step="0.1"
                       value="${m.rating}" id="rating-${m.id}" />
                <button class="small" onclick="updateRating(${m.id})">Rate</button>
                <button class="danger" onclick="deleteMeal(${m.id})">🗑️</button>
            </div>
        </div>
    `).join('');
}

document.getElementById('random-btn').addEventListener('click', async () => {
    const res = await fetch(`${API}/random`);
    if (res.status === 404) {
        alert('No meals in database yet!');
        return;
    }
    const meal   = await res.json();
    const result = document.getElementById('random-result');
    result.classList.remove('hidden');
    document.getElementById('meal-name').textContent        = meal.name;
    document.getElementById('meal-category').textContent    = '📁 ' + (meal.category || 'No category');
    document.getElementById('meal-description').textContent = '📝 ' + (meal.description || 'No description');
    document.getElementById('meal-rating').textContent      = '⭐ ' + meal.rating;
});

document.getElementById('add-btn').addEventListener('click', async () => {
    const name        = document.getElementById('input-name').value.trim();
    const category    = document.getElementById('input-category').value;
    const description = document.getElementById('input-description').value.trim();
    const msg         = document.getElementById('add-msg');

    if (!name) { msg.style.color = 'red'; msg.textContent = 'Name is required!'; return; }

    const form = new FormData();
    form.append('name', name);
    form.append('category', category);
    form.append('description', description);

    const res = await fetch(API, { method: 'POST', body: form });
    if (res.ok) {
        msg.style.color = 'green';
        msg.textContent = '✅ Meal added!';
        document.getElementById('input-name').value        = '';
        document.getElementById('input-category').value    = '';
        document.getElementById('input-description').value = '';
        loadMeals();
    }
});

async function deleteMeal(id) {
    if (!confirm('Delete this meal?')) return;
    await fetch(`${API}/${id}`, { method: 'DELETE' });
    loadMeals();
}

async function updateRating(id) {
    const rating = document.getElementById(`rating-${id}`).value;
    const form   = new FormData();
    form.append('rating', rating);
    await fetch(`${API}/${id}/rating`, { method: 'PATCH', body: form });
    loadMeals();
}

loadMeals();
