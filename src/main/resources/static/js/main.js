window.onload = function() {
    document.querySelector('.menu_burger').onclick = function() {
        let active = Number(this.dataset.active),
            menu = this.parentElement.querySelector('.nav_menu');

        if (active === 0) {
            this.dataset.active = 1;
            menu.dataset.active = 1;
        } else {
            this.dataset.active = 0;
            menu.dataset.active = 0;
        }
    }

    if (document.querySelector('.field_file')) {
        document.querySelector('.field_file').onchange = function() {
            this.closest('form').submit();
        }
    }
}