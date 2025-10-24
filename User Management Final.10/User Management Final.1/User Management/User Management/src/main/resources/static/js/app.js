(function () {
  const navToggle = document.querySelector('.nav-toggle');
  const nav = document.querySelector('[data-nav]');
  if (navToggle && nav) {
    navToggle.addEventListener('click', () => {
      const expanded = navToggle.getAttribute('aria-expanded') === 'true';
      navToggle.setAttribute('aria-expanded', String(!expanded));
      nav.classList.toggle('show');
    });
    window.addEventListener('click', (e) => {
      if (!nav.contains(e.target) && e.target !== navToggle) {
        nav.classList.remove('show');
        navToggle.setAttribute('aria-expanded', 'false');
      }
    });
  }

  // Toast helper
  window.showToast = function (message, timeout = 2500) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    toast.textContent = message;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), timeout);
  };

  // Confirm modal (lightweight)
  function createModal(title, body, onConfirm) {
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop show';
    backdrop.innerHTML = `
      <div class="modal" role="dialog" aria-modal="true" aria-labelledby="m-title">
        <div class="modal-header" id="m-title">${title}</div>
        <div class="modal-body">${body}</div>
        <div class="modal-footer">
          <button class="btn secondary" data-close>Cancel</button>
          <button class="btn danger" data-confirm>Confirm</button>
        </div>
      </div>
    `;
    document.body.appendChild(backdrop);
    backdrop.querySelector('[data-close]').addEventListener('click', () => backdrop.remove());
    backdrop.addEventListener('click', (e) => { if (e.target === backdrop) backdrop.remove(); });
    backdrop.querySelector('[data-confirm]').addEventListener('click', () => { onConfirm?.(); backdrop.remove(); });
  }

  // Bind delete confirmation if present
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-confirm-delete]');
    if (!btn) return;
    e.preventDefault();
    const form = btn.closest('form');
    createModal('Delete Account', 'This action cannot be undone.', () => form?.submit());
  });
})();


