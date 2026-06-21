const API = '/api/diary';
const YEARS = ['2024', '2025', '2026'];

const monthNames = {
    '01': 'იანვარი', '02': 'თებერვალი', '03': 'მარტი', '04': 'აპრილი',
    '05': 'მაისი', '06': 'ივნისი', '07': 'ივლისი', '08': 'აგვისტო',
    '09': 'სექტემბერი', '10': 'ოქტომბერი', '11': 'ნოემბერი', '12': 'დეკემბერი'
};

let state = { year: null, month: null, day: null };

function renderBreadcrumb() {
    const parts = [`<span class="crumb ${!state.year ? 'active' : ''}" onclick="goToYears()">წელი</span>`];

    if (state.year) {
        parts.push('<span class="sep">/</span>');
        parts.push(`<span class="crumb ${!state.month ? 'active' : ''}" onclick="goToMonths()">${state.year}</span>`);
    }
    if (state.month) {
        parts.push('<span class="sep">/</span>');
        parts.push(`<span class="crumb ${!state.day ? 'active' : ''}" onclick="goToDays()">${monthNames[state.month] || state.month}</span>`);
    }
    if (state.day) {
        parts.push('<span class="sep">/</span>');
        parts.push(`<span class="crumb active">${state.day}</span>`);
    }
    document.getElementById('breadcrumb').innerHTML = parts.join('');
}

function showOnly(id) {
    ['years-section', 'months-section', 'days-section', 'entry-section']
        .forEach(s => document.getElementById(s).classList.toggle('hidden', s !== id));
}

function renderYears() {
    document.getElementById('years-list').innerHTML = YEARS.map(y =>
        `<div class="option-chip" onclick="selectYear('${y}')">${y}</div>`
    ).join('');
}

function goToYears() {
    state = { year: null, month: null, day: null };
    renderBreadcrumb();
    showOnly('years-section');
}

function selectYear(year) {
    state.year = year;
    goToMonths();
}

async function goToMonths() {
    state.month = null;
    state.day = null;
    renderBreadcrumb();

    const res = await fetch(`${API}/${state.year}/months`);
    const months = await res.json();
    document.getElementById('months-list').innerHTML = months.length
        ? months.map(m => `<div class="option-chip" onclick="selectMonth('${m}')">${monthNames[m] || m}</div>`).join('')
        : '<p class="empty-note">ჩანაწერები არ მოიძებნა</p>';

    showOnly('months-section');
}

function selectMonth(month) {
    state.month = month;
    goToDays();
}

async function goToDays() {
    state.day = null;
    renderBreadcrumb();

    const res = await fetch(`${API}/${state.year}/${state.month}/days`);
    const days = await res.json();
    document.getElementById('days-list').innerHTML = days.length
        ? days.map(d => `<div class="option-chip" onclick="selectDay('${d}')">${d}</div>`).join('')
        : '<p class="empty-note">ჩანაწერები არ მოიძებნა</p>';

    showOnly('days-section');
}

async function selectDay(day) {
    state.day = day;
    renderBreadcrumb();

    const res = await fetch(`${API}/${state.year}/${state.month}/${day}`);
    if (res.status === 404) { alert('ჩანაწერი ვერ მოიძებნა'); return; }

    const text = await res.text();
    document.getElementById('entry-date').textContent = `${day}.${state.month}.${state.year}`;
    document.getElementById('entry-content').textContent = text;
    showOnly('entry-section');
}

renderYears();
goToYears();