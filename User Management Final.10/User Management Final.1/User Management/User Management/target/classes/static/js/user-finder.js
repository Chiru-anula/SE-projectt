(function () {
  const form = document.getElementById('searchForm');
  if (!form) return;
  const input = form.querySelector('#q');
  const result = document.getElementById('userFinderResult');
  if (!result) return;

  function roleBadge(role) {
    const cls = role === 'ADMIN' ? 'danger' : (role === 'GUIDE' ? 'warn' : 'success');
    return `<span class="badge ${cls}">${role}</span>`;
  }

  async function search(q) {
    if (!q) {
      result.innerHTML = '<div class="helper">Enter an email or user ID to search.</div>';
      return;
    }
    result.innerHTML = '<div class="helper">Searching...</div>';
    try {
      const res = await fetch(`/api/users/search?q=${encodeURIComponent(q)}`, {
        headers: { 'Accept': 'application/json' }
      });
      if (res.ok) {
        const u = await res.json();
        result.innerHTML = `
          <div class="card">
            <div class="grid cols-2">
              <div>
                <div class="field"><span class="label">Name</span><div>${u.name ?? '—'}</div></div>
                <div class="field"><span class="label">Email</span><div>${u.email ?? '—'}</div></div>
              </div>
              <div>
                <div class="field"><span class="label">Phone</span><div>${u.phone ?? '—'}</div></div>
                <div class="field"><span class="label">Role</span><div>${roleBadge(u.role)}</div></div>
              </div>
            </div>
            <div class="spacer"></div>
            <div class="btn-row">
              <a class="btn" href="/ui/permissions?id=${u.id}">Update Permissions</a>
              <a class="btn secondary" href="/ui/profile?q=${u.id}">Open Profile</a>
            </div>
          </div>`;
      } else if (res.status === 404) {
        result.innerHTML = '<div class="helper">No user found.</div>';
      } else {
        result.innerHTML = `<div class="helper">Error: ${res.status}</div>`;
      }
    } catch (e) {
      result.innerHTML = '<div class="helper">Network error. Please try again.</div>';
    }
  }

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    const q = (input?.value || '').trim();
    search(q);
  });
})();
